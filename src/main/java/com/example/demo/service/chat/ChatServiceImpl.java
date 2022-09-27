package com.example.demo.service.chat;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.example.demo.exception.chat.ChatFileCreateFailedException;
import com.example.demo.exception.client.ClientNotFoundException;
import com.example.demo.exception.client.ExistIdException;
import com.example.demo.exception.item.ItemNotFoundException;
import com.example.demo.model.chat.*;
import com.example.demo.model.client.Client;
import com.example.demo.model.item.Item;
import com.example.demo.repository.ChatRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.RedisChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService{

    private final ChatRepository chatRepository;
    private final ItemRepository itemRepository;
    private final AmazonS3Client amazonS3Client;
    private final RedisChatRepository redisChatRepository;
    private final ClientRepository clientRepository;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Override
    public ChatResponseDTO findChat(Integer itemId, Integer lenterIndex) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        Client lenter = clientRepository.findById(lenterIndex).orElseThrow(ClientNotFoundException::new);
        Chat chat = chatRepository.findByLenterAndItem(lenter, item).orElseGet(()->createChat(itemId, lenterIndex));
        String chatId = String.valueOf(chat.getChatId());
        redisChatRepository.setChatMessageUrl(chatId, chat.getFileName());
        return new ChatResponseDTO(chat,
                Integer.parseInt(redisChatRepository.getLastUser(chatId)),
                redisChatRepository.getNonReadCount(chatId));
    }

    private Chat createChat(Integer itemId, Integer lenterIndex){
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        Client lenter = clientRepository.findById(lenterIndex).orElseThrow(ClientNotFoundException::new);
        Client owner = clientRepository.findById(item.getOwnerId()).orElseThrow(ClientNotFoundException::new);
        File emptyTextFile = getEmptyTextFile(lenter, owner, item).orElseThrow(ChatFileCreateFailedException::new);
        String fileName =  "chat/" + UUID.randomUUID() + ".txt";
        String uploadImageUrl = putS3(emptyTextFile, fileName);
        if (!emptyTextFile.delete())
            log.info("로컬 파일 삭제 실패");

        return chatRepository.save(Chat.builder()
                .lenter(lenter)
                .owner(owner)
                .item(item)
                .fileName(uploadImageUrl)
                .createDate(LocalDate.now())
                .build());
    }

    private Optional<File> getEmptyTextFile(Client lenter, Client owner, Item item){
        File emptyTextFile = new File(System.getProperty("user.dir") + "/chat_message.txt");
        try {
            new FileOutputStream(emptyTextFile, false);
        } catch(FileNotFoundException e){
            log.error("로컬 파일 생성에 실패하였습니다. message:" + e.getMessage());
        }
        return Optional.of(emptyTextFile);
    }

    private String putS3(File uploadFile, String fileName) {
        try{
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("text/plain; charset=utf-8");
            metadata.setContentLength(uploadFile.length());
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, new FileInputStream(uploadFile), metadata).withCannedAcl(CannedAccessControlList.PublicRead));
        }catch(FileNotFoundException e){
            log.error("해당 파일이 존재하지 않습니다. message:" + e.getMessage());
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
  
  
    @Override
    public List<ChatListResponseDTO> findClientChatList(Integer clientIndex) {

        return chatRepository.findChatByClientIndex(clientIndex);
    }

    @Override
    public List<AlarmResponseDTO> getChatAlarmList(Integer clientIndex) {
        List<ChatListResponseDTO> chatList = chatRepository.findChatByClientIndex(clientIndex);
        return chatList.stream()
                .map(this::toAlarm)
                .filter(alarm -> alarm.getNonReadMessage() > 0)
                .collect(Collectors.toList());
    }

    private AlarmResponseDTO toAlarm(ChatListResponseDTO chat){
        String chatId = String.valueOf(chat.getChatId());
        Integer nonReadChat = redisChatRepository.getLastUser(chatId).equals(String.valueOf(chat.getOpponentIndex()))? redisChatRepository.getNonReadCount(chatId) : 0;
        return new AlarmResponseDTO(chat.getNickname(), nonReadChat, chat.getFileName());
    }

    @EventListener
    @Async
    public void messageSave(ChatMessageEvent chatMessageEvent){
        try{
            ChatMessage chatMessage = chatMessageEvent.getChatMessage();
            String fileUrl = redisChatRepository.getChatMessageUrl(String.valueOf(chatMessage.getChatId()));

            String[] keyArray = fileUrl.split("/");
            String key = keyArray[3] + "/" + keyArray[4];
            S3Object s3File = amazonS3Client.getObject(new GetObjectRequest(bucket, key));

            InputStream inputStream = s3File.getObjectContent();
            File tmp = File.createTempFile("s3file/" + chatMessage.getChatId(),".txt");
            Files.copy(inputStream, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmp,true), StandardCharsets.UTF_8));
            String message = chatMessage.getCreateDate() + " " + chatMessage.getMessageType().ordinal() + " "
                    + chatMessage.getSenderId() + " " + chatMessage.getMessage();
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            putS3(tmp, key);
            tmp.deleteOnExit();
            bufferedWriter.close();
        }catch (IOException e) {
            log.error("파일 입력에 문제가 발생하였습니다. {}", e.getMessage());
        }
    }

}
