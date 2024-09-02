package br.com.desafio.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice")
public class InvoiveEntity implements Serializable {

    @Id
    @NotNull
    @Column(name = "invoice_id")
    private String invoiceId;

    @Column(name = "seller_id")
    @NotNull
    private String sellerId;

    @Column(name = "amount")
    @NotNull
    private BigDecimal amount;

}
