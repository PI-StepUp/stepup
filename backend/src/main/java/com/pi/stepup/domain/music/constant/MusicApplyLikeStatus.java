package com.pi.stepup.domain.music.constant;

public enum MusicApplyLikeStatus {
    CAN_HEART(1), CANNOT_HEART(0);

    Integer heartStatus;

    MusicApplyLikeStatus(int heartStatus) {
        this.heartStatus = heartStatus;
    }
    public int getHeartStatus() {
        return this.heartStatus;
    }
}
