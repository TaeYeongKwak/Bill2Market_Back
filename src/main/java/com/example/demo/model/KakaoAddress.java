package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class KakaoAddress {

    private Meta meta;
    private List<Document> documents;

    @Data
    private static class Meta{
        private int total_count;
        private int pageable_count;
        private boolean is_end;
    }
}
