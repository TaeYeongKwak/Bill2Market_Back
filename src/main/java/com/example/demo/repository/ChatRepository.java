package com.example.demo.repository;

import com.example.demo.model.chat.Chat;
import com.example.demo.model.client.Client;
import com.example.demo.model.item.Item;
import com.example.demo.model.chat.ChatListResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Integer> {
  
    public Optional<Chat> findByLenterAndItem(Client lenter, Item item);

    @Query(name = "chatList", nativeQuery = true)
    public List<ChatListResponseDTO> findChatByClientIndex(@Param("client_index") Integer clientIndex);
}
