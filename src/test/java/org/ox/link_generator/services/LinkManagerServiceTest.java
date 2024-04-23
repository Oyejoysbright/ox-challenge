package org.ox.link_generator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ox.link_generator.entities.Invoice;
import org.ox.link_generator.enums.LinkType;
import org.ox.link_generator.models.JwtInvoicePayload;
import org.ox.link_generator.repos.InvoiceRepo;
import org.ox.link_generator.utils.CustomException;
import org.ox.link_generator.utils.CustomResponse;
import org.ox.link_generator.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkManagerServiceTest {

    @Mock
    private InvoiceRepo invoiceRepo;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private LinkManagerService service;

    @Test
    public void successfullyGenerateLink() throws JsonProcessingException {
        when(invoiceRepo.findById(Mockito.anyString())).thenReturn(Optional.of(new Invoice()));
        when(jwtUtils.generateToken(any(JwtInvoicePayload.class))).thenReturn("testToken");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://example.com/api/"));
        when(request.getRequestURI()).thenReturn("/api/");

        ResponseEntity<CustomResponse> response = service.generateLink(LinkType.PAYMENT, "123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        String link = (String) response.getBody().getData();
        assertTrue(StringUtils.isNotBlank(link));
    }

    @Test
    public void invoiceNotFoundWhileGeneratingLink() throws JsonProcessingException {
        when(invoiceRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomException.class, () -> {
            service.generateLink(LinkType.PAYMENT, "123");
        });
    }

    @Test
    public void successfullyRetrieveInvoiceDetails() throws JsonProcessingException {
        JwtInvoicePayload payload = new JwtInvoicePayload(LinkType.PAYMENT, new Invoice());
        when(jwtUtils.getTokenData("testToken", JwtInvoicePayload.class)).thenReturn(payload);

        ResponseEntity<CustomResponse> response = service.retrieveInvoiceDetails("testToken");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void successfullyMakePayment() throws JsonProcessingException {
        JwtInvoicePayload payload = new JwtInvoicePayload(LinkType.PAYMENT, new Invoice());
        when(jwtUtils.getTokenData("testToken", JwtInvoicePayload.class)).thenReturn(payload);
        assertDoesNotThrow(() -> {
            service.makePayment("testToken");
        });
    }
}