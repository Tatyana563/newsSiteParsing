package com.example.reporter.dto;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "article")
public class ArticleDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String heading;
    private String shortDescription;
    private String date;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArticleDto(String heading, String shortDescription, String date) {
        this.heading = heading;
        this.shortDescription = shortDescription;
        this.date = date;
    }

    public ArticleDto() {
    }
}