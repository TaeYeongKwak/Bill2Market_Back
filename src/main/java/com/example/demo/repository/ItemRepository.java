package com.example.demo.repository;


import com.example.demo.model.item.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query(name = "SimpleItemSliceByLocation", nativeQuery = true)
    public Slice<SimpleItem> findAllByLocation(@Param("client_longitude") double clientLongitude, @Param("client_latitude") double clientLatitude, @Param("client_index") Integer clientIndex, Pageable pageable);

    @Query(name = "ItemOwnerByOwnerId", nativeQuery = true)
    public Slice<ItemOwnerResponseDTO> findByOwnerId(@Param("owner_id") Integer ownerId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update Item set views = views + 1 where itemId = :itemId")
    public void updateViews(@Param("itemId")Integer itemId);

    @Query(name = "ItemsMeByClientIndex", nativeQuery = true)
    public Slice<ItemMeListResponseDTO> findItemsMeByClientIndex(@Param("client_index") Integer clientIndex, Pageable page);
}
