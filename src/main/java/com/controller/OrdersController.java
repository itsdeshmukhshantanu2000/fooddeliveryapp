package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.OrdersDTO;
import com.serviceimpl.OrdersServiceImpl;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*") // Frontend Connection
public class OrdersController {

	@Autowired
	private OrdersServiceImpl ordersServiceImpl;

	@PostMapping("/placeOrder/{custid}")
	public OrdersDTO addOrder(@PathVariable(value = "custid") int custid) {
		return ordersServiceImpl.addOrders(custid);
	}

	@GetMapping("/find/{orderId}")
	public OrdersDTO FindById(@PathVariable(value = "orderId") int orderId) {
		return ordersServiceImpl.getById(orderId);
	}

	@GetMapping("/findall")
	public List<OrdersDTO> FindAllOrders() {
		return ordersServiceImpl.findAll();
	}

	@DeleteMapping("/deleteOrder/{no}")
	public String deleteOrder(@PathVariable(value = "no") int no) {
		return ordersServiceImpl.deleteOrders(no);

	}

	@GetMapping("/findBycustId/{custId}")
	public List<OrdersDTO> getOrderCustId(@PathVariable(value = "custId") int custId) {
		return ordersServiceImpl.getOrderCustomerId(custId);
	}

	@PutMapping("/status/{orderId}")
	public String updateOrderStatus(@PathVariable("orderId") int orderId) {
		return ordersServiceImpl.orderStatus(orderId);
	}

	@PutMapping("/status1/{orderId}")
	public String updateOrderToDelivered(@PathVariable("orderId") int orderId) {
		return ordersServiceImpl.orderDelivered(orderId);
	}
}
