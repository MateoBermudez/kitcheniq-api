package com.uni.kitcheniq.model;

import com.uni.kitcheniq.enums.EmployeeType;
import jakarta.persistence.*;

@Entity
@Table(name="Employee")
public class Employee {

    @Id
    @Column(name="Id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name="Name", nullable = false)
    private String Name;

    @Column(name="Type", nullable = false)
    EmployeeType Type;

    public Employee() {
        // Default constructor
    }

    public Employee(Long id, String name, EmployeeType type) {
        Id = id;
        Name = name;
        Type = type;
    }
}
