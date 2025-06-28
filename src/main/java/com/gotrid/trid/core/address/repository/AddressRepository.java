package com.gotrid.trid.core.address.repository;

import com.gotrid.trid.core.address.model.Address;
import com.gotrid.trid.core.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByUser_Id(Integer userId);

    Optional<Address> findByUser(Users user);
}
