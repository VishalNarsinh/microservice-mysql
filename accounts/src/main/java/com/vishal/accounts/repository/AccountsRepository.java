package com.vishal.accounts.repository;

import com.vishal.accounts.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Accounts,Long> {
    Optional<Accounts> findByCustomerId(Long customerId);

    List<Accounts> findByCustomerIdIn(List<Long> customerIds);

    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);
}
