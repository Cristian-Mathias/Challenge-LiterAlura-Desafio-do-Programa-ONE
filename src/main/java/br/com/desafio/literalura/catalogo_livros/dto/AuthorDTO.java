package br.com.desafio.literalura.catalogo_livros.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorDTO {
    private String name;
    @JsonProperty("birth_year")
    private Integer birthyear;
    @JsonProperty("death_year")
    private Integer deathyear;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
