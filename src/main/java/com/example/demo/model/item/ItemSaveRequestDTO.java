package com.example.demo.model.item;

import lombok.Data;
import org.springframework.data.geo.Point;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class ItemSaveRequestDTO {

    private Integer categoryBig;
    private Integer categoryMiddle;
    private Integer categorySmall;
    @NotBlank(message = "물품의 제목이 입력되지 않았습니다.")
    private String itemTitle;
    @NotBlank(message = "물품의 내용이 입력되지 않았습니다.")
    private String itemContent;
    private ItemQuality itemQuality;
    private int price;
    private int deposit;
    @NotBlank(message = "물품의 주소가 입력되지 않았습니다.")
    private String itemAddress;
    private double itemLatitude;
    private double itemLongitude;
    private Integer ownerId;
    private String startDate;
    private String endDate;

    public Item toEntity(Point point) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return Item.builder()
                .itemLongitude(point.getX())
                .itemLatitude(point.getY())
                .ownerId(ownerId)
                .itemTitle(itemTitle)
                .categoryBig(categoryBig)
                .categoryMiddle(categoryMiddle)
                .categorySmall(categorySmall)
                .itemContent(itemContent)
                .itemQuality(itemQuality)
                .price(price)
                .deposit(deposit)
                .itemAddress(itemAddress)
                .views(0)
                .contractStatus(0)
                .startDate(LocalDate.parse(getStartDate(), dateTimeFormatter))
                .endDate(LocalDate.parse(getEndDate(), dateTimeFormatter))
                .build();
    }

}
