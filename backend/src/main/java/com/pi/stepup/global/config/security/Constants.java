package com.pi.stepup.global.config.security;

public final class Constants {

    public static final String[] PostPermitArray
        = new String[]{
        "/api/user",
        //"/api/user/auth와 checkpw를 제외한 모든 문자열"인 경우
        "/api/user/((?!auth|checkpw).)+"
    };

    public static final String[] GetPermitArray
        = new String[]{
        "/api/user/country",
        "/api/dance/playlist/\\d+",
        //"/api/dance?key=value"과 "/api/dance?key=value&key=value"인 경우
        "/api/dance\\?([^&]+)&?([^&]*)$",
        //"/api/board/문자열"과 "/api/board/문자열?key=value"인 경우
        "/api/board/[A-Za-z]+(\\?([^&]*))?$",
        //"/api/board/notice/숫자"인 경우
        "/api/board/notice/\\d+",
        //"/api/music/apply"와 "/api/music/apply?key=value"인 경우
        "/api/music/apply(\\?([^&]*))?"
    };

    //POST, PUT, DELETE
    public static final String[] AdminPermitArray
        = new String[]{
        //"/api/music"와 "/api/music/숫자"인 경우
        "/api/music(/\\d)?",
        //"/api/board/notice"와 "/api/board/notice/숫자"인 경우
        "/api/board/notice(/\\d)?"
    };
}
