package org.ox.link_generator.enums;

import lombok.Getter;
import org.ox.link_generator.utils.Paths;

public enum LinkType {
    INVOICE_DETAILS(Paths.INVOICE_DETAILS),
    PAYMENT (Paths.MAKE_PAYMENT);

    @Getter
    private final String urlPath;

    private LinkType (String urlPath) {
        this.urlPath = urlPath;
    }
}
