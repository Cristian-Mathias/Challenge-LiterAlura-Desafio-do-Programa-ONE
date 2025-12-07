package br.com.desafio.literalura.catalogo_livros.repository;

import br.com.desafio.literalura.catalogo_livros.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN FETCH b.author")
    List<Book> findAllWithAuthors();
    Long countByLanguage(String language);
    @Query("SELECT b FROM Book b JOIN FETCH b.author WHERE b.language = :language")
    List<Book> findByLanguageFetchAuthor(@Param("language") String language);
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.author ORDER BY b.downloadCount DESC")
    List<Book> findTop10MostDownloaded(Pageable pageable);
}
