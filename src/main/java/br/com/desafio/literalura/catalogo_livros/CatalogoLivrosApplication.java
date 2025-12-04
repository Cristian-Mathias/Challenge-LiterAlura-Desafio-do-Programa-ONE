package br.com.desafio.literalura.catalogo_livros;

import br.com.desafio.literalura.catalogo_livros.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogoLivrosApplication implements CommandLineRunner {


	private BookService bookService;

    public CatalogoLivrosApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) throws Exception {
		SpringApplication.run(CatalogoLivrosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		bookService.exibirMenu();
	}
}
