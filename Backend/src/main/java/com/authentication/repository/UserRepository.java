package com.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.entity.User;

public interface UserRepository  extends JpaRepository<User, Long> {
	

}
