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
class PartialPaymentProcessorStrategyTest {

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private PartialPaymentProcessorStrategy partialPaymentProcessor;

    @Test
    void process() {
        ReflectionTestUtils.setField(partialPaymentProcessor, "kafkaTopic", "kafka-topic-test");
        var paymentItemModel = PaymentItemModelMock.create();

        Mockito.doNothing().when(kafkaProducer).send(anyString(), anyString());

        var result = partialPaymentProcessor.process(paymentItemModel);

        paymentItemModel.setPaymentStatus(PaymentStatusEnum.PARTIAL_PAYMENT);
        assertEquals(paymentItemModel, result);
    }

    @Test
    void getStrategy() {
        assertEquals(-1, partialPaymentProcessor.getStrategy());
    }
}