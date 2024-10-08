package br.com.desafio.domain.usecase.impl;

import br.com.desafio.domain.exception.InvoiceNotFoundException;
import br.com.desafio.domain.exception.SellerNotFoundException;
import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.strategy.PaymentProcessorStrategy;
import br.com.desafio.domain.usecase.ConfirmPaymentUseCase;
import br.com.desafio.repository.SellerRepository;
import br.com.desafio.repository.entity.InvoiceEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConfirmPaymentUseCaseImpl implements ConfirmPaymentUseCase {

    private final SellerRepository sellerRepository;

    private final List<PaymentProcessorStrategy> paymentProcessorList;

    @Override
    public PaymentModel confirm(PaymentModel paymentModel) {
        var seller = sellerRepository.findById(paymentModel.getClientId())
                .orElseThrow(() -> new SellerNotFoundException(paymentModel.getClientId()));

        validateItems(seller.getInvoiceList(), paymentModel.getPaymentItems());

        paymentModel.setPaymentItems(
                process(paymentModel.getPaymentItems(), seller.getInvoiceList()));

        return paymentModel;
    }

    private List<PaymentItemModel> process(List<PaymentItemModel> paymentItems, List<InvoiceEntity> invoiceList) {
        return paymentItems.stream()
                .map(item -> invoiceList.stream()
                        .filter(inv -> inv.getInvoiceId().equals(item.getPaymentId()))
                        .findFirst()
                        .map(invoice -> Pair.of(item, item.getPaymentValue().compareTo(invoice.getAmount())))
                        .orElseGet(null))
                .map(pair -> paymentProcessorList.stream()
                        .filter(proc -> proc.getStrategy() == pair.getRight())
                        .findFirst()
                        .map(processor -> processor.process(pair.getLeft()))
                        .orElseGet(null)
                )
                .filter(Objects::nonNull)
                .toList();
    }

    private void validateItems(List<InvoiceEntity> invoiceList, List<PaymentItemModel> paymentItems) {
        var listId = invoiceList.stream().map(InvoiceEntity::getInvoiceId).toList();
        var notFoundIdList = paymentItems.stream()
                .map(payItem -> listId.contains(payItem.getPaymentId()) ? null : payItem.getPaymentId())
                .filter(Objects::nonNull)
                .toList();
        if (!notFoundIdList.isEmpty()) {
            throw new InvoiceNotFoundException(notFoundIdList.toString());
        }
    }

}
