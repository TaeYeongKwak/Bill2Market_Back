package com.example.demo.model.item;

import com.querydsl.core.types.OrderSpecifier;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.demo.model.item.QItem.item;

@AllArgsConstructor
@Getter
public enum OrderType {
    ACCURATE(null),
    RECENTLY(item.create_date.desc()),
    EXPENSIVE(item.price.desc()),
    INEXPENSIVE(item.price.asc()),
    DISTANCE(null);

    private OrderSpecifier order;

}
