package br.com.desafio.mock;

import br.com.desafio.controller.Payment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentMock {

    public static final String PAYMENT_ID = "789";
    public static final BigDecimal PAYMENT_VALUE = BigDecimal.valueOf(100);
    public static final String CLIENT_ID = "123";

    public static Payment create() {
        return Payment.builder()
                .clientId(CLIENT_ID)
                .paymentItems(List.of(PaymentItemMock.create()))
                .build();
    }

}
