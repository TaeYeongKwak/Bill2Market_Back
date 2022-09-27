package com.example.demo.repository;

import com.example.demo.model.item.*;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.model.basket.QBasket.basket;
import static com.example.demo.model.item.QItem.item;
import static com.example.demo.model.item.QItemPhoto.itemPhoto1;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<SimpleItem> findByCategory(Integer clientIndex, ItemSearchRequestDTO itemSearchRequestDTO, Pageable pageable){
      JPAQuery<SimpleItem> query = jpaQueryFactory.select(new QSimpleItem(
                        item.itemId.as("itemId"),
                        item.itemTitle.as("itemTitle"),
                        item.itemAddress.as("itemAddress"),
                        item.price.as("price"),
                        item.deposit.as("deposit"),
                        itemPhoto1.itemPhoto.as("itemPhoto"),
                        item.contractStatus.as("contractStatus").stringValue(),
                        item.create_date,
                        JPAExpressions
                                .select(basket.basketPK.itemId)
                                .from(basket)
                                .where(basket.basketPK.itemId.eq(item.itemId)
                                       .and(basket.basketPK.clientIndex.eq(clientIndex))).exists().as("isLike")))
                .from(item)
                .leftJoin(item.photos, itemPhoto1)
                .on(itemPhoto1.isMain.eq(true))
                .where(
                        categoryBigEq(itemSearchRequestDTO.getCategoryBig()),
                        categoryMiddleEq(itemSearchRequestDTO.getCategoryMiddle()),
                        categorySmallEq(itemSearchRequestDTO.getCategorySmall()),
                        stDistanceSphere(itemSearchRequestDTO.getLatitude(), itemSearchRequestDTO.getLongitude())

                );
        return orderingCategory(query, itemSearchRequestDTO.getOrderType(), pageable, itemSearchRequestDTO.getLatitude(), itemSearchRequestDTO.getLongitude());
    }

    public List<SimpleItem> searchItemByItemIn(List<ElasticItem> itemList, ItemSearchRequestDTO itemSearchRequestDTO, Integer clientIndex, Pageable pageable){
        List<Integer> itemIdList = itemList.stream().map(ElasticItem::getItemId).collect(Collectors.toList());
        JPAQuery<SimpleItem> query = jpaQueryFactory.select(new QSimpleItem(
                        item.itemId.as("itemId"),
                        item.itemTitle.as("itemTitle"),
                        item.itemAddress.as("itemAddress"),
                        item.price.as("price"),
                        item.deposit.as("deposit"),
                        itemPhoto1.itemPhoto.as("itemPhoto"),
                        item.contractStatus.as("contractStatus").stringValue(),
                        item.create_date,
                        JPAExpressions
                                .select(basket.basketPK.itemId)
                                .from(basket)
                                .where(basket.basketPK.itemId.eq(item.itemId)
                                       .and(basket.basketPK.clientIndex.eq(clientIndex))).exists().as("isLike"))
                ).from(item)
                .leftJoin(item.photos, itemPhoto1)
                .on(itemPhoto1.isMain.eq(true))
                .where(
                        item.itemId.in(itemIdList),
                        stDistanceSphere(itemSearchRequestDTO.getLatitude(), itemSearchRequestDTO.getLongitude())
                );

        return ordering(query, itemSearchRequestDTO.getOrderType(), pageable, itemIdList);
    }

    private List<SimpleItem> ordering(JPAQuery<SimpleItem> query, OrderType orderType, Pageable pageable, List<Integer> itemIdList){
        query = (orderType == OrderType.ACCURATE) ? query.orderBy(Expressions.stringTemplate("FIELD({0}, {1})", item.itemId, itemIdList).asc()) :
                                                    query.orderBy(orderType.getOrder());
        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private List<SimpleItem> orderingCategory(JPAQuery<SimpleItem> query, OrderType orderType, Pageable pageable, Double latitude, Double longitude){//조건에 따른 거
        query = (orderType == OrderType.DISTANCE)? query.orderBy(stDistanceSphereSort(latitude, longitude)): query.orderBy(orderType.getOrder());
        return query.offset(pageable.getOffset()).limit(pageable.getPageSize()+1).fetch();

    }

    private OrderSpecifier stDistanceSphereSort(Double latitude, Double longitude){//거리순
        return Expressions.stringTemplate("ST_Distance_Sphere(POINT({0}, {1}), POINT(item_longitude, item_latitude))", longitude, latitude).asc();
    }

    private BooleanExpression stDistanceSphere(Double latitude, Double longitude){//6Km이내
        return (latitude == 0 || longitude == 0) ? null : Expressions.stringTemplate("ST_Distance_Sphere(POINT({0}, {1}), POINT(item_longitude, item_latitude))", longitude, latitude).loe("6000");
    }

    private BooleanExpression categoryBigEq(Integer categoryBig){
        return categoryBig != null ? item.categoryBig.eq(categoryBig) : null;
    }

    private BooleanExpression categoryMiddleEq(Integer categoryMiddle){
        return categoryMiddle != null ? item.categoryMiddle.eq(categoryMiddle) : null;
    }

    private BooleanExpression categorySmallEq(Integer categorySmall){
        return categorySmall != null ? item.categorySmall.eq(categorySmall) : null;
    }

}
