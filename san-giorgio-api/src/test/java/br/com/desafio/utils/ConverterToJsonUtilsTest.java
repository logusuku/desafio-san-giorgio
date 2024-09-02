package br.com.desafio.utils;

import br.com.desafio.mock.PaymentItemModelMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterToJsonUtilsTest {

    @Test
    void testToString() {
        var expected = "{\"paymentId\":\"789\",\"paymentValue\":100,\"paymentStatus\":\"FULL_PAYMENT\"}";
        var paymentItemModel = PaymentItemModelMock.create() ;

        var result = ConverterToJsonUtils.toString(paymentItemModel);

        assertEquals(result, expected);
    }

}