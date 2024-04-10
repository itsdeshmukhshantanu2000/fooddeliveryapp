package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.CartItem;
import com.entity.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
	public  List<CartItem> findByProduct(Product product);
}
