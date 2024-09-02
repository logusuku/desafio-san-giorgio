package br.com.desafio.controller;


import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.usecase.ConfirmPaymentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final ConfirmPaymentUseCase confirmPaymentUseCase;

    @PutMapping(path = "/api/payment")
    public ResponseEntity<Payment> setPayment(@RequestBody @Valid Payment request) {
        PaymentModel paymentModel = PaymentModel.builder()
                .clientId(request.getClientId())
                .paymentItems(request.getPaymentItems().stream()
                        .map(item -> PaymentItemModel.builder()
                                .paymentId(item.getPaymentId())
                                .paymentValue(item.getPaymentValue())
                                .paymentStatus(item.getPaymentStatus()).build())
                        .toList())
                .build();

        var payment = confirmPaymentUseCase.confirm(paymentModel);
        return ResponseEntity.status(HttpStatus.OK).body(Payment.builder()
                .clientId(payment.getClientId())
                .paymentItems(payment.getPaymentItems().stream()
                        .map(item -> PaymentItem.builder()
                                .paymentId(item.getPaymentId())
                                .paymentValue(item.getPaymentValue())
                                .paymentStatus(item.getPaymentStatus())
                                .build())
                        .toList() )
                .build());
    }
}
