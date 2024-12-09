package com.kpit.SpringShoppingCart.controller;

import com.kpit.SpringShoppingCart.entity.Items;
import com.kpit.SpringShoppingCart.service.ItemsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ShoppingController {

    @Autowired
    private ItemsService itemsService;

    @GetMapping("/items")
    public List<Items> getAllItems(Pageable page){
        return itemsService.getAllItems(page).toList();
    }

    @GetMapping("/items/{id}")
    public Items getItemById(@PathVariable("id") Long id){
        return itemsService.getItemById(id);
    }

    @DeleteMapping("/items")
    public String deleteItemById(@RequestParam("id") Long id){
        itemsService.deleteItemById(id);
        return "The item with id "+id+"  is deleted";
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/items")
    public Items saveItemDetails(@Valid @RequestBody Items item){
        return itemsService.saveItemDetails(item);
    }

    @PutMapping("/items/{id}")
    public Items updateItemDetails(@PathVariable("id") Long id , @RequestBody Items item){
        return itemsService.updateItemDetails(id,item);
    }

    @GetMapping("/items/category")
    public List<Items> getItemsByCategory(@RequestParam String category,Pageable page){
        return itemsService.readByCategory(category,page);
    }

    @GetMapping("/items/name")
    public List<Items> getItemsByName(@RequestParam("itemName") String name,Pageable page){
        return itemsService.readByName(name,page);
    }

    @GetMapping("/items/date")
    public List<Items> getItemsByDate(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, Pageable page){
        return itemsService.readByDate(startDate,endDate,page);
    }


}
