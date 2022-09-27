package com.example.demo.model.chat;

import com.example.demo.model.item.OwnerInfo;
import com.example.demo.model.client.Client;
import com.example.demo.model.item.BaseEntity;
import com.example.demo.model.item.Item;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="Chat")
@NamedNativeQuery(
        name = "chatList",
        query = "SELECT IFNULL(Contract.contract_id, -1) AS 'contract_id', Chat.item_id,  Chat.chat_id, Chat.file_name, Client.nickname, Client.client_photo, " +
                "CASE WHEN Chat.owner_index = :client_index THEN Chat.lenter_index " +
                "ELSE Chat.owner_index END as opponentIndex " +
                "FROM Chat " +
                "INNER JOIN Client ON Client.client_index = CASE WHEN Chat.owner_index = :client_index THEN Chat.lenter_index " +
                "ELSE Chat.owner_index END " +
                "LEFT JOIN Contract ON Contract.chat_id=Chat.chat_id "+
                "WHERE Chat.owner_index = :client_index OR Chat.lenter_index = :client_index " +
                "ORDER BY Chat.item_id DESC ",
        resultSetMapping = "chatListMapping"
)
@SqlResultSetMapping(
        name = "chatListMapping",
        classes = @ConstructorResult(
                targetClass = ChatListResponseDTO.class,
                columns = {
                        @ColumnResult(name = "contract_id", type = Integer.class),
                        @ColumnResult(name = "item_id", type = Integer.class),
                        @ColumnResult(name = "chat_id", type = Integer.class),
                        @ColumnResult(name = "opponentIndex", type = Integer.class),
                        @ColumnResult(name = "file_name", type = String.class),
                        @ColumnResult(name = "nickname", type = String.class),
                        @ColumnResult(name = "client_photo", type = String.class)
                }
        )
)
public class Chat {
    @Id
    @Column(name = "chat_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatId;

    @OneToOne
    @JoinColumn(name = "lenter_index")
    private Client lenter;
    @OneToOne
    @JoinColumn(name = "owner_index")
    private Client owner;
    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

}
