package br.com.desafio.literalura.catalogo_livros.config;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;

public class HttpClientConfig {

    public static HttpClient createDefaultClient(){
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .executor(Executors.newFixedThreadPool(4))
                .build();
    }
}
