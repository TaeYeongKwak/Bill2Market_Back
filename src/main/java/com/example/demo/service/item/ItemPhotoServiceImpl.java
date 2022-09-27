package com.example.demo.service.item;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemPhotoServiceImpl {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public List<String> upload(List<MultipartFile> multipartFile, String dirName) throws IOException {
        List<String> urls = new ArrayList<String>();
        for(MultipartFile a: multipartFile){
            File uploadFile = convert(a).orElseThrow(() -> new IllegalArgumentException("파일 변환 실패"));
            urls.add(upload(uploadFile, dirName));
        }
        return urls;
    }

    // S3로 파일 업로드하기
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
}