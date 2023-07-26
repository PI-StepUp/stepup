package com.pi.stepup.domain.rank.constant;

public enum PointType {
    FIRST_PRIZE(100), SECOND_PRIZE(50), THIRD_PRIZE(30),
    SUCCESS_MUSIC(5),
    OPEN_DANCE(50),
    PRACTICE_ROOM(3);

    private final Integer point;

    PointType(Integer point) {
        this.point = point;
    }

    public Integer getPoint() {
        return this.point;
    }
}
