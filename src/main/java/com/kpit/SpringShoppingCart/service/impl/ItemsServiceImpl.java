package com.kpit.SpringShoppingCart.service.impl;

import com.kpit.SpringShoppingCart.Repository.ItemsRepository;
import com.kpit.SpringShoppingCart.entity.Items;
import com.kpit.SpringShoppingCart.exceptions.ItemNotFoundException;
import com.kpit.SpringShoppingCart.service.ItemsService;

import com.kpit.SpringShoppingCart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private UserService userService;

    @Override
    public Page<Items> getAllItems(Pageable page) {
        return itemsRepository.findByUserId(userService.getLoggedInUser().getId(),page);
    }

    @Override
    public Items getItemById(Long id) {
        Optional<Items> item =  itemsRepository.findByUserIdAndId(userService.getLoggedInUser().getId(),id);
        if(item.isPresent()){
            return item.get();
        }
        throw new ItemNotFoundException("Item is not found for id : "+id);
    }

    @Override
    public void deleteItemById(Long id) {
        Items item = getItemById(id);
        itemsRepository.delete(item);
    }

    @Override
    public Items saveItemDetails(Items item) {
        item.setUser(userService.getLoggedInUser());
        return  itemsRepository.save(item);
    }

    @Override
    public Items updateItemDetails(Long id, Items item) {
        Items existingItem = getItemById(id);
        existingItem.setItemName(item.getItemName() == null ? existingItem.getItemName() : item.getItemName());
        existingItem.setCategory(item.getCategory() == null ? existingItem.getCategory() : item.getCategory());
        existingItem.setDescription(item.getDescription() == null ? existingItem.getDescription() : item.getDescription());
        existingItem.setCost(item.getCost() == null ? existingItem.getCost() : item.getCost());
        existingItem.setDate(item.getDate() == null ? existingItem.getDate() : item.getDate());

        return itemsRepository.save(existingItem);
    }

    @Override
    public List<Items> readByCategory(String category, Pageable page) {
        return itemsRepository.findByUserIdAndCategory(userService.getLoggedInUser().getId(),category,page).toList();
    }

    @Override
    public List<Items> readByName(String name, Pageable page) {
        Long id = userService.getLoggedInUser().getId();
        return itemsRepository.findByUserIdAndItemNameContaining(id,name,page).toList();
    }

    @Override
    public List<Items> readByDate(Date startDate, Date endDate, Pageable page) {
        if(startDate == null){
            startDate = new Date(0);
        }

        if(endDate == null){
            endDate = new Date(System.currentTimeMillis());
        }
        Long id = userService.getLoggedInUser().getId();

        return itemsRepository.findByUserIdAndDateBetween(id,startDate,endDate,page).toList();
    }
}
