package com.example.reporter.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String heading;
    private String shortDescription;
    private Date date;
    private String source;
    private String link;
    @Length(max = 1000000)
    private String description;
    @Length(max = 1000000)
    private String text;
    private String category;
    private String imageUrl;

    public Article(String heading, String shortDescription, Date date, String source, String link, String description, String text, String category, String imageUrl) {
        this.heading = heading;
        this.shortDescription = shortDescription;
        this.date = date;
        this.source = source;
        this.link = link;
        this.description = description;
        this.text = text;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public Article() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCategory() {
        return category;
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

    public void setCategory(String caregory) {
        this.category = caregory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", heading='" + heading + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", date=" + date +
                ", source='" + source + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", category='" + category + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
