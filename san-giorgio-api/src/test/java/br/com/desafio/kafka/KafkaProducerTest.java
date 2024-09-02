package br.com.desafio.kafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaProducer producer;

    private String topic = "topic-test";

    @Test
    void send() throws Exception {
        String data = "KafkaProducer";

        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        Mockito.when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);

        Assertions.assertDoesNotThrow(() -> producer.send(topic, data));
    }

}