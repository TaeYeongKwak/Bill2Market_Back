package com.example.demo.model.client;

import lombok.*;
import javax.persistence.*;
import java.util.Date;
import com.example.demo.model.item.OwnerInfo;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;


@DynamicInsert
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="Client")
@NamedNativeQuery(
        name = "ownerInfo",
        query = "SELECT nickname, ROUND(AVG(Review.review_score),1) AS trust_point, client_photo " +
                "FROM Item " +
                "INNER JOIN Review " +
                "ON Item.item_id = Review.review_item AND Review.review_type IN(0, 1, 2) " +
                "INNER JOIN Client " +
                "ON Client.client_index = Item.owner_id " +
                "AND Client.client_index = :client_index",
        resultSetMapping = "ownerInfoMapping"
)
@SqlResultSetMapping(
        name = "ownerInfoMapping",
        classes = @ConstructorResult(
                targetClass = OwnerInfo.class,
                columns = {
                        @ColumnResult(name = "nickname", type = String.class),
                        @ColumnResult(name = "trust_point", type = Float.class),
                        @ColumnResult(name = "client_photo", type = String.class)
                }
        )
)
public class Client {

    @Id
    @Column(name = "client_index")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientIndex;
    @Column(name = "client_id")
    private String clientId;
    @Column
    private String password;
    @Column
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column
    private Date birthdate;
    @Column(name = "sns_type")
    @Enumerated(EnumType.ORDINAL)
    private SnsType snsType;
    @Column(name = "client_name")
    private String clientName;
    @Column
    private String nickname;
    @Column(name = "subscribe")
    @Enumerated(EnumType.ORDINAL)
    private SubscribeStatus subscribeStatus;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private Role role;
    @Column(name = "client_photo")
    @ColumnDefault("https://bill2market.s3.ap-northeast-2.amazonaws.com/clientPhoto/default-img.png")
    private String clientPhoto;
    @Transient
    private Float trustPoint;
}
