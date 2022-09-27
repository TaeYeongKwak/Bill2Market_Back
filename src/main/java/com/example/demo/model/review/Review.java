package com.example.demo.model.review;

import com.example.demo.model.item.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="Review")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "ReviewByItemId",
                query = "SELECT review_score, review_title, review_content, create_date, nickname AS 'writer' " +
                        "FROM Review " +
                        "LEFT JOIN Client " +
                        "ON review_writer = client_index " +
                        "WHERE :item_id = review_item " +
                        "AND Review.review_type = '2'",
                resultSetMapping = "ReviewMapping"
        ),
        @NamedNativeQuery(
                name = "ReviewByOwnerId",
                query = "SELECT review_score, review_title, review_content, create_date, Writer.nickname AS 'writer' " +
                        "FROM Review " +
                        "LEFT JOIN Client AS Target " +
                        "ON review_target = Target.client_index " +
                        "INNER JOIN Client AS Writer " +
                        "ON review_writer = Writer.client_index " +
                        "WHERE :client_index = Target.client_index " +
                        "AND review_type = '0'",
                resultSetMapping = "ReviewMapping"
        ),
        @NamedNativeQuery(
                name = "ItemReviewByOwnerId",
                query = "SELECT review_score, review_title, review_content, Review.create_date, Writer.nickname AS 'writer', Item.item_title " +
                        "FROM Review " +
                        "INNER JOIN Item " +
                        "ON Item.item_id = Review.review_item " +
                        "INNER JOIN Client AS Writer " +
                        "ON Writer.client_index = review_writer " +
                        "WHERE Item.owner_id = :client_index " +
                        "AND Review.review_type = 2 " +
                        "ORDER BY Review.create_date DESC",
                resultSetMapping = "ItemReviewMapping"
        )
})
@SqlResultSetMapping(
        name = "ReviewMapping",
        classes = @ConstructorResult(
                targetClass = ReviewResponseDTO.class,
                columns = {
                        @ColumnResult(name = "review_score", type = Integer.class),
                        @ColumnResult(name = "review_title", type = String.class),
                        @ColumnResult(name = "review_content", type = String.class),
                        @ColumnResult(name = "create_date", type = Date.class),
                        @ColumnResult(name = "writer", type = String.class),
                }
        ))
@SqlResultSetMapping(
        name = "ItemReviewMapping",
        classes = @ConstructorResult(
                targetClass = ItemReviewResponseDTO.class,
                columns = {
                        @ColumnResult(name = "review_score", type = Integer.class),
                        @ColumnResult(name = "review_title", type = String.class),
                        @ColumnResult(name = "review_content", type = String.class),
                        @ColumnResult(name = "create_date", type = Date.class),
                        @ColumnResult(name = "writer", type = String.class),
                        @ColumnResult(name = "item_title", type = String.class)
                }
        ))
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;
    @Column(name = "contract_id")
    private int contractId;
    @Column(name = "review_writer")
    private Integer reviewWriter;
    @Column(name = "review_target")
    private Integer reviewTarget;
    @Column(name = "review_item")
    private Integer reviewItem;
    @Column(name = "review_type")
    @Enumerated(EnumType.ORDINAL)
    private ReviewType reviewType;
    @Column(name = "review_score")
    private int reviewScore;
    @Column(name = "review_title")
    private String reviewTitle;
    @Column(name = "review_content")
    private String reviewContent;
    @Column(name = "review_status")
    private Integer reviewStatus;
}
