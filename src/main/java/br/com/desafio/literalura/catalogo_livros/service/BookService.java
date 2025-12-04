package br.com.desafio.literalura.catalogo_livros.service;

import br.com.desafio.literalura.catalogo_livros.config.BookHttpClient;
import br.com.desafio.literalura.catalogo_livros.dto.GutendexResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class BookService {
    private final Scanner scanner = new Scanner(System.in);
    private final BookHttpClient httpClient;

    public BookService(BookHttpClient httpClient) {
        this.httpClient = httpClient;
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
                case 2 -> listarLivrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosPorAno();
                case 5 -> listarLivrosPorIdioma();
                case 0 -> System.out.println("Encerrando aplicação...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    public void buscarLivroPorTitulo(){
        System.out.print("Digite o título: ");
        String titulo = scanner.nextLine();

        GutendexResponseDTO resposta = httpClient.buscarLivrosPorTitulo(titulo);
        httpClient.imprimirLivros(resposta);
    }

    private void listarLivrosRegistrados() {
        System.out.println("Listando livros registrados...");
    }

    private void listarAutoresRegistrados() {
        System.out.println("Listando livros por autores registrados...");
    }

    private void listarAutoresVivosPorAno(){
        System.out.println("Listando autores vivos em determinado ano...");
    }

    private  void listarLivrosPorIdioma(){
        System.out.println("Listando livros por idiomas...");
    }
}