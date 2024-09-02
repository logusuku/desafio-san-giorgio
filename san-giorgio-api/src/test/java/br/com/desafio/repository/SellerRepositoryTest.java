package br.com.desafio.repository;

import br.com.desafio.repository.entity.InvoiveEntity;
import br.com.desafio.repository.entity.SellerEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SellerRepositoryTest {
/*
    @Autowired
    private SellerRepository sellerRepository;

    private SellerEntity seller;

    @BeforeEach
    public void setUp() {
        seller = SellerEntity.builder()
                .sellerId("123456")
                .invoiveList(List.of(InvoiveEntity.builder()
                        .sellerId("12345")
                        .invoiceId("98765")
                        .amount(new BigDecimal(100))
                        .build()))
                .build();
        sellerRepository.save(seller);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        sellerRepository.delete(seller);
    }

    @Test
    void saveAndFind() {
        var seller = SellerEntity.builder()
                .sellerId("123456")
                .invoiveList(List.of(InvoiveEntity.builder()
                                .sellerId("12345")
                                .invoiceId("98765")
                                .amount(new BigDecimal(100))
                        .build()))
                .build();

        entityManager.persist(seller);
        entityManager.flush();

        var entity = sellerRepository.findById(seller.getSellerId());

        Assertions.assertThat(entity).isEqualTo(seller);
    }
*/
}