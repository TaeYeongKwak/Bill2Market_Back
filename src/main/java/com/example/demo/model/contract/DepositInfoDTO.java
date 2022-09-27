package com.example.demo.model.contract;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
@Builder
public class DepositInfoDTO {

    private String cntr_account_type; //N
    private String cntr_account_num; //빌리페이 계좌번호
    private String wd_pass_phrase; //None
    private String wd_print_content; //보증금 반납및 대여료 전송
    private String name_check_option; // off
    private String tran_dtime; // 현재날짜
    private String req_cnt; // 1
    private List<DepositInfoReqListDTO> req_list;

}

