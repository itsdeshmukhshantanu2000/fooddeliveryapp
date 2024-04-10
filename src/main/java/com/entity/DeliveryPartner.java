package com.entity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "deliverypartnerdb")
public class DeliveryPartner {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String partnerName;
	private int partnerAge;
	private String gender;
	private long contactNumber;
	@Enumerated(EnumType.STRING)
	private PartnerStatus status;
}