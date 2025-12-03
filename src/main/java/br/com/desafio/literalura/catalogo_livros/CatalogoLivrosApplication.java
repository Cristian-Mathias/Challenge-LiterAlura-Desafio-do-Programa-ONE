package br.com.desafio.literalura.catalogo_livros;

import br.com.desafio.literalura.catalogo_livros.config.BookHttpClient;
import br.com.desafio.literalura.catalogo_livros.dto.GutendexResponseDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogoLivrosApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(CatalogoLivrosApplication.class, args);

		BookHttpClient client = new BookHttpClient();
		GutendexResponseDTO resposta = client.buscarLivros();
		client.imprimirLivros(resposta);
	}

}
