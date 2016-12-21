package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "d_language")
public class Language {

    public static final String RU = "ru";
    public static final String EN = "en";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lang")
    private String lang;

    public Language() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Language)) return false;

        Language language = (Language) o;

        if (id != null ? !id.equals(language.id) : language.id != null) return false;
        return lang != null ? lang.equals(language.lang) : language.lang == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        return result;
    }

    public Language(String lang) {
        this.lang = lang;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
