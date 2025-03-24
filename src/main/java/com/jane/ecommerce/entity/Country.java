package com.jane.ecommerce.entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "country")
public class Country {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name= "code")
    private  String code;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "country")
    private List<State> states;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

}
