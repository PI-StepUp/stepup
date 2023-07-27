package com.pi.stepup.domain.rank.constant;

public enum RankResponseMessage {
    UPDATE_POINT_SUCCESS("포인트 적립 완료"),
    READ_POINT_SUCCESS("포인트 조회 완료"),
    READ_RANK_SUCCESS("사용자 등급 조회 완료"),
    READ_PONT_HISTORY_SUCCESS("포인트 적립 내역 조회 완료");

    private final String message;

    RankResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
