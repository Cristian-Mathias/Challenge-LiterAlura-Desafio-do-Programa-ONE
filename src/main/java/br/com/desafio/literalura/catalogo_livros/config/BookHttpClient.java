package br.com.desafio.literalura.catalogo_livros.config;

import br.com.desafio.literalura.catalogo_livros.dto.BookDTO;
import br.com.desafio.literalura.catalogo_livros.dto.GutendexResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
public class BookHttpClient {

    private final HttpClient client;
    private final ObjectMapper mapper;
    private static final String BASE_URL = "https://gutendex.com/books/";

    public BookHttpClient() {
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient();
    }

    private GutendexResponseDTO enviarRequisicao(String url) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode());

            String json = response.body();

            return mapper.readValue(json, GutendexResponseDTO.class);

        } catch (Exception e) {
            System.out.println("Erro na requisição HTTP: " + e.getMessage());
            return null;
        }
    }

    public GutendexResponseDTO buscarLivros() {
        String url = BASE_URL + "?search=java";
        return enviarRequisicao(url);
    }

    public GutendexResponseDTO buscarLivrosPorTitulo(String titulo) {
        String url = BASE_URL + "?search=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        return enviarRequisicao(url);
    }

    public void imprimirLivros(GutendexResponseDTO resposta) {
        if (resposta == null || resposta.getResults() == null) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        System.out.println("Total de livros encontrados: " + resposta.getCount());

        for (BookDTO livro : resposta.getResults()) {
            System.out.println("Título: " + livro.getTitle());
            System.out.print("Autores: ");
            livro.getAuthors().forEach(a -> System.out.print(a.getName() + " "));

            System.out.println("\nDownloads: " + livro.getDownloadCount());
            System.out.println("---------------------------");
        }
    }
}
