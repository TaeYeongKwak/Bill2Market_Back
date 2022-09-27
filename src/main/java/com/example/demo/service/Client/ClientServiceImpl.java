package com.example.demo.service.Client;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.exception.client.ClientNotFoundException;
import com.example.demo.exception.client.ExistNicknameException;
import com.example.demo.exception.common.HttpFailException;
import com.example.demo.exception.item.ItemNotFoundException;
import com.example.demo.model.client.Client;
import com.example.demo.model.client.Role;
import com.example.demo.model.client.SnsType;
import com.example.demo.model.item.Item;
import com.example.demo.model.response.CommonResult;
import com.example.demo.model.review.ItemReviewResponseDTO;
import com.example.demo.model.review.ReviewResponseDTO;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Value("${photo.url.default-img}")
    public String defaultPhotoUrl;

    @Override
    public void setNickname(int clientIndex, String nickName) {
        Client client = clientRepository.findById(clientIndex).orElseThrow(ClientNotFoundException::new);
        if(clientRepository.findByNickname(nickName).isPresent()) {
            throw new ExistNicknameException();
        } else{
            client.setNickname(nickName);
        }
        clientRepository.save(client);
    }

    @Override
    public CommonResult getClientFromNaver(String naverToken) {
        try {
            HttpGet httpGet = new HttpGet("https://openapi.naver.com/v1/nid/me");
            httpGet.setHeader("Authorization", "BEARER " + naverToken);

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            CloseableHttpResponse response = httpClient.execute(httpGet);

            if(response.getStatusLine().getStatusCode() == 200){
                ResponseHandler<String> handler = new BasicResponseHandler();

                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(handler.handleResponse(response), Map.class);
                Map<String, String> clientInfo = (Map<String, String>)map.get("response");

                Client client = clientRepository.findByPhoneNumber(clientInfo.get("mobile"));
                Date date = Date.valueOf(clientInfo.get("birthyear") + "-" + clientInfo.get("birthday"));

                if(client == null){ // 회원가입
                    client = Client.builder()
                            .email(clientInfo.get("email"))
                            .phoneNumber(clientInfo.get("mobile"))
                            .clientName(clientInfo.get("name"))
                            .birthdate(date)
                            .snsType(SnsType.NAVER)
                            .role(Role.USER)
                            .build();
                    int tmpIndex = clientRepository.save(client).getClientIndex();
                    return responseService.getNeedNickname(tmpIndex);
                }else{ // 로그인
                    Map<String, Object> loginMap = new HashMap<>();
                    return responseService.getLoginResponse(jwtTokenProvider.createToken(client.getClientIndex(), client.getRole()), client.getClientIndex());
                }
            }else{
                throw new HttpFailException();
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new HttpFailException();
        }
    }

    @Override
    public Slice<ReviewResponseDTO> getReviewByOwnerIndex(Integer itemId, Integer page) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        return reviewRepository.findSliceByClientIndex(item.getOwnerId(), PageRequest.of(page,10));
    }
    
    @Override
    public Slice<ItemReviewResponseDTO> getItemReviewByOwnerIndex(Integer clientIndex, Integer page){
        return reviewRepository.findSliceAllByClientIndex(clientIndex, PageRequest.of(page,10));
    }
    
    @Override
    public Client findById(Integer clientIndex) {
        Client client = clientRepository.findById(clientIndex).orElseThrow(ClientNotFoundException::new);
        client.setTrustPoint(clientRepository.findReviewPointByClientIndex(clientIndex));
        return client;
    }

    @Override
    public void modifyClientPhoto(Integer clientIndex, MultipartFile clientPhoto) throws IOException {
        Client client = clientRepository.findById(clientIndex).orElseThrow(ClientNotFoundException::new);
        String leastClientPhoto = client.getClientPhoto();
        File uploadFile = convert(clientPhoto).orElseThrow(() -> new IllegalArgumentException("파일 변환 실패"));
        client.setClientPhoto(upload(uploadFile, "clientPhoto"));
        clientRepository.save(client);

        if(!leastClientPhoto.equals(defaultPhotoUrl)){
            log.info("프로필 이미지 삭제");
            deleteClientPhoto(leastClientPhoto);
        }
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장시킨 이미지 삭제
    private void removeNewFile(File targetFile) {
        if (!targetFile.delete()) {
            log.info("로컬 파일 삭제 실패");
        }
    }

    // 로컬에 파일 업로드
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 위에서 지정한 경로에 파일 생성
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // 데이터를 파일에 바이트 스트림으로 저장
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private void deleteClientPhoto(String url){
        String[] keyArray = url.split("/");
        String key = keyArray[3] + "/" + keyArray[4];
        amazonS3Client.deleteObject(bucket, key);
    }

}
