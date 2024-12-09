package com.kpit.SpringShoppingCart.Repository;

import com.kpit.SpringShoppingCart.entity.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ItemsRepository extends JpaRepository<Items,Long> {

    //SELECT * FROM tbl_expenses WHERE user_id=? AND category='%category value%'
    Page<Items> findByUserIdAndCategory(Long userId,String category,Pageable page);

    //SELECT * FROM tbl_expenses WHERE user_id=? AND name LIKE '%keyword%'
    Page<Items> findByUserIdAndItemNameContaining(Long userId, String keyword,Pageable page);

    //SELECT * FROM tbl_expenses WHERE user_id=? AND date BETWEEN 'startDate' AND 'endDate'
    Page<Items> findByUserIdAndDateBetween(Long userId, Date startDate, Date endDate, Pageable page);

    Page<Items> findByUserId(Long userId, Pageable page);

    Optional<Items> findByUserIdAndId(Long userId, Long itemId);
}
