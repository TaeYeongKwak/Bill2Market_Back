package com.example.demo.model.item;


import lombok.Data;

@Data
public class OwnerInfo{
    private String nickname;
    private Float trustPoint;
    private String clientPhoto;

    public OwnerInfo(String nickname, Float trustPoint, String clientPhoto){
        this.nickname = nickname;
        this.trustPoint = trustPoint;
        this.clientPhoto = clientPhoto;
    }
}
