package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByReaderCardNumber(String cardNumber);

}
