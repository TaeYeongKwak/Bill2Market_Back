package com.example.demo.service.item;

import com.example.demo.model.item.*;
import com.example.demo.model.review.ItemReviewRequestDTO;
import com.example.demo.model.review.ReviewResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ItemService {
    public Slice<SimpleItem> findItemList(ItemSearchRequestDTO itemSearchRequestDTO, Integer clientIndex);
    public void saveItem(ItemSaveRequestDTO itemSaveRequestDTO, List<MultipartFile> itemPhotoSaveRequest) throws IOException;
    public ItemDetailResponseDTO findItemOne(Integer itemId, Integer clientIndex);
    public Slice<ReviewResponseDTO> findItemReview(Integer itemId, Integer page);
    public Slice<SimpleItem> findByCategory(Integer clientIndex, ItemSearchRequestDTO itemSearchRequestDTO);
    public Slice<SimpleItem> findItemByQuery(ItemSearchRequestDTO itemSearchRequestDTO, Integer clientIndex);
    public Slice<ItemMeListResponseDTO> findItemsMe(Integer clientIndex, Integer page);
    public Slice<ItemOwnerResponseDTO> findItemListByClientIndex(Integer clientIndex, Pageable pageable);
    public void saveItemReview(Integer clientIndex, ItemReviewRequestDTO itemReviewRequestDTO);

    public void deleteItem(Integer clientIndex, Integer itemId);
    public void modifyItem(Integer clientIndex, Integer itemId, ItemSaveRequestDTO itemSaveRequestDTO, List<MultipartFile> itemPhotoSaveRequest) throws IOException;
}
