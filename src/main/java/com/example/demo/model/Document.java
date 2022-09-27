package com.example.demo.model;

import lombok.Data;

@Data
public class Document{
    private String address_name;
    private String address_type;
    private String x;
    private String y;
    private Address address;
    private RoadAddress road_address;

    @Data
    private static class Address{
        private String address_name;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String region_3depth_h_name;
        private String h_code;
        private String b_code;
        private String mountain_yn;
        private String main_address_no;
        private String sub_address_no;
        private String zip_code;
        private String x;
        private String y;
    }

    @Data
    private static class RoadAddress{
        private String address_name;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String road_name;
        private String underground_yn;
        private String main_building_no;
        private String sub_building_no;
        private String building_name;
        private String zone_no;
        private String x;
        private String y;
    }
}
