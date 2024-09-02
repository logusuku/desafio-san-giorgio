package br.com.desafio.domain.usecase.impl;

import br.com.desafio.domain.exception.InvoiceNotFoundException;
import br.com.desafio.domain.exception.SellerNotFoundException;
import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.strategy.Impl.FullPaymentProcessorStrategy;
import br.com.desafio.domain.strategy.Impl.OverPaymentProcessorStrategy;
import br.com.desafio.domain.strategy.Impl.PartialPaymentProcessorStrategy;
import br.com.desafio.domain.strategy.PaymentProcessorStrategy;
import br.com.desafio.domain.usecase.ConfirmPaymentUseCase;
import br.com.desafio.repository.SellerRepository;
import br.com.desafio.repository.entity.InvoiveEntity;
import br.com.desafio.repository.entity.SellerEntity;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfirmPaymentUseCaseImplTest {

    public static final String SELLER_ID = "123456";
    public static final String INVOICE_ID_FULL = "999";
    public static final String INVOICE_ID_OVER = "888";
    public static final String INVOICE_ID_PARTIAL = "777";

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
                () -> confirmPaymentUseCase.confirm(getPaymentModel()));
    }

    @Test
    void confirmInvoiceNotFoundException() {
        Mockito.when(sellerRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(getSellerEntity()));

        var payment = getPaymentModel();
        payment.getPaymentItems().get(0).setPaymentId("1010");

        assertThrows(InvoiceNotFoundException.class,
                () -> confirmPaymentUseCase.confirm(payment));
    }

    @Test
    void confirmSuccess() {
        Mockito.when(sellerRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(getSellerEntity()));

        Mockito.when(fullPaymentProcessor.process(ArgumentMatchers.any()))
                .thenReturn(getFullPayment());
        Mockito.when(fullPaymentProcessor.getStrategy()).thenReturn(0);

        Mockito.when(overPaymentProcessor.process(ArgumentMatchers.any()))
                .thenReturn(getOverPayment());
        Mockito.when(overPaymentProcessor.getStrategy()).thenReturn(1);

        Mockito.when(partialPaymentProcessor.process(ArgumentMatchers.any()))
                .thenReturn(getPartialPayment());
        Mockito.when(partialPaymentProcessor.getStrategy()).thenReturn(-1);

        PaymentModel paymentModel = getPaymentModel();
        var result = confirmPaymentUseCase.confirm(paymentModel);

        Assertions.assertThat(result.getClientId()).isEqualTo(paymentModel.getClientId());
        Assertions.assertThat(result.getPaymentItems())
                .isEqualTo(List.of(getFullPayment(), getOverPayment(), getPartialPayment()));
    }


    private SellerEntity getSellerEntity() {
        return SellerEntity.builder()
                .sellerId(SELLER_ID)
                .invoiveList(List.of(
                        InvoiveEntity.builder()
                            .sellerId(SELLER_ID)
                            .invoiceId(INVOICE_ID_FULL)
                            .amount(BigDecimal.valueOf(100.0))
                            .build(),
                        InvoiveEntity.builder()
                                .sellerId(SELLER_ID)
                                .invoiceId(INVOICE_ID_OVER)
                                .amount(BigDecimal.valueOf(100.0))
                                .build(),
                        InvoiveEntity.builder()
                                .sellerId(SELLER_ID)
                                .invoiceId(INVOICE_ID_PARTIAL)
                                .amount(BigDecimal.valueOf(100.0))
                                .build()
                        ))
                .build();
    }



    private PaymentModel getPaymentModel() {
        return PaymentModel.builder()
                .clientId(SELLER_ID)
                .paymentItems(List.of(getFullPayment(), getOverPayment(), getPartialPayment()))
                .build();
    }

    private PaymentItemModel getOverPayment() {
        return PaymentItemModel.builder()
                .paymentId(INVOICE_ID_OVER)
                .paymentValue(BigDecimal.valueOf(150.0))
                .paymentStatus("OverPayment")
                .build();
    }

    private PaymentItemModel getFullPayment() {
        return PaymentItemModel.builder()
                .paymentId(INVOICE_ID_FULL)
                .paymentValue(BigDecimal.valueOf(100.0))
                .paymentStatus("FullPayment")
                .build();
    }

    private PaymentItemModel getPartialPayment() {
        return PaymentItemModel.builder()
                .paymentId(INVOICE_ID_PARTIAL)
                .paymentValue(BigDecimal.valueOf(50.0))
                .paymentStatus("PartialPayment")
                .build();
    }

}