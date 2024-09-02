package br.com.desafio.domain.strategy;

import br.com.desafio.domain.model.PaymentItemModel;

public interface PaymentProcessorStrategy {

    PaymentItemModel process(PaymentItemModel paymentItemModel);

    Integer getStrategy();
}
