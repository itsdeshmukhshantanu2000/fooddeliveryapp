package com.service;

import com.dto.CartDTO;

public interface CartService {
	
	public CartDTO addToCart(int custid,int productid);

	public String deleteProduct(int custid, int prodid);
	

}
