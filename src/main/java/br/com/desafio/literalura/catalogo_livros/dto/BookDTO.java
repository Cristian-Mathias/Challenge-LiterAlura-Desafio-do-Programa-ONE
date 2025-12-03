package br.com.desafio.literalura.catalogo_livros.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {
    private int id;
    private String title;
    private List<AuthorDTO> authors;
    private List<String> bookshelves;
    private List<String> languages;

    @JsonAlias("download_count")
    private int downloadCount;

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public List<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDTO> authors) {
        this.authors = authors;
    }

    public List<String> getBookshelves() {
        return bookshelves;
    }

    public void setBookshelves(List<String> bookshelves) {
        this.bookshelves = bookshelves;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "authors=" + authors +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", bookshelves=" + bookshelves +
                ", languages=" + languages +
                ", downloadCount=" + downloadCount +
                '}';
    }

}
