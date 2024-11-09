package com.example.librarymanager.repository;

import com.example.librarymanager.domain.dto.response.GetCartDetailResponseDto;
import com.example.librarymanager.domain.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    @Query("SELECT new com.example.librarymanager.domain.dto.response.GetCartDetailResponseDto(cd) " +
            "FROM CartDetail cd INNER JOIN Cart c ON cd.cart.id = c.id " +
            "WHERE c.id = :cartId")
    List<GetCartDetailResponseDto> getAllByCartId(@Param("cartId") Long cartId);

}
