package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.entity.OrderedCart;

@Repository
public interface OrderedCartRepository extends JpaRepository<OrderedCart, Integer> {

	// Custom Repository method
	@Query(value = "select * from ordered_cart where orderid=?1", nativeQuery = true)
	public OrderedCart findByOrderId(int orderid);

	// Custom Repository method
	@Query(value = "select * from ordered_cart where customer_id=?1", nativeQuery = true)
	public List<OrderedCart> findByCustomerId(int orderid);

	@Modifying
	@Transactional
	@Query(value = "delete from ordered_cart where orderid = ?1 ", nativeQuery = true)
	public void deleteByOrderId(int id);

}
