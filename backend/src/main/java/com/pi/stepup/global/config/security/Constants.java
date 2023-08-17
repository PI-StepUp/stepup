package com.pi.stepup.global.config.security;

public final class Constants {

    public static final String[] PostPermitArray
        = new String[]{
        "/api/user",
        "/api/user/((?!auth|checkpw).)+"
    };

    public static final String[] GetPermitArray
        = new String[]{
        "/api/user/country",
        "/api/dance/playlist/\\d+",
        "/api/dance\\?([^&]+)&?([^&]*)$",
        "/api/board/[A-Za-z]+(\\?([^&]*))?$",
        "/api/board/notice/\\d+",
        "/api/music/apply(\\?([^&]*))?$"
    };

    //POST, PUT, DELETE
    public static final String[] AdminPermitArray
        = new String[]{
        "/api/user/statistics/country",
        "/api/music(\\/\\d+)?$",
        "/api/board/notice(\\/\\d+)?$"
    };
}
