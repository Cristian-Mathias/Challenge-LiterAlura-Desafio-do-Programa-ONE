package br.com.desafio.literalura.catalogo_livros.repository;

import br.com.desafio.literalura.catalogo_livros.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN FETCH b.author")
    List<Book> findAllWithAuthors();
    Long countByLanguage(String language);
    @Query("SELECT b FROM Book b JOIN FETCH b.author WHERE b.language = :language")
    List<Book> findByLanguageFetchAuthor(@Param("language") String language);
}
