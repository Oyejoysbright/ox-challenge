package org.ox.link_generator.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ox.link_generator.enums.LinkType;
import org.ox.link_generator.services.LinkManagerService;
import org.ox.link_generator.utils.CustomResponse;
import org.ox.link_generator.utils.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.Mockito.when;

@WebMvcTest(LinkManagerController.class)
public class LinkManagerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LinkManagerService service;
    private RequestBuilder builder;
    private MockHttpServletResponse res;
    private final ObjectMapper MAPPER = new ObjectMapper();
    private static MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

    @BeforeEach
    public void beforeEach() throws Exception {
        params.clear();
        params.add("invoiceId", "random");
    }

    @Test
    public void successfullyGenerateDetailsLink() throws Exception {
        when(service.generateLink(Mockito.any(), Mockito.anyString()))
                .thenReturn(CustomResponse.ok(Mockito.anyString(), Mockito.anyString()));
        params.add("type", LinkType.INVOICE_DETAILS.name());
        builder = MockMvcRequestBuilders.get("/generate")
                .params(params);
        res = mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        CustomResponse resPayload = MAPPER.readValue(res.getContentAsString(), CustomResponse.class);
        Assertions.assertFalse(resPayload.getHasError());
        Assertions.assertTrue(resPayload.getData() instanceof String);
    }

    @Test
    public void successfullyGeneratePaymentLink() throws Exception {
        when(service.generateLink(Mockito.any(), Mockito.anyString()))
                .thenReturn(CustomResponse.ok(Mockito.anyString(), Mockito.anyString()));
        params.add("type", LinkType.PAYMENT.name());
        builder = MockMvcRequestBuilders.get("/generate")
                .params(params);
        res = mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        CustomResponse resPayload = MAPPER.readValue(res.getContentAsString(), CustomResponse.class);
        Assertions.assertFalse(resPayload.getHasError());
        Assertions.assertTrue(resPayload.getData() instanceof String);
    }

    @Test
    public void invalidGenerationLinkType() throws Exception {
        params.add("type", "WRONG_TYPE");
        builder = MockMvcRequestBuilders.get("/generate")
                .params(params);
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void successfullyFetchInvoiceDetails() throws Exception {
        when(service.retrieveInvoiceDetails(Mockito.anyString()))
                .thenReturn(CustomResponse.ok(Mockito.anyString(), CustomResponse.class));
        builder = MockMvcRequestBuilders.get(Paths.INVOICE_DETAILS)
                .param("token", "random");
        res = mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();
        CustomResponse resPayload = MAPPER.readValue(res.getContentAsString(), CustomResponse.class);
        Assertions.assertFalse(resPayload.getHasError());
        Assertions.assertNotNull(resPayload.getData());
    }

    @Test
    public void successfullyRedirectToPaymentService() throws Exception {
        when(service.makePayment(Mockito.anyString()))
                .thenReturn(Mockito.anyString());
        builder = MockMvcRequestBuilders.get(Paths.MAKE_PAYMENT)
                .param("token", "random");
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andReturn();
    }
}
