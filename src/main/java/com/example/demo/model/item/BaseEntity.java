package com.example.demo.model.item;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity { //자동 생성일, 수정일 기록
    @CreatedDate
    private LocalDate create_date; //create_date 컬럼


    @LastModifiedDate
    private LocalDate update_date; //update_date 컬럼
}