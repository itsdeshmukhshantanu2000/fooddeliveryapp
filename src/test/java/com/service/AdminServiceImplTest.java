package com.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
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
import com.entity.AdminEntity;
import com.repository.AdminRepository;
import com.serviceimpl.AdminServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    public void testAddAdmin() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setAddress("Test Address");
        userDTO.setNumber(1234567890);
        userDTO.setPassword("testPassword");

        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setUsername("testUser");
        adminEntity.setEmail("test@example.com");
        adminEntity.setAddress("Test Address");
        adminEntity.setNumber(1234567890);
        adminEntity.setPassword("testPassword");

        when(adminRepository.save(any(AdminEntity.class))).thenReturn(adminEntity);

        AdminEntity savedAdmin = adminService.addAdmin(userDTO);

        assertNotNull(savedAdmin);
        assertEquals("testUser", savedAdmin.getUsername());
        assertEquals("test@example.com", savedAdmin.getEmail());
        assertEquals("Test Address", savedAdmin.getAddress());
        assertEquals(1234567890, savedAdmin.getNumber());
        assertEquals("testPassword", savedAdmin.getPassword());

        verify(adminRepository).save(any(AdminEntity.class));
    }

    @Test
    public void testUpdateAdmin() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUser");
        userDTO.setEmail("updated@example.com");
        userDTO.setAddress("Updated Address");
        userDTO.setNumber(987654321);

        AdminEntity existingAdmin = new AdminEntity();
        existingAdmin.setId(1);
        existingAdmin.setUsername("oldUser");
        existingAdmin.setEmail("old@example.com");
        existingAdmin.setAddress("Old Address");
        existingAdmin.setNumber(1234567890);
        existingAdmin.setPassword("oldPassword");

        when(adminRepository.findById(1)).thenReturn(Optional.of(existingAdmin));
        when(adminRepository.save(any(AdminEntity.class))).thenReturn(existingAdmin);

        String result = adminService.updateAdmin(1, userDTO);

        assertEquals("Admin Updated Successfully", result);
        assertEquals("updatedUser", existingAdmin.getUsername());
        assertEquals("updated@example.com", existingAdmin.getEmail());
        assertEquals("Updated Address", existingAdmin.getAddress());
        assertEquals(987654321, existingAdmin.getNumber());

        verify(adminRepository).findById(1);
        verify(adminRepository).save(any(AdminEntity.class));
    }

    @Test
    public void testDeleteAdmin() {
        AdminEntity existingAdmin = new AdminEntity();
        existingAdmin.setId(1);

        when(adminRepository.findById(1)).thenReturn(Optional.of(existingAdmin));
        doNothing().when(adminRepository).delete(existingAdmin);

        String result = adminService.deleteAdmin(1);

        assertEquals("Admin deleted successfully!!", result);

        verify(adminRepository).findById(1);
        verify(adminRepository).delete(existingAdmin);
    }

    @Test
    public void testGetAdminByEmail() {
        AdminEntity existingAdmin = new AdminEntity();
        existingAdmin.setId(1);
        existingAdmin.setUsername("testUser");
        existingAdmin.setEmail("test@example.com");
        existingAdmin.setAddress("Test Address");
        existingAdmin.setNumber(1234567890);

        when(adminRepository.findByEmail("test@example.com")).thenReturn(existingAdmin);

        UserDTO userDTO = adminService.getAdminByEmail("test@example.com");

        assertNotNull(userDTO);
        assertEquals("testUser", userDTO.getUsername());
        assertEquals("test@example.com", userDTO.getEmail());
        assertEquals("Test Address", userDTO.getAddress());
        assertEquals(1234567890, userDTO.getNumber());

        verify(adminRepository).findByEmail("test@example.com");
    }

    @Test
    public void testReadAllAdmins() {
        List<AdminEntity> admins = new ArrayList<>();
        AdminEntity admin1 = new AdminEntity();
        admin1.setId(1);
        admin1.setUsername("user1");
        admin1.setEmail("user1@example.com");
        admin1.setAddress("Address 1");
        admin1.setNumber(1234567890);
        admins.add(admin1);

        AdminEntity admin2 = new AdminEntity();
        admin2.setId(2);
        admin2.setUsername("user2");
        admin2.setEmail("user2@example.com");
        admin2.setAddress("Address 2");
        admin2.setNumber(987654321);
        admins.add(admin2);

        when(adminRepository.findAll()).thenReturn(admins);

        List<UserDTO> userDTOs = adminService.readAllAdmins();

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

        verify(adminRepository).findAll();
    }
}

