package br.com.desafio.mock;

import br.com.desafio.controller.PaymentItem;
import br.com.desafio.domain.model.PaymentStatusEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static br.com.desafio.mock.PaymentMock.PAYMENT_ID;
import static br.com.desafio.mock.PaymentMock.PAYMENT_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentItemMock {

    public static PaymentItem create() {
        return PaymentItem.builder()
                        .paymentId(PAYMENT_ID)
                        .paymentValue(PAYMENT_VALUE)
                        .paymentStatus(PaymentStatusEnum.FULL_PAYMENT.name())
                        .build();
    }

}
