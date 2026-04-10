package com.ecommerce.aura_store.repository;

import com.ecommerce.aura_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository < User, Long>{

    User findUsersByUserId(Long userId);
}
