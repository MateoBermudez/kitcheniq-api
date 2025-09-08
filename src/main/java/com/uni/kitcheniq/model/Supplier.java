package com.uni.kitcheniq.model;

import jakarta.persistence.*;

@Entity
@Table(name="Supplier")
public class Supplier {

    @Id
    @Column(name="Id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name="Name", nullable = false)
    private String Name;

    @Column(name="ContactInfo", nullable = false)
    private String ContactInfo;

    public Supplier() {
        // Default constructor
    }

    public Supplier(Long id, String name, String contactInfo) {
        Id = id;
        Name = name;
        ContactInfo = contactInfo;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContactInfo() {
        return ContactInfo;
    }

    public void setContactInfo(String contactInfo) {
        ContactInfo = contactInfo;
    }

}
