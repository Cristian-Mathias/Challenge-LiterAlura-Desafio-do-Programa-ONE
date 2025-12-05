package br.com.desafio.literalura.catalogo_livros.service;

import br.com.desafio.literalura.catalogo_livros.config.BookHttpClient;
import br.com.desafio.literalura.catalogo_livros.dto.GutendexResponseDTO;
import br.com.desafio.literalura.catalogo_livros.model.Author;
import br.com.desafio.literalura.catalogo_livros.model.Book;
import br.com.desafio.literalura.catalogo_livros.repository.AuthorRepository;
import br.com.desafio.literalura.catalogo_livros.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Scanner;

@Service
public class BookService {
    private final Scanner scanner = new Scanner(System.in);
    private final BookHttpClient httpClient;

    @Autowired
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public BookService(BookHttpClient httpClient, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.httpClient = httpClient;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void exibirMenu() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===== CATÁLOGO DE LIVROS =====");
            System.out.println("1 - Buscar livro pelo título");
            System.out.println("2 - Listar livros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos em um determinado ano");
            System.out.println("5 - Listar livros por idioma");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> buscarLivroPorTitulo();
                case 2 -> listarTodosLivros();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosPorAno();
                case 5 -> listarLivrosPorIdioma();
                case 0 -> System.out.println("Encerrando aplicação...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    public void buscarLivroPorTitulo() {
        System.out.print("Digite o título: ");
        String titulo = scanner.nextLine();

        GutendexResponseDTO resposta = httpClient.buscarLivrosPorTitulo(titulo);

        if (resposta == null || resposta.getResults().isEmpty()) {
            System.out.println("Nenhum livro encontrado para o título informado.");
            return;
        }

        httpClient.imprimirLivros(resposta);

        System.out.print("Deseja salvar os livros encontrados no banco? (S/N): ");
        String opcao = scanner.nextLine();

        if (opcao.equalsIgnoreCase("S")) {
            for (var dto : resposta.getResults()) {

                Author author = null;

                if (dto.getAuthors() != null && !dto.getAuthors().isEmpty()) {
                    var jsonAuthor = dto.getAuthors().get(0);

                    if (jsonAuthor.getName() != null && !jsonAuthor.getName().isBlank()) {
                        author = authorRepository.findByName(jsonAuthor.getName())
                                .orElseGet(() -> authorRepository.save(
                                        new Author(
                                                jsonAuthor.getName(),
                                                jsonAuthor.getBirthyear(),
                                                jsonAuthor.getDeathyear()
                                        )
                                ));
                    } else {
                        System.out.println("Autor inválido, livro não terá autor salvo: " + dto.getTitle());
                    }
                } else {
                    System.out.println("Livro sem autores: " + dto.getTitle());
                }

                Book book = new Book(dto);
                book.setAuthor(author);
                bookRepository.save(book);

                System.out.println("Livro salvo: " + book.getTitle());
            }

            System.out.println("Todos os livros salvos com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
    }


    @Transactional
    private void listarTodosLivros() {
        List<Book> books = bookRepository.findAllWithAuthors();

        if(books.isEmpty()){
            System.out.println("Nenhum livro cadastrado no banco.");
        }

        for (Book book : books) {
            String autor = book.getAuthor() != null ? book.getAuthor().getName() : "Autor desconhecido";

            System.out.println("---------------------------");
            System.out.println("Título: " + book.getTitle());
            System.out.println("Autor: " + autor);
            System.out.println("Idioma: " + book.getLanguage());
            System.out.println("Downloads: " + book.getDownloadCount());
        }
    }

    private void listarAutoresRegistrados() {
        List<Author> authors = authorRepository.findAll();

        if (authors.isEmpty()){
            System.out.println("Nenhum autor cadastrado no banco.");
            return;
        }

        System.out.println("Autores registrados:");
        imprimirAutores(authors);
    }

    private void listarAutoresVivosPorAno(){
        System.out.print("Digite o ano: ");
        int ano = Integer.parseInt(scanner.nextLine());

        List<Author> authors = authorRepository.findLivingAuthorsByYear(ano);

        if (authors.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado no ano " + ano);
            return;
        }

        System.out.println("Autores vivos em " + ano + ":");
        imprimirAutores(authors);
    }

    private  void listarLivrosPorIdioma(){
        System.out.println("Digite o idioma (ex: en, pt, fr): ");
    }


    private void imprimirAutores(List<Author> authors) {
        if (authors.isEmpty()) {
            System.out.println("Nenhum autor encontrado.");
            return;
        }

        for (Author author : authors) {
            String birth = author.getBirthyear() != null ? author.getBirthyear().toString() : "Desconhecido";
            String death = author.getDeathyear() != null ? author.getDeathyear().toString() : "Ainda vivo";

            System.out.println("---------------------------");
            System.out.println("Nome: " + author.getName());
            System.out.println("Ano de nascimento: " + birth);
            System.out.println("Ano de falecimento: " + death);
        }
    }
}