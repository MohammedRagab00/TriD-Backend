package com.gotrid.trid.core.address.repository;

import com.gotrid.trid.core.address.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByUser_Id(Integer userId);
}
