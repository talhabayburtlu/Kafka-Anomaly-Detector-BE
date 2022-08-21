package com.classified.seller.commons.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Destination")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, length = 1024)
    private String name;

    @ManyToMany
    @JoinTable(name = "destionation_targets",
            joinColumns = @JoinColumn(name = "target_id") ,
            inverseJoinColumns = @JoinColumn(name = "destination_id"))
    private List<Target> targets;

}
