package br.com.desafio.mock;

import br.com.desafio.domain.model.PaymentModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

import static br.com.desafio.mock.PaymentMock.CLIENT_ID;
import static br.com.desafio.mock.SellerEntityMock.SELLER_ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentModelMock {

    public static PaymentModel create() {
        return PaymentModel.builder()
                .clientId(CLIENT_ID)
                .paymentItems(List.of(PaymentItemModelMock.create()))
                .build();
    }

    public static PaymentModel createComplete() {
        return PaymentModel.builder()
                .clientId(SELLER_ID)
                .paymentItems(List.of(PaymentItemModelMock.createPartial(),
                        PaymentItemModelMock.createFull(),
                        PaymentItemModelMock.createOver()))
                .build();
    }

}
