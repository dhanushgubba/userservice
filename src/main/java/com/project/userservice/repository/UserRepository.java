package com.project.userservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.userservice.model.Role;
import com.project.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Long >{
	User findByEmail(String email);
	
	List<User> findByRole(Role role);
	
	boolean existsByEmail(String email);
	
	boolean existsByPhone(Long phone);
}
