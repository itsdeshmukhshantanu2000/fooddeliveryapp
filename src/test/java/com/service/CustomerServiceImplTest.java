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

import com.dto.UserDTO;
import com.entity.CustomerEntity;
import com.repository.CartRepository;
import com.repository.CustomerRepository;
import com.serviceimpl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void testRegisterCustomer() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setAddress("Test Address");
        userDTO.setNumber(1234567890);
        userDTO.setPassword("testPassword");

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUsername("testUser");
        customerEntity.setEmail("test@example.com");
        customerEntity.setAddress("Test Address");
        customerEntity.setNumber(1234567890);
        customerEntity.setPassword("testPassword");

        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);

        CustomerEntity savedCustomer = customerService.registerCustomer(userDTO);

        assertNotNull(savedCustomer);
        assertEquals("testUser", savedCustomer.getUsername());
        assertEquals("test@example.com", savedCustomer.getEmail());
        assertEquals("Test Address", savedCustomer.getAddress());
        assertEquals(1234567890, savedCustomer.getNumber());
        assertEquals("testPassword", savedCustomer.getPassword());

    }

    @Test
    public void testReadAllCustomers() {
        List<CustomerEntity> customers = new ArrayList<>();
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setId(1);
        customer1.setUsername("user1");
        customer1.setEmail("user1@example.com");
        customer1.setAddress("Address 1");
        customer1.setNumber(1234567890);
        customers.add(customer1);

        CustomerEntity customer2 = new CustomerEntity();
        customer2.setId(2);
        customer2.setUsername("user2");
        customer2.setEmail("user2@example.com");
        customer2.setAddress("Address 2");
        customer2.setNumber(987654321);
        customers.add(customer2);

        when(customerRepository.findAll()).thenReturn(customers);

        List<UserDTO> userDTOs = customerService.readAllCustomers();

        assertNotNull(userDTOs);
        assertEquals(2, userDTOs.size());

        UserDTO userDTO1 = userDTOs.get(0);
        assertEquals("user1", userDTO1.getUsername());
        assertEquals("user1@example.com", userDTO1.getEmail());
        assertEquals("Address 1", userDTO1.getAddress());
        assertEquals(1234567890, userDTO1.getNumber());

        UserDTO userDTO2 = userDTOs.get(1);
        assertEquals("user2", userDTO2.getUsername());
        assertEquals("user2@example.com", userDTO2.getEmail());
        assertEquals("Address 2", userDTO2.getAddress());
        assertEquals(987654321, userDTO2.getNumber());
    }

    @Test
    public void testUpdateCustomer() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUser");
        userDTO.setEmail("updated@example.com");
        userDTO.setAddress("Updated Address");
        userDTO.setNumber(987654321);
        userDTO.setPassword("updatedPassword");

        CustomerEntity existingCustomer = new CustomerEntity();
        existingCustomer.setId(1);
        existingCustomer.setUsername("oldUser");
        existingCustomer.setEmail("old@example.com");
        existingCustomer.setAddress("Old Address");
        existingCustomer.setNumber(1234567890);
        existingCustomer.setPassword("oldPassword");

        when(customerRepository.findById(1)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(existingCustomer);

        String result = customerService.updateCustomer(1, userDTO);

        assertEquals("Customer Updated Successfully", result);
        assertEquals("updatedUser", existingCustomer.getUsername());
        assertEquals("updated@example.com", existingCustomer.getEmail());
        assertEquals("Updated Address", existingCustomer.getAddress());
        assertEquals(987654321, existingCustomer.getNumber());
        assertEquals("updatedPassword", existingCustomer.getPassword());
    }

    @Test
    public void testDeleteCustomer() {
        CustomerEntity existingCustomer = new CustomerEntity();
        existingCustomer.setId(1);

        when(customerRepository.findById(1)).thenReturn(Optional.of(existingCustomer));
        doNothing().when(customerRepository).deleteById(1);

        String result = customerService.deleteCustomer(1);

        assertEquals("Customer deleted successfully.", result);
    }


}
