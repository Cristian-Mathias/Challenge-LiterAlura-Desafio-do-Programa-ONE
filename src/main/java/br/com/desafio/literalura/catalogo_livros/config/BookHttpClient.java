package br.com.desafio.literalura.catalogo_livros.config;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BookHttpClient {

    private final HttpClient client;

    public BookHttpClient() {
        this.client = HttpClient.newHttpClient();
    }

    public String buscarLivros() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/books/v1/volumes?q=java"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Corpo: " + response.body());

        return response.body();
    }
}
