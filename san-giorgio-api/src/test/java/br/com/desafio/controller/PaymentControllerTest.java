package br.com.desafio.controller;

import br.com.desafio.domain.exception.InvoiceNotFoundException;
import br.com.desafio.domain.exception.SellerNotFoundException;
import br.com.desafio.domain.exception.handler.CustomExceptionHandler;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.usecase.ConfirmPaymentUseCase;
import br.com.desafio.mock.PaymentMock;
import br.com.desafio.mock.PaymentModelMock;
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

import java.nio.charset.StandardCharsets;

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
        var paymentModel = PaymentModelMock.create();
        var expected = PaymentMock.create();

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
        var expected = PaymentMock.create();

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
        var expected = PaymentMock.create();

        when(confirmPaymentUseCase.confirm(any(PaymentModel.class)))
                .thenThrow(new InvoiceNotFoundException(TEST_ID));

        mockMvc.perform(
                        put(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(expected)))
                .andExpect(status().isUnprocessableEntity());
    }

}