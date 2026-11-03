package com.smartexam.security.constant;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER = "Authorization";

    public static final String ROLE_PREFIX = "ROLE_";

    public static final long ACCESS_TOKEN_EXPIRATION = 3600000;

    public static final long REFRESH_TOKEN_EXPIRATION = 604800000;

}