package com.classified.seller.commons.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Cluster")
public class Cluster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 32, unique = true)
    private String name;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "env", length = 16)
    private String env;

    @Column(name = "address", length = 512, unique = true)
    private String address;

    @Column(name = "config" , length = 2048)
    private String config;

    @Column(name = "active")
    @ColumnDefault("false")
    private Boolean active;
}
