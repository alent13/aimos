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
