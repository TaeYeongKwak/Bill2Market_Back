package com.example.demo.model.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Data
@ToString
public class AccountUserInfoResponseDTO {

    private String api_tran_id;
    private String rsp_code;
    private String rsp_message;
    private String api_tran_dtm;
    private String user_seq_no;
    private String user_ci;
    private String user_name;
    private String res_cnt;
    private List<Registration> res_list;

    @ToString
    @Data
    public class Registration{
        private String fintech_use_num;
        private String account_alias;
        private String bank_code_std;
        private String bank_code_sub;
        private String bank_name;
        private String savings_bank_name;
        private String account_num_masked;
        private String account_holder_name;
        private String account_holder_type;
        private String inquiry_agree_yn;
        private String inquiry_agree_dtime;
        private String transfer_agree_yn;
        private String transfer_agree_dtime;
        private String payer_num;
        private String account_seq;
        private String account_type;
    }
}
