package com.example.demo.model.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@AllArgsConstructor
@ToString
@Data
public class ItemReviewRequestDTO {

    @Positive(message = "물품 번호가 입력되지 않았습니다.")
    private Integer itemId;
    @NotBlank(message = "리뷰 제목이 입력되지 않았습니다.")
    private String reviewTitle;
    @NotBlank(message = "리뷰 내용이 입력되지 않았습니다.")
    private String reviewContent;
    @PositiveOrZero(message = "리뷰 점수가 입력되지 않았습니다.")
    private Integer reviewScore;

}
