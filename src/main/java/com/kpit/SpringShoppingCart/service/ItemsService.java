package com.kpit.SpringShoppingCart.service;

import com.kpit.SpringShoppingCart.entity.Items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ItemsService {

    Page<Items> getAllItems(Pageable page);

    Items getItemById(Long id);

    void deleteItemById(Long id);

    Items saveItemDetails(Items item);

    Items updateItemDetails(Long id , Items item);

    List<Items> readByCategory(String category, Pageable page);

    List<Items> readByName(String name , Pageable page);

    List<Items> readByDate(Date st , Date ed , Pageable page);
}
