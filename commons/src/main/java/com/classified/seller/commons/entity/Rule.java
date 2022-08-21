package com.classified.seller.commons.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Rule" , uniqueConstraints= @UniqueConstraint(columnNames={"topic", "consumer_group"}))
@Cacheable
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lower_boundary")
    private Long lowerBoundary;

    @Column(name = "upper_boundary")
    private Long upperBoundary;

    @Column(name = "topic", length = 1024)
    private String topic;

    @Column(name = "consumer_group", length = 1024)
    private String consumerGroup;

    @ManyToOne
    @JoinColumn(name = "cluster_id", nullable = false)
    private Cluster cluster;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

}
