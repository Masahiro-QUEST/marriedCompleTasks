package com.example.taskmanager.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tasks") 
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private String registerDate;
    private boolean completed;

    public Task(Long id, String title, String description, String registerDate, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.registerDate = registerDate;
        this.completed = completed;
    }
}
