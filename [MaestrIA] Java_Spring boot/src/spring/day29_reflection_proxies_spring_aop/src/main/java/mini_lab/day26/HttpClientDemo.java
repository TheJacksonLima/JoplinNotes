package mini_lab.day26;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

public class HttpClientDemo {

    private static final String PAYMENTS_URL = "http://localhost:8081/payments";

    static boolean isSuccessful(int status){
        return status >=200 && status < 300;
    }

    public static void main(String[] args){
        String missingPayment = PAYMENTS_URL +"/9999999999";
        String goodPayment = PAYMENTS_URL +"/1";

        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
        //URI uri = URI.create(PAYMENTS_URL);
        //URI uri = URI.create(missingPayment);
        URI uri = URI.create(goodPayment);
        HttpRequest request = HttpRequest.newBuilder(uri)
                                         .timeout(Duration.ofSeconds(3))
                                         .header("Accept", "application/json")
                                         .GET()
                                         .build();

        try{
            var response = client.send(request,HttpResponse.BodyHandlers.ofString());
            if (isSuccessful(response.statusCode())) {
                System.out.println(response.body());
            } else {
                System.out.println("Request failed: " +  response.statusCode());
            }

        }
        catch (HttpTimeoutException ex) {
            System.err.println("Request timed out");
        }
        catch (IOException ex) {
            System.err.println("Network failure: " + ex.getMessage() );
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Request interrupted");
        }
    }
}