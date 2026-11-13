package mini_lab.day26;

import org.jfl.domain.PaymentMethod;
import org.jfl.dto.PaymentRequest;
import org.jfl.dto.PaymentResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;

public class RestClientDemo {

    private static final String BASE_URL = "http://localhost:8081";
    private static final Long EXISTING_PAYMENT_ID = 1L;
    private static final Long MISSING_PAYMENT_ID = 9999999999L;


    public static void main(String[] args) {

        RestClient restClient =
                RestClient.builder()
                        .baseUrl(BASE_URL)
                        .build();


        System.out.println("""
                
                ==========================
                EXERCISE 1 - GET PAYMENT
                ==========================
                """);

        getExistingPayment(restClient);


        System.out.println("""
                
                ==========================
                EXERCISE 2 - GET 404
                ==========================
                """);

        getMissingPayment(restClient);


        System.out.println("""
                
                ==========================
                EXERCISE 3 - POST PREVIEW
                ==========================
                """);

        postValidPayment(restClient);


        System.out.println("""
                
                ==========================
                EXERCISE 4 - INVALID POST
                ==========================
                """);

        postInvalidPayment(restClient);


        System.out.println("""
                
                ==========================
                EXERCISE 5 - RESPONSE ENTITY
                ==========================
                """);

        getPaymentWithResponseEntity(
                restClient
        );


        System.out.println("""
                
                ==========================
                EXERCISE 6 - ON STATUS
                ==========================
                """);

        getPaymentWithCustomStatusHandling(
                restClient
        );
    }


    /*
     * Exercise 1
     *
     * Basic GET request.
     *
     * HTTP JSON
     *      ↓
     * Jackson
     *      ↓
     * PaymentResponse
     */
    private static void getExistingPayment(
            RestClient restClient) {

        PaymentResponse payment =
                restClient
                        .get()
                        .uri(
                                "/payments/{id}",
                                EXISTING_PAYMENT_ID
                        )
                        .accept(
                                MediaType.APPLICATION_JSON
                        )
                        .retrieve()
                        .body(
                                PaymentResponse.class
                        );

        System.out.println(
                "Payment:"
        );

        System.out.println(
                payment
        );
    }


    /*
     * Exercise 2
     *
     * Call a payment that does not exist.
     *
     * Important difference:
     *
     * HttpClient
     * 404 → HttpResponse
     *
     * RestClient.retrieve()
     * 404 → exception by default
     */
    private static void getMissingPayment(
            RestClient restClient) {

        try {

            PaymentResponse payment =
                    restClient
                            .get()
                            .uri(
                                    "/payments/{id}",
                                    MISSING_PAYMENT_ID
                            )
                            .accept(
                                    MediaType.APPLICATION_JSON
                            )
                            .retrieve()
                            .body(
                                    PaymentResponse.class
                            );

            System.out.println(
                    payment
            );

        } catch (
                RestClientResponseException ex
        ) {

            System.err.println(
                    "HTTP request failed"
            );

            System.err.println(
                    "Status: "
                            + ex.getStatusCode()
            );

            System.err.println(
                    "Response body:"
            );

            System.err.println(
                    ex.getResponseBodyAsString()
            );
        }
    }


    /*
     * Exercise 3
     *
     * POST a valid PaymentRequest.
     *
     * PaymentRequest
     *      ↓
     * Jackson
     *      ↓
     * JSON
     *      ↓
     * HTTP
     */
    private static void postValidPayment(
            RestClient restClient) {

        PaymentRequest request =
                new PaymentRequest(
                        new BigDecimal(
                                "100.00"
                        ),
                        PaymentMethod.PIX,
                        "PAY-2026-123456"
                );

        PaymentResponse response =
                restClient
                        .post()
                        .uri(
                                "/payments/preview"
                        )
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .accept(
                                MediaType.APPLICATION_JSON
                        )
                        .body(
                                request
                        )
                        .retrieve()
                        .body(
                                PaymentResponse.class
                        );

        System.out.println(
                "Preview:"
        );

        System.out.println(
                response
        );
    }


    /*
     * Exercise 4
     *
     * Send invalid data.
     *
     * Day 24 Bean Validation should reject:
     *
     * amount = -100
     * reference = INVALID
     *
     * Expected:
     *
     * 400 Bad Request
     */
    private static void postInvalidPayment(
            RestClient restClient) {

        PaymentRequest invalidRequest =
                new PaymentRequest(
                        new BigDecimal(
                                "-100.00"
                        ),
                        PaymentMethod.PIX,
                        "INVALID"
                );

        try {

            PaymentResponse response =
                    restClient
                            .post()
                            .uri(
                                    "/payments/preview"
                            )
                            .contentType(
                                    MediaType.APPLICATION_JSON
                            )
                            .accept(
                                    MediaType.APPLICATION_JSON
                            )
                            .body(
                                    invalidRequest
                            )
                            .retrieve()
                            .body(
                                    PaymentResponse.class
                            );

            System.out.println(
                    response
            );

        } catch (
                RestClientResponseException ex
        ) {

            System.err.println(
                    "Validation failed"
            );

            System.err.println(
                    "Status: "
                            + ex.getStatusCode()
            );

            System.err.println(
                    "Error body:"
            );

            System.err.println(
                    ex.getResponseBodyAsString()
            );
        }
    }


    /*
     * Exercise 5
     *
     * Instead of getting only the body,
     * retrieve the complete ResponseEntity.
     *
     * This gives us:
     *
     * status
     * headers
     * body
     */
    private static void getPaymentWithResponseEntity(
            RestClient restClient) {

        ResponseEntity<PaymentResponse>
                response =
                restClient
                        .get()
                        .uri(
                                "/payments/{id}",
                                EXISTING_PAYMENT_ID
                        )
                        .accept(
                                MediaType.APPLICATION_JSON
                        )
                        .retrieve()
                        .toEntity(
                                PaymentResponse.class
                        );

        System.out.println(
                "Status: "
                        + response.getStatusCode()
        );

        System.out.println(
                "Headers: "
                        + response.getHeaders()
        );

        System.out.println(
                "Body: "
                        + response.getBody()
        );
    }


    /*
     * Exercise 6
     *
     * Customize status handling.
     *
     * Translate:
     *
     * HTTP 404
     *      ↓
     * Java/application exception
     */
    private static void
    getPaymentWithCustomStatusHandling(
            RestClient restClient) {

        try {

            PaymentResponse payment =
                    restClient
                            .get()
                            .uri(
                                    "/payments/{id}",
                                    MISSING_PAYMENT_ID
                            )
                            .retrieve()
                            .onStatus(
                                    status ->
                                            status.value()
                                                    == 404,

                                    (
                                            request,
                                            response
                                    ) -> {

                                        throw new IllegalStateException(
                                                "Payment not found"
                                        );
                                    }
                            )
                            .body(
                                    PaymentResponse.class
                            );

            System.out.println(
                    payment
            );

        } catch (
                IllegalStateException ex
        ) {

            System.err.println(
                    ex.getMessage()
            );
        }
    }
}