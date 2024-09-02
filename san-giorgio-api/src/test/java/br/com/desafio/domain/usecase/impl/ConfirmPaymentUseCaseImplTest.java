package br.com.desafio.domain.usecase.impl;

import br.com.desafio.domain.exception.InvoiceNotFoundException;
import br.com.desafio.domain.exception.SellerNotFoundException;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.strategy.Impl.FullPaymentProcessorStrategy;
import br.com.desafio.domain.strategy.Impl.OverPaymentProcessorStrategy;
import br.com.desafio.domain.strategy.Impl.PartialPaymentProcessorStrategy;
import br.com.desafio.domain.strategy.PaymentProcessorStrategy;
import br.com.desafio.domain.usecase.ConfirmPaymentUseCase;
import br.com.desafio.mock.PaymentItemModelMock;
import br.com.desafio.mock.PaymentModelMock;
import br.com.desafio.mock.SellerEntityMock;
import br.com.desafio.repository.SellerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ConfirmPaymentUseCaseImplTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private FullPaymentProcessorStrategy fullPaymentProcessor;

    @Mock
    private OverPaymentProcessorStrategy overPaymentProcessor;

    @Mock
    private PartialPaymentProcessorStrategy partialPaymentProcessor;

    private ConfirmPaymentUseCase confirmPaymentUseCase;

    @BeforeEach
    void setup() {
        List<PaymentProcessorStrategy> processorList = List.of(fullPaymentProcessor,
                overPaymentProcessor, partialPaymentProcessor);

        confirmPaymentUseCase = new ConfirmPaymentUseCaseImpl(sellerRepository, processorList);
    }

    @Test
    void confirmSellerNotFoundException() {
        Mockito.when(sellerRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        assertThrows(SellerNotFoundException.class,
                () -> confirmPaymentUseCase.confirm(PaymentModelMock.createComplete()));
    }

    @Test
    void confirmInvoiceNotFoundException() {
        Mockito.when(sellerRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(SellerEntityMock.create()));

        var payment = PaymentModelMock.createComplete();
        payment.getPaymentItems().get(0).setPaymentId("1010");

        assertThrows(InvoiceNotFoundException.class,
                () -> confirmPaymentUseCase.confirm(payment));
    }

    @Test
    void confirmSuccess() {
        Mockito.when(sellerRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(SellerEntityMock.create()));

        Mockito.when(fullPaymentProcessor.process(ArgumentMatchers.any()))
                .thenReturn(PaymentItemModelMock.createFull());
        Mockito.when(fullPaymentProcessor.getStrategy()).thenReturn(0);

        Mockito.when(overPaymentProcessor.process(ArgumentMatchers.any()))
                .thenReturn(PaymentItemModelMock.createOver());
        Mockito.when(overPaymentProcessor.getStrategy()).thenReturn(1);

        Mockito.when(partialPaymentProcessor.process(ArgumentMatchers.any()))
                .thenReturn(PaymentItemModelMock.createPartial());
        Mockito.when(partialPaymentProcessor.getStrategy()).thenReturn(-1);

        PaymentModel paymentModel = PaymentModelMock.createComplete();
        var result = confirmPaymentUseCase.confirm(paymentModel);

        Assertions.assertThat(result.getClientId()).isEqualTo(paymentModel.getClientId());

        Assertions.assertThat(result.getPaymentItems())
                .containsAll(List.of(PaymentItemModelMock.createFull(),
                        PaymentItemModelMock.createOver(),
                        PaymentItemModelMock.createPartial()));
    }

}