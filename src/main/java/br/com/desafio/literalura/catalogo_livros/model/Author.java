package br.com.desafio.literalura.catalogo_livros.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_name", nullable = false)
    private String name;
    private Integer birthyear;
    private Integer deathyear;

    public Author(){}

    public Author(String name, Integer birthyear, Integer deathyear) {
        this.name = name;
        this.birthyear = birthyear;
        this.deathyear = deathyear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(Integer birthyear) {
        this.birthyear = birthyear;
    }

    public Integer getDeathyear() {
        return deathyear;
    }

    public void setDeathyear(Integer deathyear) {
        this.deathyear = deathyear;
    }
}
