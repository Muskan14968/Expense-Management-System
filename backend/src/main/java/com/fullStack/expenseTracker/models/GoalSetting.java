package com.fullStack.expenseTracker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    @JsonIgnore
    private User user;

    private String name;
    private Double price;
    private Double amountSaved;
    private LocalDate targetDate;

    public GoalSetting(User user, String name, Double price, Double amountSaved, LocalDate targetDate) {
        this.user = user;
        this.name = name;
        this.price = price;
        this.amountSaved = amountSaved;
        this.targetDate = targetDate;
    }
}
