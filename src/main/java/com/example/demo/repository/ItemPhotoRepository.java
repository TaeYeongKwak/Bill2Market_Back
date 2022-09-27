package com.example.demo.repository;

import com.example.demo.model.client.Client;
import com.example.demo.model.item.Item;
import com.example.demo.model.item.ItemPhoto;
import com.example.demo.model.item.SimpleItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemPhotoRepository extends JpaRepository<ItemPhoto, Integer> {

}
