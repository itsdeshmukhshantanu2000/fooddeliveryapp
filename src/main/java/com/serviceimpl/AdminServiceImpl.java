package com.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.UserDTO;
import com.entity.AdminEntity;
import com.exception.AdminNotFoundException;
import com.repository.AdminRepository;
import com.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public AdminEntity addAdmin(UserDTO userDTO) {
		AdminEntity admin = new AdminEntity();
		admin.setUsername(userDTO.getUsername());
		admin.setAddress(userDTO.getAddress());
		admin.setEmail(userDTO.getEmail());
		admin.setNumber(userDTO.getNumber());
		admin.setPassword(userDTO.getPassword());
		

		adminRepository.save(admin);
		return admin;
	}

	// Update User
	@Override
	public String updateAdmin(int id, UserDTO adminData) {

		try {
			AdminEntity admin = adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException());
			if (admin.getUsername() != null)
				admin.setUsername(adminData.getUsername());
			if (admin.getNumber() != 0)
				admin.setNumber(adminData.getNumber());
			if (admin.getAddress() != null)
				admin.setAddress(adminData.getAddress());
			if (admin.getEmail() != null)
				admin.setEmail(adminData.getEmail());
			adminRepository.save(admin);

		} catch (AdminNotFoundException e) {

			System.out.println(e);
			return "Admin data not updated";
		}
		return "Admin Updated Successfully";
	}

	// Delete user
	@Override
	public String deleteAdmin(int id) {
		try {
			AdminEntity admin= adminRepository.findById(id).orElseThrow(()->new AdminNotFoundException());
			adminRepository.delete(admin);
		}catch(AdminNotFoundException e){
			System.out.println(e);
			return "Invalid admin ID...";
		}

		return "Admin deleted successfully!!";
		
	}

	@Override
	public UserDTO getAdminByEmail(String email) {
		AdminEntity admin = adminRepository.findByEmail(email);
		if(admin==null) {
			throw new AdminNotFoundException("Admin not found for given email....");
		}
		UserDTO userDTO = new UserDTO();

		userDTO.setAddress(admin.getAddress());
		userDTO.setEmail(admin.getEmail());
		userDTO.setNumber(admin.getNumber());
		userDTO.setUsername(admin.getUsername());
		userDTO.setId(admin.getId());

		return userDTO;
	}

	// Read all Users
	@Override
	public List<UserDTO> readAllAdmins() {
		List<AdminEntity> admins = adminRepository.findAll();

		System.out.println(admins);
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		for (AdminEntity admin : admins) {
			UserDTO userDTO = new UserDTO();
			userDTO.setUsername(admin.getUsername());
			userDTO.setAddress(admin.getAddress());
			userDTO.setEmail(admin.getEmail());
			userDTO.setPassword(admin.getPassword());
			userDTO.setNumber(admin.getNumber());
			userDTO.setId(admin.getId());
			userDTOs.add(userDTO);

		}

		return userDTOs;
	}

}
