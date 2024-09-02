package br.com.desafio.repository.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seller")
public class SellerEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6855626428226697507L;

    @Id
    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "name")
    @NotNull
    private String name;

    @OneToMany
    @JoinColumn(name = "seller_id")
    private List<InvoiveEntity> invoiveList;
}
