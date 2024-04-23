package org.ox.link_generator.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.ox.link_generator.enums.LinkType;
import org.ox.link_generator.services.LinkManagerService;
import org.ox.link_generator.utils.CustomResponse;
import org.ox.link_generator.utils.Paths;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Api
@Validated
public class LinkManagerController {
    private final LinkManagerService service;

    @GetMapping("/generate")
    public ResponseEntity<CustomResponse> generateLink(LinkType type, @NotNull String invoiceId) throws JsonProcessingException {
        return service.generateLink(type, invoiceId);
    }

    @GetMapping(Paths.INVOICE_DETAILS)
    public ResponseEntity<CustomResponse> retrieveInvoiceDetails(@NotBlank String token) throws JsonProcessingException {
        return service.retrieveInvoiceDetails(token);
    }

    @GetMapping(Paths.MAKE_PAYMENT)
    public void makePayment(@NotBlank String token, HttpServletResponse response) throws IOException {
        response.sendRedirect(service.makePayment(token));
    }

}
