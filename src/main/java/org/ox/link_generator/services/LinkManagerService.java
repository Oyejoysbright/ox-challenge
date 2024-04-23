package org.ox.link_generator.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.ox.link_generator.entities.Invoice;
import org.ox.link_generator.enums.LinkType;
import org.ox.link_generator.interfaces.ILinkManager;
import org.ox.link_generator.models.JwtInvoicePayload;
import org.ox.link_generator.repos.InvoiceRepo;
import org.ox.link_generator.utils.CustomException;
import org.ox.link_generator.utils.CustomResponse;
import org.ox.link_generator.utils.JwtUtils;
import org.ox.link_generator.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class LinkManagerService implements ILinkManager {

    private final InvoiceRepo invoiceRepo;
    private final JwtUtils jwtUtils;
    private final HttpServletRequest request;
    @Value("${server.servlet.context-path}")
    private String serverContextPath;
    @Value("${payment-url}")
    private String paymentUrl;

    @Override
    public ResponseEntity<CustomResponse> generateLink(LinkType type, String invoiceId) throws JsonProcessingException {
        Invoice invoice = invoiceRepo.findById(invoiceId).orElseThrow(
                () -> new CustomException(
                        HttpStatus.NOT_FOUND,
                        ResponseMessage.INVOICE_NOT_FOUND,
                        null
                )
        );

        String token = jwtUtils.generateToken(new JwtInvoicePayload(type, invoice));
        String reqUrl = request.getRequestURL().toString();
        String link = reqUrl.replace(
                request.getRequestURI(),
                serverContextPath + type.getUrlPath() + "/?token=" + token
        );

        return CustomResponse.ok(
                ResponseMessage.LINK_GENERATED,
                link
        );
    }

    @Override
    public ResponseEntity<CustomResponse> retrieveInvoiceDetails(String token) throws JsonProcessingException {
        JwtInvoicePayload payload = jwtUtils.getTokenData(token, JwtInvoicePayload.class);
        return CustomResponse.ok(
                ResponseMessage.DETAILS_RETRIEVED,
                payload.getData()
        );
    }

    @Override
    public String makePayment(String token) throws JsonProcessingException {
        JwtInvoicePayload payload = jwtUtils.getTokenData(token, JwtInvoicePayload.class);
        return paymentUrl+payload.getData().getId();
    }
}
