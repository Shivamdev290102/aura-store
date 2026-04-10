package com.ecommerce.aura_store.repository;

import com.ecommerce.aura_store.entity.Address;
import com.ecommerce.aura_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
