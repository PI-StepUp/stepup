package com.pi.stepup.domain.board.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("MEETING")
public class Meeting extends Board{
    private String region;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private int commentCnt;


}
