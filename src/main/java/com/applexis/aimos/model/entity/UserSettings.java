package com.applexis.aimos.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_page_show")
    private ShowMode pageShow;

    @ManyToOne
    @JoinColumn(name = "id_email_show")
    private ShowMode emailShow;

    @ManyToOne
    @JoinColumn(name = "id_phone_show")
    private ShowMode phoneShow;

    @ManyToOne
    @JoinColumn(name = "id_about_me_show")
    private ShowMode aboutMeShow;

    @ManyToOne
    @JoinColumn(name = "id_lang")
    private Language lang;

    public UserSettings() {

    }

    public UserSettings(ShowMode pageShow, ShowMode emailShow, ShowMode phoneShow, ShowMode aboutMeShow, Language lang) {
        this();
        this.pageShow = pageShow;
        this.emailShow = emailShow;
        this.phoneShow = phoneShow;
        this.aboutMeShow = aboutMeShow;
        this.lang = lang;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShowMode getPageShow() {
        return pageShow;
    }

    public void setPageShow(ShowMode pageShow) {
        this.pageShow = pageShow;
    }

    public ShowMode getEmailShow() {
        return emailShow;
    }

    public void setEmailShow(ShowMode emailShow) {
        this.emailShow = emailShow;
    }

    public ShowMode getPhoneShow() {
        return phoneShow;
    }

    public void setPhoneShow(ShowMode phoneShow) {
        this.phoneShow = phoneShow;
    }

    public ShowMode getAboutMeShow() {
        return aboutMeShow;
    }

    public void setAboutMeShow(ShowMode aboutMeShow) {
        this.aboutMeShow = aboutMeShow;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }
}
