package com.example.reporter.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String heading;
    private String shortDescription;
    private Date date;
    private String source;
    private String link;
    private String description;
    private String text;
    private String caregory;

    public Article(String heading, String shortDescription, Date date, String source, String link, String description, String text, String caregory) {
        this.heading = heading;
        this.shortDescription = shortDescription;
        this.date = date;
        this.source = source;
        this.link = link;
        this.description = description;
        this.text = text;
        this.caregory = caregory;
    }

    public Article() {
    }

    public String getHeading() {
        return heading;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Date getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public String getCaregory() {
        return caregory;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCaregory(String caregory) {
        this.caregory = caregory;
    }
}
