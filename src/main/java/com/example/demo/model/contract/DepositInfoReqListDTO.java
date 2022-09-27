package com.example.demo.model.contract;

import lombok.Data;

@Data
public class DepositInfoReqListDTO {

    private String tran_no; //1
    private String bank_tran_id; //M202200946 + U + 난수발생기
    private String fintech_use_num; // 고객의 finId (lenter, owner)
    private String print_content; //빌리페이 보증금 환급, 빌리페이 대여료
    private String tran_amt; // deposit, price
    private String req_client_name; // lenterName, ownerName
    private String req_client_num; // BILL + lenterIndex, BILL + ownerIndex
    private String req_client_fintech_use_num;
    private String transfer_purpose;// TR, ST

}
