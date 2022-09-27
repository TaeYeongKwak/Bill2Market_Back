package com.example.demo.service.basket;

import com.example.demo.model.basket.Basket;
import com.example.demo.model.basket.BasketMyListResponseDTO;
import org.springframework.data.domain.Slice;


public interface BasketService {

    public long countBasketByItem(int itemId);
    public Basket saveBasket(int clientIndex, int itemId);
    public void deleteBasket(int clientIndex, int itemId);
    public Slice<BasketMyListResponseDTO> findMyBasketList(Integer ownerId, Integer page);
}
