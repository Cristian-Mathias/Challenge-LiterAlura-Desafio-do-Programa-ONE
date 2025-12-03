package br.com.desafio.literalura.catalogo_livros.config;

import br.com.desafio.literalura.catalogo_livros.dto.GutendexResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BookHttpClient {

    private final HttpClient client;
    private final ObjectMapper mapper;

    public BookHttpClient() {
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient();
    }

    public String buscarLivros() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://gutendex.com/books/?search=java"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());

        String json = response.body();

        GutendexResponseDTO resposta = mapper.readValue(json, GutendexResponseDTO.class);

        System.out.println("Total de livros encontrados: " + resposta.getCount());
        System.out.println("Título do primeiro livro: " + resposta.getResults().get(0).getTitle());
        System.out.println("Autor(es): " + resposta.getResults().get(0).getAuthors().get(0).getName());

        return json;
    }
}
