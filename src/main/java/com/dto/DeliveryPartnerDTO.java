package com.dto;

import com.entity.PartnerStatus;

import lombok.Data;

@Data
public class DeliveryPartnerDTO {
	private int id;
	private String partnerName;
	private int partnerAge;
	private String gender;
	private long contactNumber;
	private PartnerStatus status;
}
