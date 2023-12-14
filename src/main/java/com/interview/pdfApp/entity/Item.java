package com.interview.pdfApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue
     Long Id;
     String name;
     int amount;
    public Item(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}
