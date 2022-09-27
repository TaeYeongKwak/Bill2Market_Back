package com.example.demo.service.basket;

import com.example.demo.exception.basket.BasketNotFoundException;
import com.example.demo.exception.basket.DuplicateBasketException;
import com.example.demo.exception.item.ItemNotFoundException;
import com.example.demo.model.basket.Basket;
import com.example.demo.model.basket.BasketMyListResponseDTO;
import com.example.demo.model.basket.BasketPK;
import com.example.demo.repository.BasketRepository;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasketServiceImpl implements BasketService{

    private final BasketRepository basketRepository;
    private final ItemRepository itemRepository;

    @Override
    public long countBasketByItem(int itemId) {
        return basketRepository.countByItemId(itemId);
    }

    @Override
    public Basket saveBasket(int clientIndex, int itemId) {
        if (!itemRepository.existsById(itemId)) throw new ItemNotFoundException();
        else if (basketRepository.existsById(new BasketPK(clientIndex, itemId))) throw new DuplicateBasketException();

        return basketRepository.save(new Basket(clientIndex, itemId));
    }

    @Override
    public void deleteBasket(int clientIndex, int itemId) {
        basketRepository.delete(basketRepository.findById(new BasketPK(clientIndex, itemId))
                .orElseThrow(BasketNotFoundException::new));
    }

    @Override
    public Slice<BasketMyListResponseDTO> findMyBasketList(Integer ownerId, Integer page) {

        return basketRepository.findBasketListByClientIndex(ownerId, PageRequest.of(page, 10));
    }

}
