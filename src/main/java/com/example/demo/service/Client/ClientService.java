package com.example.demo.service.Client;

import com.example.demo.model.client.Client;
import com.example.demo.model.response.CommonResult;
import com.example.demo.model.review.ItemReviewResponseDTO;
import com.example.demo.model.review.ReviewResponseDTO;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ClientService {

    public void setNickname(int clientIndex, String nickName);
    public CommonResult getClientFromNaver(String naverToken);
    public Slice<ReviewResponseDTO> getReviewByOwnerIndex(Integer itemId, Integer page);
    public Slice<ItemReviewResponseDTO> getItemReviewByOwnerIndex(Integer clientIndex, Integer page);
    public Client findById(Integer clientIndex);
    public void modifyClientPhoto(Integer clientIndex, MultipartFile clientPhoto) throws IOException;

}
