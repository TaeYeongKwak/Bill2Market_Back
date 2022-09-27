package com.example.demo.controller;

import com.example.demo.model.response.CommonResult;
import com.example.demo.service.ResponseService;
import com.example.demo.service.basket.BasketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"4. Basket"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/baskets")
public class BasketController {

    private final ResponseService responseService;
    private final BasketService basketService;

    @ApiOperation(value = "찜한 횟수", notes = "해당 물품을 찜한 사용자의 수를 조회한다.")
    @GetMapping("/{item-id}")
    public CommonResult countBasket(@PathVariable("item-id") Integer itemId){
        return responseService.getSingleResult(basketService.countBasketByItem(itemId));
    }

    @ApiOperation(value = "찜하기", notes = "사용자가 해당 물품을 찜한다.")
    @PostMapping("")
    public CommonResult addBasket(Integer itemId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        basketService.saveBasket(Integer.parseInt(auth.getName()), itemId);
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "찜하기 취소", notes = "사용자가 해당 물품의 찜하기를 취소한다.")
    @DeleteMapping("")
    public CommonResult deleteBasket(Integer itemId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        basketService.deleteBasket(Integer.parseInt(auth.getName()), itemId);
        return responseService.getSuccessfulResult();
    }

    @ApiOperation(value = "찜 목록 보기", notes = "내가 찜한 물품 목록을 본다.")
    @GetMapping("/me")
    public CommonResult basketList(Integer page){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return responseService.getSingleResult(basketService.findMyBasketList(Integer.parseInt(auth.getName()), page));
    }

}
