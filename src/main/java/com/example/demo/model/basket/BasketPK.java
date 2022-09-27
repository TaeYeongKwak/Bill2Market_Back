package com.example.demo.model.basket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class BasketPK implements Serializable {

    @Column(name = "client_index")
    private int clientIndex;
    @Column(name = "item_id")
    private int itemId;

}
