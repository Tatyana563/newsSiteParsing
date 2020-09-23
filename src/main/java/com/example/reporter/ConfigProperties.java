package com.example.reporter;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration

@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "article")
public class ConfigProperties {
    private int chunk;
    private int poolSize;
    private int numberOfPAges;

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getNumberOfPAges() {
        return numberOfPAges;
    }

    public void setNumberOfPAges(int numberOfPAges) {
        this.numberOfPAges = numberOfPAges;
    }
}
