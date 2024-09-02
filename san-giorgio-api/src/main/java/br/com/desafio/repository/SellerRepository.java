package br.com.desafio.repository;

import br.com.desafio.repository.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<SellerEntity, String> {
}
