package com.example.demo.model.review;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class ItemReviewResponseDTO {

    private Integer reviewScore;
    private String reviewTitle;
    private String reviewContent;
    private Date createDate;
    private String writer;
    private String itemTitle;

}
