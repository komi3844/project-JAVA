package com.mmtr.finance.parser.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "currencies")
public class Currency {
    public Currency(String code, float exchangeRate,Date tmstmp){
        this.code = code;
        this.exchangeRate = exchangeRate;
        this.tmstmp = tmstmp;
    }
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "exchange_rate", nullable = false)
    private float exchangeRate;

    @Column(name = "tmstmp", nullable = false)
    private Date tmstmp;
}
