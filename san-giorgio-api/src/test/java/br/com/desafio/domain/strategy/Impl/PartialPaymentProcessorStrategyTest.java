package br.com.desafio.domain.strategy.Impl;

import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.kafka.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class PartialPaymentProcessorStrategyTest {

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private PartialPaymentProcessorStrategy partialPaymentProcessor;

    @Test
    void process() {
        ReflectionTestUtils.setField(partialPaymentProcessor, "kafkaTopic", "kafka-topic-test");
        var paymentItemModel = PaymentItemModel.builder()
                .paymentId("1234")
                .paymentValue(BigDecimal.valueOf(1234))
                .build();

        Mockito.doNothing().when(kafkaProducer).send(anyString(), anyString());

        var result = partialPaymentProcessor.process(paymentItemModel);

        paymentItemModel.setPaymentStatus("PartialPayment");
        assertEquals(paymentItemModel, result);
    }

    @Test
    void getStrategy() {
        assertEquals(-1, partialPaymentProcessor.getStrategy());
    }
}