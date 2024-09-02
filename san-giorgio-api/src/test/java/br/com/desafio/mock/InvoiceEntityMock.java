package br.com.desafio.mock;

import br.com.desafio.repository.entity.InvoiceEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static br.com.desafio.mock.SellerEntityMock.SELLER_ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvoiceEntityMock {

    public static final String INVOICE_ID_FULL = "999";
    public static final String INVOICE_ID_OVER = "888";
    public static final String INVOICE_ID_PARTIAL = "777";
    public static final BigDecimal AMOUNT_100 = BigDecimal.valueOf(100.0);

    public static InvoiceEntity createFull() {
        return InvoiceEntity.builder()
                .sellerId(SELLER_ID)
                .invoiceId(INVOICE_ID_FULL)
                .amount(AMOUNT_100)
                .build();
    }

    public static InvoiceEntity createOver() {
        return InvoiceEntity.builder()
                .sellerId(SELLER_ID)
                .invoiceId(INVOICE_ID_OVER)
                .amount(AMOUNT_100)
                .build();
    }

    public static InvoiceEntity createPartial() {
        return InvoiceEntity.builder()
                .sellerId(SELLER_ID)
                .invoiceId(INVOICE_ID_PARTIAL)
                .amount(AMOUNT_100)
                .build();
    }
}
