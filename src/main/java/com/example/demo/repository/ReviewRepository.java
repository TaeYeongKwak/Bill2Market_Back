package com.example.demo.repository;

import com.example.demo.model.review.ItemReviewResponseDTO;
import com.example.demo.model.review.Review;
import com.example.demo.model.review.ReviewResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(name = "ReviewByItemId", nativeQuery = true)
    public Slice<ReviewResponseDTO> findSliceByItemId(@Param("item_id") Integer itemId, Pageable page);

    @Query(name = "ReviewByOwnerId", nativeQuery = true)
    public Slice<ReviewResponseDTO> findSliceByClientIndex(@Param("client_index") Integer clientIndex, Pageable page);

    @Query(name = "ItemReviewByOwnerId", nativeQuery = true)
    public Slice<ItemReviewResponseDTO> findSliceAllByClientIndex(@Param("client_index") Integer clientIndex, Pageable page);

    public boolean existsByReviewWriterAndReviewItem(Integer reviewWriter, Integer reviewItem);
}
