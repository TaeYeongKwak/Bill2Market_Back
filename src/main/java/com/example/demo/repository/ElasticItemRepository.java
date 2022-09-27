package com.example.demo.repository;

import com.example.demo.model.item.ElasticItem;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@RequiredArgsConstructor
@Repository
public class ElasticItemRepository{

    private final ElasticsearchOperations elasticsearchOperations;

    public List<ElasticItem> searchItemByQuery(String query) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQuery(query)
                        .field("item_title.nori")
                        .field("item_content.nori")
                        .type(MultiMatchQueryBuilder.Type.BEST_FIELDS))
                        .build();

        SearchHits<ElasticItem> search = elasticsearchOperations.search(searchQuery, ElasticItem.class);
        return search.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

    }

}
