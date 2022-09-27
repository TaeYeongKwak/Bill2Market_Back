package com.example.demo.controller;

import com.example.demo.model.item.ItemSaveRequestDTO;
import com.example.demo.model.item.ItemSearchRequestDTO;
import com.example.demo.model.response.CommonResult;
import com.example.demo.model.review.ItemReviewRequestDTO;
import com.example.demo.repository.ClientRepository;
import com.example.demo.service.ResponseService;
import com.example.demo.service.item.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Api(tags = {"3. Item"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ClientRepository clientRepository;
    private final ResponseService responseService;
    private final ItemService itemService;

    @ApiOperation(value = "기본 물품 리스트 조회", notes = "사용자와의 거리에 따른 물품 리스트를 조회한다.")
    @GetMapping("")
    public CommonResult itemList(ItemSearchRequestDTO itemSearchRequestDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(itemService.findItemList(itemSearchRequestDTO, (!auth.getName().equals("anonymousUser"))? Integer.parseInt(auth.getName()) : -1000));
    }

    @ApiOperation(value = "내 물품 보기", notes = "내가 올린 물품 목록을 조회한다.")
    @GetMapping("/me")
    public CommonResult myList(Integer page){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return responseService.getSingleResult(itemService.findItemsMe(Integer.parseInt(auth.getName()), page));
    }

    @ApiOperation(value = "기본 물품 상세 조회", notes = "번호에 맞는 물품을 조회한다.")
    @GetMapping("/{item-id}")
    public CommonResult itemDetail(@PathVariable("item-id") Integer itemId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(itemService.findItemOne(itemId, (!auth.getName().equals("anonymousUser"))? Integer.parseInt(auth.getName()) : -1000));
    }

    @ApiOperation(value = "사용자 판매 목록 조회", notes = "판매자가 판매한 목록들을 조회한다.")
    @GetMapping("/owner/{client-index}")
    public CommonResult ownerItemDetail(@PathVariable("client-index") Integer clientIndex,
                                        @RequestParam Integer page){
        return responseService.getSingleResult(itemService.findItemListByClientIndex(clientIndex, PageRequest.of(page,10)));
    }

    @ApiOperation(value = "물품 리뷰 조회", notes = "해당 물품의 리뷰를 조회한다.")
    @GetMapping("/{item-id}/review")
    public CommonResult itemReview(@PathVariable("item-id") Integer itemId, @RequestParam Integer page){
        return responseService.getSingleResult(itemService.findItemReview(itemId, page));
    }

    @ApiOperation(value = "게시물 저장", notes = "게시물 저장")
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResult itemSave(@RequestPart(value = "item") ItemSaveRequestDTO itemSaveRequestDTO,
                                 @RequestPart(value = "itemPhoto") List<MultipartFile> itemPhotoSaveRequest) throws IOException {

        itemService.saveItem(itemSaveRequestDTO, itemPhotoSaveRequest);
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "카테고리 검색", notes = "카테고리 검색")
    @GetMapping("/search-category")
    public CommonResult itemCategorySearch(ItemSearchRequestDTO itemSearchRequestDTO)  {//nearby도 구현
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(itemService.findByCategory((!auth.getName().equals("anonymousUser"))? Integer.parseInt(auth.getName()) : -1000, itemSearchRequestDTO));
    }
  
    @ApiOperation(value="게시물 검색", notes = "키워드 검색을 통해 게시물을 검색하여 물품 리스트를 조회한다.")
    @GetMapping("/search-keyword")
    public CommonResult searchItemList(ItemSearchRequestDTO itemSearchRequestDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(itemService.findItemByQuery(itemSearchRequestDTO, (!auth.getName().equals("anonymousUser"))? Integer.parseInt(auth.getName()) : -1000));
    }

    @ApiOperation(value = "물품 리뷰 작성", notes = "해당 물품의 리뷰를 작성한다.")
    @PostMapping("/review")
    public CommonResult writeItemReview(@Valid @RequestBody ItemReviewRequestDTO itemReviewRequestDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        itemService.saveItemReview(Integer.parseInt(auth.getName()), itemReviewRequestDTO);
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "게시물 삭제", notes = "해당 게시물을 삭제합니다.")
    @DeleteMapping("/{item-id}")
    public CommonResult deleteItem(@PathVariable("item-id") Integer itemId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        itemService.deleteItem(Integer.parseInt(auth.getName()), itemId);
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "게시물 수정", notes = "해당 게시물을 수정합니다.")
    @PutMapping("/{item-id}")
    public CommonResult itemModify(
            @PathVariable("item-id") Integer itemId,
            @RequestPart(value = "item") ItemSaveRequestDTO itemSaveRequestDTO,
            @RequestPart(value = "itemPhoto") List<MultipartFile> itemPhotoSaveRequest) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        itemService.modifyItem(Integer.parseInt(auth.getName()), itemId, itemSaveRequestDTO, itemPhotoSaveRequest);
        return responseService.getSuccessfulResult();
    }

}