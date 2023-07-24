package com.pi.stepup.domain.dance.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "USER_ID")
//    private User host;

    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY)
    private RandomDance randomDance;

    //1:1 양방향
    public void setRandomDanceAndSetThis(RandomDance randomDance) {
        this.randomDance = randomDance;
        randomDance.setReservationAndSetThis(this);
    }

}
