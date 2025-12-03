package br.com.desafio.literalura.catalogo_livros.config;

import br.com.desafio.literalura.catalogo_livros.dto.BookDTO;
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

    public GutendexResponseDTO buscarLivros() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://gutendex.com/books/?search=java"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());

        String json = response.body();

        GutendexResponseDTO resposta = mapper.readValue(json, GutendexResponseDTO.class);

        return resposta;
    }

    public void imprimirLivros(GutendexResponseDTO resposta){
        System.out.println("Total de livros encontrados: " + resposta.getCount());
        for (BookDTO livro : resposta.getResults()) {
            System.out.println("Título: " + livro.getTitle());
            System.out.print("Autores: ");
            livro.getAuthors().forEach(a -> System.out.print(a.getName() + " "));
            System.out.println("\nDownload Count: " + livro.getDownloadCount());
            System.out.println("---------------------------");
        }
    }
}
