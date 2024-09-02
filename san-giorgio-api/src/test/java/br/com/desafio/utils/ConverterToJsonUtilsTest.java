package br.com.desafio.utils;

import br.com.desafio.domain.model.PaymentItemModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ConverterToJsonUtilsTest {

    @Test
    void testToString() {
        var expected = "{\"paymentId\":\"1234\",\"paymentValue\":1234,\"paymentStatus\":null}";
        var paymentItemModel = PaymentItemModel.builder()
                .paymentId("1234")
                .paymentValue(BigDecimal.valueOf(1234))
                .build();

        var result = ConverterToJsonUtils.toString(paymentItemModel);

        assertEquals(result, expected);
    }

}