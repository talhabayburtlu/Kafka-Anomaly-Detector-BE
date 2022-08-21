package com.classified.seller.commons.entity;

import com.classified.seller.commons.enumeration.TargetTypeEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Target")
public class Target {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 1024)
    private String name;

    @Column(name = "value" , length = 1024)
    private String value;
    @Column(name = "type")
    private String type;

    public TargetTypeEnum getType() {
        return TargetTypeEnum.fromName(this.type);
    }
    public void setType(TargetTypeEnum type) {
        this.type = type.getName();
    }

    public static class TargetBuilder {
        public Target.TargetBuilder type(TargetTypeEnum type) {
            this.type = type.getName();
            return this;
        }
    }
}
