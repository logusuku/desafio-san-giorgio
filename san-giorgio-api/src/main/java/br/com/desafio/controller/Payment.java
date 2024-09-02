package br.com.desafio.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @NotEmpty
    @JsonProperty("client_id")
    private String clientId;
    @NotEmpty
    @JsonProperty("payment_items")
    private List<PaymentItem> paymentItems;
}
