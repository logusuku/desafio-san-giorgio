package br.com.desafio.domain.strategy.Impl;

import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.model.PaymentStatusEnum;
import br.com.desafio.domain.strategy.PaymentProcessorStrategy;
import br.com.desafio.kafka.KafkaProducer;
import br.com.desafio.utils.ConverterToJsonUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class PartialPaymentProcessorStrategy implements PaymentProcessorStrategy {

    private final KafkaProducer kafkaProducer;

    @Value("${kafka-topics.partial-payment}")
    private String kafkaTopic;

    @Override
    public PaymentItemModel process(PaymentItemModel paymentItemModel) {
        paymentItemModel.setPaymentStatus(PaymentStatusEnum.PARTIAL_PAYMENT);
        kafkaProducer.send(kafkaTopic, ConverterToJsonUtils.toString(paymentItemModel));
        return paymentItemModel;
    }

    @Override
    public Integer getStrategy() {
        return -1;
    }
}
