package br.com.desafio.controller;

import br.com.desafio.domain.exception.InvoiceNotFoundException;
import br.com.desafio.domain.exception.SellerNotFoundException;
import br.com.desafio.domain.exception.handler.CustomExceptionHandler;
import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.usecase.ConfirmPaymentUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest( classes = {
        PaymentController.class,
        ConfirmPaymentUseCase.class,
        CustomExceptionHandler.class
})
@EnableWebMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"server.port=8083"})
class PaymentControllerTest {

    public static final String PAYMENT_ID = "789";
    public static final BigDecimal PAYMENT_VALUE = BigDecimal.valueOf(100);
    public static final String PAYMENT_STATUS = "FullPayment";
    public static final String CLIENT_ID = "123";
    public static final String TEST_ID = "1";
    public static final String PATH = "/api/payment";

    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @MockBean
    private ConfirmPaymentUseCase confirmPaymentUseCase;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(
                        (request, response, chain) -> {
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            chain.doFilter(request, response);
                        },
                        "/*")
                .build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void setPaymentSuccess() throws Exception {
        var paymentModel = getPaymentModel();
        var expected = getPayment();

        when(confirmPaymentUseCase.confirm(any(PaymentModel.class)))
                .thenReturn(paymentModel);

        var res = mockMvc.perform(
                        put(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(expected)))
                .andExpect(status().isOk());

        var actual = mapper.readValue(res.andReturn().getResponse().getContentAsString(), Payment.class);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void setPaymentSellerNotFoundException() throws Exception {
        var expected = getPayment();

        when(confirmPaymentUseCase.confirm(any(PaymentModel.class)))
                .thenThrow(new SellerNotFoundException(TEST_ID));

        mockMvc.perform(
                        put(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(expected)))

                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void setPaymentInvoiceNotFoundException() throws Exception {
        var expected = getPayment();

        when(confirmPaymentUseCase.confirm(any(PaymentModel.class)))
                .thenThrow(new InvoiceNotFoundException(TEST_ID));

        mockMvc.perform(
                        put(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(expected)))
                .andExpect(status().isUnprocessableEntity());
    }

    private PaymentModel getPaymentModel() {
        return PaymentModel.builder()
                .clientId(CLIENT_ID)
                .paymentItems(List.of(PaymentItemModel.builder()
                        .paymentId(PAYMENT_ID)
                        .paymentValue(PAYMENT_VALUE)
                        .paymentStatus(PAYMENT_STATUS)
                        .build()))
                .build();
    }

    private Payment getPayment() {
        return Payment.builder()
                .clientId(CLIENT_ID)
                .paymentItems(List.of(PaymentItem.builder()
                        .paymentId(PAYMENT_ID)
                        .paymentValue(PAYMENT_VALUE)
                        .paymentStatus(PAYMENT_STATUS)
                        .build()))
                .build();
    }

}