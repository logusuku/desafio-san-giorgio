package br.com.desafio.mock;

import br.com.desafio.repository.entity.SellerEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SellerEntityMock {

    public static final String SELLER_ID = "123456";

    public static SellerEntity create() {
        return SellerEntity.builder()
                .sellerId(SELLER_ID)
                .invoiceList(List.of(InvoiceEntityMock.createFull(),
                                InvoiceEntityMock.createOver(),
                                InvoiceEntityMock.createPartial()))
                .build();
    }
}
