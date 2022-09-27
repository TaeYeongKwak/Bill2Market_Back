package com.example.demo.service.item;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.example.demo.exception.client.CAuthenticationEntryPointException;
import com.example.demo.exception.client.ClientNotFoundException;
import com.example.demo.exception.contract.ContractNotFoundException;
import com.example.demo.exception.review.DuplicateItemReviewException;
import com.example.demo.model.Document;
import com.example.demo.model.KakaoAddress;
import com.example.demo.model.contract.Contract;
import com.example.demo.model.contract.ReviewWrite;
import com.example.demo.model.review.ItemReviewRequestDTO;
import com.example.demo.model.review.Review;
import com.example.demo.model.review.ReviewType;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import com.example.demo.exception.item.ItemNotFoundException;
import com.example.demo.model.item.*;
import com.example.demo.model.review.ReviewResponseDTO;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.geo.Point;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService{

    private final ItemPhotoServiceImpl itemPhotoServiceImpl;
    private final ItemPhotoRepository itemPhotoRepository;
    private final ClientRepository clientRepository;
    private final BasketRepository basketRepository;
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final ItemRepositoryCustom itemRepositoryCustom;
    private final ContractRepository contractRepository;
    private final ContractRepositoryCustom contractRepositoryCustom;
    private final Gson gson;
    private final RestTemplate restTemplate;
    private final ElasticItemRepository elasticItemRepository;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Value("${kakao_api_key}")
    private String KAKAO_API_KEY;

    private Point getAddressPoint(String address){//kakao api
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com")
                .path("/v2/local/search/address.json")
                .queryParam("query", address)
                .queryParam("analyze_type", "similar")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

        Point point = null;
        try{
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
            KakaoAddress kakaoAddress = gson.fromJson(response.getBody(), KakaoAddress.class);
            if(kakaoAddress.getDocuments().size() > 0){
                Document doc = kakaoAddress.getDocuments().get(0);
                point = new Point(Double.parseDouble(doc.getX()), Double.parseDouble(doc.getY()));

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return point;
    }

    @Override
    public Slice<SimpleItem> findItemList(ItemSearchRequestDTO itemSearchRequestDTO, Integer clientIndex) {
        return itemRepository.findAllByLocation(itemSearchRequestDTO.getLongitude(), itemSearchRequestDTO.getLatitude(), clientIndex, PageRequest.of(itemSearchRequestDTO.getPage(), 10));
    }

    @Transactional
    @Override
    public void saveItem(ItemSaveRequestDTO itemSaveRequestDTO, List<MultipartFile> itemPhotoSaveRequest) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        itemSaveRequestDTO.setOwnerId(Integer.parseInt(auth.getName()));
        Point point = getAddressPoint(itemSaveRequestDTO.getItemAddress());
        Item item = itemRepository.save(itemSaveRequestDTO.toEntity(point));
        this.uploadItemPhoto(item, itemPhotoSaveRequest);
    }

    @Override
    public Slice<SimpleItem> findByCategory(Integer clientIndex, ItemSearchRequestDTO itemSearchRequestDTO) {
        Pageable pageable = PageRequest.of(itemSearchRequestDTO.getPage(), 10);
        List<SimpleItem> content = itemRepositoryCustom.findByCategory(clientIndex, itemSearchRequestDTO, pageable);
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public ItemDetailResponseDTO findItemOne(Integer itemId, Integer clientIndex) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        itemRepository.updateViews(itemId);
        return ItemDetailResponseDTO.builder()
                .ownerInfo(clientRepository.findOwnerInfoByClientIndex(item.getOwnerId()).orElseThrow(ClientNotFoundException::new))
                .item(item)
                .basketCount(basketRepository.countByItemId(itemId))
                .isLike(basketRepository.existsBasketByBasketPK(itemId, clientIndex) == 1? true : false)
                .build();
    }

    @Override
    public Slice<ReviewResponseDTO> findItemReview(Integer itemId, Integer page) {
        return reviewRepository.findSliceByItemId(itemId, PageRequest.of(page, 10));
    }

    @Override
    public Slice<SimpleItem> findItemByQuery(ItemSearchRequestDTO itemSearchRequestDTO, Integer clientIndex) {
        Pageable pageable = PageRequest.of(itemSearchRequestDTO.getPage(), 10);
        List<ElasticItem> searchList = elasticItemRepository.searchItemByQuery(itemSearchRequestDTO.getQuery());
        List<SimpleItem> content = itemRepositoryCustom.searchItemByItemIn(searchList, itemSearchRequestDTO,clientIndex, pageable);

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<ItemMeListResponseDTO> findItemsMe(Integer clientIndex, Integer page) {

        return itemRepository.findItemsMeByClientIndex(clientIndex, PageRequest.of(page,10));
    }
    public Slice<ItemOwnerResponseDTO> findItemListByClientIndex(Integer clientIndex, Pageable pageable) {

        return itemRepository.findByOwnerId(clientIndex, pageable);
    }

    @Transactional
    @Override
    public void saveItemReview(Integer clientIndex, ItemReviewRequestDTO itemReviewRequestDTO) {
        if(reviewRepository.existsByReviewWriterAndReviewItem(clientIndex, itemReviewRequestDTO.getItemId()))
            throw new DuplicateItemReviewException();

        Integer contractId = contractRepositoryCustom.getContractIdByItemIdAndLenterIndex(itemReviewRequestDTO.getItemId(), clientIndex).orElseThrow(ContractNotFoundException::new);
        reviewRepository.save(Review.builder()
                .contractId(contractId)
                .reviewItem(itemReviewRequestDTO.getItemId())
                .reviewWriter(clientIndex)
                .reviewType(ReviewType.ITEM)
                .reviewScore(itemReviewRequestDTO.getReviewScore())
                .reviewTitle(itemReviewRequestDTO.getReviewTitle())
                .reviewContent(itemReviewRequestDTO.getReviewContent())
                .reviewStatus(0)
                .build());

        Contract contract = contractRepository.findById(contractId).get();
        contract.setReviewWrite(ReviewWrite.WRITE);
        contractRepository.save(contract);
    }

    @Transactional
    @Override
    public void deleteItem(Integer clientIndex, Integer itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        if(!item.getOwnerId().equals(clientIndex))
            throw new CAuthenticationEntryPointException();

        this.deleteS3(item);
        itemRepository.delete(item);
    }

    @Transactional
    @Override
    public void modifyItem(Integer clientIndex, Integer itemId, ItemSaveRequestDTO itemSaveRequestDTO, List<MultipartFile> itemPhotoSaveRequest) throws IOException {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        if(!item.getOwnerId().equals(clientIndex))
            throw new CAuthenticationEntryPointException();

        this.deleteS3(item);

        Point point = getAddressPoint(itemSaveRequestDTO.getItemAddress());
        itemSaveRequestDTO.setItemLongitude(point.getX());
        itemSaveRequestDTO.setItemLatitude(point.getY());

        item.modifyItemInfo(itemSaveRequestDTO);
        itemRepository.save(item);

        itemPhotoRepository.deleteAll(item.getPhotos());
        this.uploadItemPhoto(item, itemPhotoSaveRequest);
    }

    private void deleteS3(Item item){
        String[] keys = item.getPhotos()
                .stream()
                .map(itemPhoto -> {
                    String[] keyArray = itemPhoto.getItemPhoto().split("/");
                    return keyArray[3] + "/" + keyArray[4];
                }).toArray(String[]::new);

        if(keys.length > 0){
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket).withKeys(keys);
            amazonS3Client.deleteObjects(deleteObjectsRequest);
        }
    }

    private void uploadItemPhoto(Item item, List<MultipartFile> itemPhotoSaveRequest) throws IOException {
        List<String> photoUrls = itemPhotoServiceImpl.upload(itemPhotoSaveRequest, "itemPhoto");

        boolean isMain = true;
        for(String url: photoUrls) {
            itemPhotoRepository.save(ItemPhoto.builder().itemId(item.getItemId()).itemPhoto(url).isMain(isMain).build());
            isMain = false;
        }
    }
}
