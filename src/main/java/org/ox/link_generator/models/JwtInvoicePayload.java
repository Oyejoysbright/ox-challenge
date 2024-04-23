package org.ox.link_generator.models;

import lombok.*;
import org.ox.link_generator.entities.Invoice;
import org.ox.link_generator.enums.LinkType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtInvoicePayload {
    private LinkType type;
    private Invoice data;
}
