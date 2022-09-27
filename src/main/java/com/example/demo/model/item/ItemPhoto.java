package com.example.demo.model.item;

import lombok.*;

import javax.persistence.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="Item_Photo")
public class ItemPhoto {
    @Id
    @Column(name = "item_photo_index")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemPhotoIndex;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "item_photo")
    private String itemPhoto;

    @Column(name = "is_main")
    private Boolean isMain;
}
