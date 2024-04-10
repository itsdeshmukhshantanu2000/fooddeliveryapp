package com.dto;

import java.time.LocalDateTime;

import com.entity.Status;

import lombok.Data;

@Data
public class OrdersDTO {

	private int orderId;
	private LocalDateTime date;
	private Status status;
	private CartDTO cart;
	private OrderedCartDTO orderedCartDTO;
	private String deliveryPartnerName;

}
