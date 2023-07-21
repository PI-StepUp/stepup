package com.pi.stepup.domain.board.domain;

import com.pi.stepup.domain.dance.domain.RandomDance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@DiscriminatorValue("NOTICE")
public class Notice extends Board{

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RANDOM_DANCE_ID")
    private RandomDance randomDance;

}
