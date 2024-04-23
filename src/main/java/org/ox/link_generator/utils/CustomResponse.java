
package org.ox.link_generator.utils;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.*;
import lombok.*;
import org.springframework.http.*;

/**
 *
 * @author Ayomide.Oyediran
 */
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomResponse {
    Boolean hasError = false;
    String message;
    Object data;
    static CustomResponse body = new CustomResponse();

    private static <T> ResponseEntity<T> builder(HttpStatus status, T body) {
        return ResponseEntity.status(status == null? HttpStatus.OK : status).body(body);
    }

    public static ResponseEntity<CustomResponse> error(String message, Object data) {
        body.hasError = true;
        body.message = message == null? ResponseMessage.SERVER_ERROR : message;
        body.data = data;
        return builder(HttpStatus.INTERNAL_SERVER_ERROR, body);
    }

    public static ResponseEntity<CustomResponse> custom(Boolean hasError, HttpStatus status, String message, Object data) {
        body.hasError = hasError;
        body.message = message;
        body.data = data;
        return builder(status, body);
    }

    public static <T> ResponseEntity<T> customEX(Boolean hasError, HttpStatus status, String message, Object data, Class<T> returnClass) {
        body.hasError = hasError;
        body.message = message;
        body.data = data;
        return builder(status, (T) body);
    }

    public static ResponseEntity<CustomResponse> ok(String message, Object data) {
        body.hasError = false;
        body.message = message;
        body.data = data;
        return builder(null, body);
    }

    public static ResponseEntity<CustomResponse> badRequest(String message, Object data) {
        body.hasError = true;
        body.message = message;
        body.data = data;
        return builder(HttpStatus.BAD_REQUEST, body);
    }

    public static ResponseEntity<CustomResponse> badRequest(String message) {
        body.hasError = true;
        body.message = message;
        body.data = null;
        return builder(HttpStatus.BAD_REQUEST, body);
    }

    public static ResponseEntity<CustomResponse> notFound(String message) {
        body.hasError = true;
        body.message = message;
        body.data = null;
        return builder(HttpStatus.NOT_FOUND, body);
    }
}
