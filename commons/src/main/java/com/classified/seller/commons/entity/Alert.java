package com.classified.seller.commons.entity;

import com.classified.seller.commons.enumeration.AlertLevelEnum;
import com.classified.seller.commons.enumeration.AlertStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Alert")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lag")
    private Long lag;
    @Column(name = "status")
    private String status;

    @Column(name = "level")
    private String level;

    @Column(name = "message" , length = 1024)
    private String message;

    @Column(name = "topic", length = 1024)
    private String topic;

    @Column(name = "consumer_group", length = 1024)
    private String consumerGroup;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "processed")
    private Boolean processed;

    @ManyToOne
    @JoinColumn(name = "cluster_id", nullable = false)
    private Cluster cluster;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private Rule rule;

    @ManyToOne
    @JoinColumn(name = "regex_id")
    private Regex regex;

    public AlertStatusEnum getStatus() {
        return AlertStatusEnum.fromName(this.status);
    }
    public void setStatus(AlertStatusEnum status) {
        this.status = status.getName();
    }

    public AlertLevelEnum getLevel() {
        return AlertLevelEnum.fromName(this.level);
    }
    public void setLevel(AlertLevelEnum level) {
        this.level = level.getName();
    }

    public static class AlertBuilder {
        public AlertBuilder status(AlertStatusEnum status) {
            this.status = status.getName();
            return this;
        }

        public AlertBuilder level(AlertLevelEnum level) {
            this.level = level.getName();
            return this;
        }
    }
}
