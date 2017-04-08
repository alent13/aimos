package com.applexis.aimos.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by applexis on 08.04.2017.
 */

@Entity
@Table(name = "d_plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name="cost", columnDefinition="Decimal(10,2) default '100.00'")
    private BigDecimal cost;

    @Column(name = "space")
    private Long space;

    public Plan() {
    }

    public Plan(String name, BigDecimal cost, Long space) {
        this.name = name;
        this.cost = cost;
        this.space = space;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Long getSpace() {
        return space;
    }

    public void setSpace(Long space) {
        this.space = space;
    }
}
