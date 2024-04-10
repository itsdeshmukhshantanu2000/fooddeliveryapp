package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entity.DeliveryPartner;


@Repository
public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Integer>{
	public DeliveryPartner findByPartnerName(String partnerName);

}
