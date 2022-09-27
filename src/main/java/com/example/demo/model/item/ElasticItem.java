package com.example.demo.model.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Id;

@ToString
@Builder
@Getter
@AllArgsConstructor
@Document(indexName = "item")
public class ElasticItem {

    @Id
    @Field(name = "item_id")
    private Integer itemId;
    @Field(name = "item_title")
    private String itemTitle;
    @Field(name = "item_content")
    private String itemContent;
    private Double score;

    public void setScore(double score){
        this.score = score;
    }

}
