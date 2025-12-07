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

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@Service
public class BookService {
    private final Scanner scanner = new Scanner(System.in);
    private final BookHttpClient httpClient;

    @Autowired
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

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
                case 5 -> listarLivrosPorIdiomaMenu();
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

    private void listarAutoresVivosPorAno() {
        System.out.print("Digite o ano: ");
        String input = scanner.nextLine().trim();

        if (!input.matches("\\d+")) {
            System.out.println(" Ano inválido! Digite apenas números positivos.");
            return;
        }

        int ano = Integer.parseInt(input);

        if (ano <= 0) {
            System.out.println(" Ano inválido! O ano deve ser maior que zero.");
            return;
        }

        List<Author> authors = authorRepository.findLivingAuthorsByYear(ano);

        if (authors.isEmpty()) {
            System.out.println("Nenhum autor vivo encontrado no ano " + ano + ".");
            return;
        }

        System.out.println("\n Autores vivos em " + ano + ":");
        imprimirAutores(authors);
    }


    private void listarLivrosPorIdiomaMenu() {
        System.out.println("Escolha um número correspondente ao idioma:");
        System.out.println(" '1' - Português (pt)");
        System.out.println(" '2' - Inglês (en)");
        System.out.print("Opção: ");

        int opcao = Integer.parseInt(scanner.nextLine());
        if (opcao != 1 && opcao != 2) {
            System.out.println("Opção inválida! Escolha 1 ou 2.");
            return;
        }

        String language = opcao == 1 ? "pt" : "en";

        List<Book> livros = listarLivrosPorIdioma(language);
        Long total = contarLivrosPorIdioma(language);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma: " + language);
            return;
        }

        System.out.println("\n=== Livros encontrados no idioma '" + language + "' ===");
        for (Book livro : livros) {
            String autor = livro.getAuthor() != null
                    ? livro.getAuthor().getName()
                    : "Autor desconhecido";

            System.out.println("---------------------------");
            System.out.println("Título: " + livro.getTitle());
            System.out.println("Autor: " + autor);
            System.out.println("Downloads: " + livro.getDownloadCount());
        }

        System.out.println("\nTotal de livros encontrados: " + total);

        int somaDownloads = livros.stream()
                .mapToInt(Book::getDownloadCount)
                .sum();

        double mediaDownloads = livros.stream()
                .mapToInt(Book::getDownloadCount)
                .average()
                .orElse(0.0);

        Book maisPopular = livros.stream()
                .max(Comparator.comparingInt(Book::getDownloadCount))
                .orElse(null);

        Book menosPopular = livros.stream()
                .min(Comparator.comparingInt(Book::getDownloadCount))
                .orElse(null);

        System.out.println("\n=== Estatísticas do idioma '" + language + "' ===");
        System.out.println("Soma total de downloads: " + somaDownloads);
        System.out.printf("Média de downloads: %.2f%n", mediaDownloads);

        if (maisPopular != null) {
            System.out.println("Livro mais popular: " + maisPopular.getTitle() +
                    " (" + maisPopular.getDownloadCount() + " downloads)");
        }

        if (menosPopular != null) {
            System.out.println("Livro menos popular: " + menosPopular.getTitle() +
                    " (" + menosPopular.getDownloadCount() + " downloads)");
        }
    }

    private List<Book> listarLivrosPorIdioma(String language) {
        return bookRepository.findByLanguageFetchAuthor(language);
    }

    private Long contarLivrosPorIdioma(String language){
        return bookRepository.countByLanguage(language);
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