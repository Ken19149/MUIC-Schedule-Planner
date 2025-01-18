import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class planner {
    public static void main(String[] args) {
        // access JSON from a url
        HttpRequest muicSchedule = access("https://os.muic.io/data.json");
        System.out.println(muicSchedule);
    }

    public static HttpRequest access(String link) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.valueOf(link)))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();

        return request;
    }

}
