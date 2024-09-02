package br.com.desafio.mock;

import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.model.PaymentStatusEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static br.com.desafio.mock.InvoiceEntityMock.*;
import static br.com.desafio.mock.PaymentMock.PAYMENT_ID;
import static br.com.desafio.mock.PaymentMock.PAYMENT_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentItemModelMock {

    public static PaymentItemModel create() {
        return PaymentItemModel.builder()
                        .paymentId(PAYMENT_ID)
                        .paymentValue(PAYMENT_VALUE)
                        .paymentStatus(PaymentStatusEnum.FULL_PAYMENT)
                        .build();
    }

    public static PaymentItemModel createOver() {
        return PaymentItemModel.builder()
                .paymentId(INVOICE_ID_OVER)
                .paymentValue(BigDecimal.valueOf(150.0))
                .paymentStatus(PaymentStatusEnum.OVER_PAYMENT)
                .build();
    }

    public static PaymentItemModel createFull() {
        return PaymentItemModel.builder()
                .paymentId(INVOICE_ID_FULL)
                .paymentValue(BigDecimal.valueOf(100.0))
                .paymentStatus(PaymentStatusEnum.FULL_PAYMENT)
                .build();
    }

    public static PaymentItemModel createPartial() {
        return PaymentItemModel.builder()
                .paymentId(INVOICE_ID_PARTIAL)
                .paymentValue(BigDecimal.valueOf(50.0))
                .paymentStatus(PaymentStatusEnum.PARTIAL_PAYMENT)
                .build();
    }

}
