package br.com.desafio.domain.exception;

public class SellerNotFoundException extends RuntimeException {

    public SellerNotFoundException(String clientId) {
        super("Vendedor não encontrado, código: " + clientId);
    }
}
