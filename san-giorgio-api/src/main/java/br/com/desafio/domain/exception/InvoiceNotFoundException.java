package br.com.desafio.domain.exception;

public class InvoiceNotFoundException extends RuntimeException {

    public InvoiceNotFoundException(String ids) {
        super("Código(s) de cobrança(s) não encontrado(s):" + ids);
    }
}
