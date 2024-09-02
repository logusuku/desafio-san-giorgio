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
class FullPaymentProcessorStrategyTest {

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private FullPaymentProcessorStrategy fullPaymentProcessor;

    @Test
    void process() {
        ReflectionTestUtils.setField(fullPaymentProcessor, "kafkaTopic", "kafka-topic-test");
        var paymentItemModel = PaymentItemModelMock.create();

        Mockito.doNothing().when(kafkaProducer).send(anyString(), anyString());

        var result = fullPaymentProcessor.process(paymentItemModel);

        paymentItemModel.setPaymentStatus(PaymentStatusEnum.FULL_PAYMENT);
        assertEquals(paymentItemModel, result);
    }

    @Test
    void getStrategy() {
        assertEquals(0, fullPaymentProcessor.getStrategy());
    }
}