package org.ox.link_generator.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ox.link_generator.enums.LinkType;
import org.ox.link_generator.utils.CustomResponse;
import org.springframework.http.ResponseEntity;

public interface ILinkManager {
    ResponseEntity<CustomResponse> generateLink(LinkType type, String invoiceId) throws JsonProcessingException;
    ResponseEntity<CustomResponse> retrieveInvoiceDetails(String token) throws JsonProcessingException;
    String makePayment(String token) throws JsonProcessingException;
}
