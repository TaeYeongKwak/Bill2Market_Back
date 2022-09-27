package com.example.demo.model.basket;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="Basket")
public class Basket implements Serializable {

    @EmbeddedId
    private BasketPK basketPK;

    public Basket(int clientIndex, int itemId){
        this.basketPK = new BasketPK(clientIndex, itemId);
    }

}
