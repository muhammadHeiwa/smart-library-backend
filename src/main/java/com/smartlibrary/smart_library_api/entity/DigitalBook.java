package com.smartlibrary.smart_library_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_digital_books")
public class DigitalBook extends Book {

    private String fileUrl;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}