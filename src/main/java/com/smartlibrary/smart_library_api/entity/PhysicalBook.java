package com.smartlibrary.smart_library_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_physical_books")
public class PhysicalBook extends Book {

    private String shelfLocation;

    public String getShelfLocation() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }
}