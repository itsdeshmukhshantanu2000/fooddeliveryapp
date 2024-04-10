package com.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dto.DeliveryPartnerDTO;
import com.entity.DeliveryPartner;
import com.entity.PartnerStatus;
import com.exception.DeliveryPartnerNotFoundException;
import com.repository.DeliveryPartnerRepository;
import com.serviceimpl.DeliveryPartnerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DeliveryPartnerServiceImplTest {

    @Mock
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @InjectMocks
    private DeliveryPartnerServiceImpl deliveryPartnerService;

    @Test
    public void testAddDeliveryPartner() {
        DeliveryPartner deliveryPartner = new DeliveryPartner();
        deliveryPartner.setId(1);
        deliveryPartner.setPartnerName("John Doe");
        deliveryPartner.setPartnerAge(30);
        deliveryPartner.setGender("Male");
        deliveryPartner.setContactNumber(1234567890L);
        deliveryPartner.setStatus(PartnerStatus.Not_Assigned);

        when(deliveryPartnerRepository.save(any(DeliveryPartner.class))).thenReturn(deliveryPartner);

        DeliveryPartnerDTO addedDeliveryPartner = deliveryPartnerService.addDeliveryPartner(deliveryPartner);

        assertNotNull(addedDeliveryPartner);
        assertEquals(1, addedDeliveryPartner.getId());
        assertEquals("John Doe", addedDeliveryPartner.getPartnerName());
        assertEquals(30, addedDeliveryPartner.getPartnerAge());
        assertEquals("Male", addedDeliveryPartner.getGender());
        assertEquals(1234567890L, addedDeliveryPartner.getContactNumber());
        assertEquals(PartnerStatus.Not_Assigned, addedDeliveryPartner.getStatus());
    }

    @Test
    public void testDeleteDeliveryPartner() {
        doNothing().when(deliveryPartnerRepository).deleteById(1);

        String result = deliveryPartnerService.deleteDeliveryPartner(1);

        assertEquals("Delivery partner deleted successfully.", result);
    }

    @Test
    public void testUpdateDeliveryPartner() throws DeliveryPartnerNotFoundException {
        DeliveryPartner existingDeliveryPartner = new DeliveryPartner();
        existingDeliveryPartner.setId(1);
        existingDeliveryPartner.setPartnerName("Old Name");
        existingDeliveryPartner.setPartnerAge(25);
        existingDeliveryPartner.setGender("Female");
        existingDeliveryPartner.setContactNumber(9876543210L);
        existingDeliveryPartner.setStatus(PartnerStatus.Not_Assigned);

        DeliveryPartner updatedDeliveryPartner = new DeliveryPartner();
        updatedDeliveryPartner.setId(1);
        updatedDeliveryPartner.setPartnerName("New Name");
        updatedDeliveryPartner.setPartnerAge(30);
        updatedDeliveryPartner.setGender("Male");
        updatedDeliveryPartner.setContactNumber(1234567890L);
        updatedDeliveryPartner.setStatus(PartnerStatus.Not_Assigned);

        when(deliveryPartnerRepository.findById(1)).thenReturn(Optional.of(existingDeliveryPartner));
        when(deliveryPartnerRepository.save(any(DeliveryPartner.class))).thenReturn(updatedDeliveryPartner);

        String result = deliveryPartnerService.updateDeliveryPartner(1, updatedDeliveryPartner);

        assertEquals("Delivery partner details updated.", result);
        assertEquals("New Name", existingDeliveryPartner.getPartnerName());
        assertEquals(30, existingDeliveryPartner.getPartnerAge());
        assertEquals("Male", existingDeliveryPartner.getGender());
        assertEquals(1234567890L, existingDeliveryPartner.getContactNumber());
        assertEquals(PartnerStatus.Not_Assigned, existingDeliveryPartner.getStatus());
    }

    @Test
    public void testGetDeliveryPartnerById() throws DeliveryPartnerNotFoundException {
        DeliveryPartner deliveryPartner = new DeliveryPartner();
        deliveryPartner.setId(1);
        deliveryPartner.setPartnerName("John Doe");
        deliveryPartner.setPartnerAge(30);
        deliveryPartner.setGender("Male");
        deliveryPartner.setContactNumber(1234567890L);
        deliveryPartner.setStatus(PartnerStatus.Not_Assigned);

        when(deliveryPartnerRepository.findById(1)).thenReturn(Optional.of(deliveryPartner));

        DeliveryPartnerDTO foundDeliveryPartner = deliveryPartnerService.getDeliveryPartnerById(1);

        assertNotNull(foundDeliveryPartner);
        assertEquals(1, foundDeliveryPartner.getId());
        assertEquals("John Doe", foundDeliveryPartner.getPartnerName());
        assertEquals(30, foundDeliveryPartner.getPartnerAge());
        assertEquals("Male", foundDeliveryPartner.getGender());
        assertEquals(1234567890L, foundDeliveryPartner.getContactNumber());
        assertEquals(PartnerStatus.Not_Assigned, foundDeliveryPartner.getStatus());
    }

    @Test
    public void testGetAllDeliveryPartners() {
        DeliveryPartner deliveryPartner1 = new DeliveryPartner();
        deliveryPartner1.setId(1);
        deliveryPartner1.setPartnerName("John Doe");
        deliveryPartner1.setPartnerAge(30);
        deliveryPartner1.setGender("Male");
        deliveryPartner1.setContactNumber(1234567890L);
        deliveryPartner1.setStatus(PartnerStatus.Not_Assigned);

        DeliveryPartner deliveryPartner2 = new DeliveryPartner();
        deliveryPartner2.setId(2);
        deliveryPartner2.setPartnerName("Jane Doe");
        deliveryPartner2.setPartnerAge(25);
        deliveryPartner2.setGender("Female");
        deliveryPartner2.setContactNumber(9876543210L);
        deliveryPartner2.setStatus(PartnerStatus.Not_Assigned);

        List<DeliveryPartner> deliveryPartners = new ArrayList<>();
        deliveryPartners.add(deliveryPartner1);
        deliveryPartners.add(deliveryPartner2);

        when(deliveryPartnerRepository.findAll()).thenReturn(deliveryPartners);

        List<DeliveryPartnerDTO> foundDeliveryPartners = deliveryPartnerService.getAllDeliveryPartners();

        assertNotNull(foundDeliveryPartners);
        assertEquals(2, foundDeliveryPartners.size());

        DeliveryPartnerDTO foundDeliveryPartner1 = foundDeliveryPartners.get(0);
        assertEquals(1, foundDeliveryPartner1.getId());
        assertEquals("John Doe", foundDeliveryPartner1.getPartnerName());
        assertEquals(30, foundDeliveryPartner1.getPartnerAge());
        assertEquals("Male", foundDeliveryPartner1.getGender());
        assertEquals(1234567890L, foundDeliveryPartner1.getContactNumber());
        assertEquals(PartnerStatus.Not_Assigned, foundDeliveryPartner1.getStatus());

        DeliveryPartnerDTO foundDeliveryPartner2 = foundDeliveryPartners.get(1);
        assertEquals(2, foundDeliveryPartner2.getId());
        assertEquals("Jane Doe", foundDeliveryPartner2.getPartnerName());
        assertEquals(25, foundDeliveryPartner2.getPartnerAge());
        assertEquals("Female", foundDeliveryPartner2.getGender());
        assertEquals(9876543210L, foundDeliveryPartner2.getContactNumber());
        assertEquals(PartnerStatus.Not_Assigned, foundDeliveryPartner2.getStatus());
    }

    @Test
    public void testAssignDeliveryPartner() {
        DeliveryPartner deliveryPartner1 = new DeliveryPartner();
        deliveryPartner1.setId(1);
        deliveryPartner1.setStatus(PartnerStatus.Not_Assigned);

        DeliveryPartner deliveryPartner2 = new DeliveryPartner();
        deliveryPartner2.setId(2);
        deliveryPartner2.setStatus(PartnerStatus.Assigned);

        List<DeliveryPartner> deliveryPartners = new ArrayList<>();
        deliveryPartners.add(deliveryPartner1);
        deliveryPartners.add(deliveryPartner2);

        when(deliveryPartnerRepository.findAll()).thenReturn(deliveryPartners);

        int assignedId = deliveryPartnerService.assignDeliveryPartner();

        assertEquals(1, assignedId);
    }
}
