package br.com.desafio.domain.strategy.Impl;

import br.com.desafio.domain.model.PaymentStatusEnum;
import br.com.desafio.kafka.KafkaProducer;
import br.com.desafio.mock.PaymentItemModelMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class OverPaymentProcessorStrategyTest {

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private OverPaymentProcessorStrategy overPaymentProcessor;

    @Test
    void process() {
        ReflectionTestUtils.setField(overPaymentProcessor, "kafkaTopic", "kafka-topic-test");
        var paymentItemModel = PaymentItemModelMock.create();

        Mockito.doNothing().when(kafkaProducer).send(anyString(), anyString());

        var result = overPaymentProcessor.process(paymentItemModel);

        paymentItemModel.setPaymentStatus(PaymentStatusEnum.OVER_PAYMENT);
        assertEquals(paymentItemModel, result);
    }

    @Test
    void getStrategy() {
        assertEquals(1, overPaymentProcessor.getStrategy());
    }
}