package br.com.desafio.literalura.catalogo_livros.repository;

import br.com.desafio.literalura.catalogo_livros.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    Optional<Author> findByName(String name);

    @Query("SELECT a FROM Author a WHERE a.birthyear <= :year AND (a.deathyear IS NULL OR a.deathyear >= :year)")
    List<Author> findLivingAuthorsByYear(@Param("year") Integer year);

    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Author> findByNameAuthor(@Param("name") String name);
}
