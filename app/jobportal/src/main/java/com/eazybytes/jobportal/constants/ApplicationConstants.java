package com.eazybytes.jobportal.constants;

public class ApplicationConstants {

    private ApplicationConstants() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static final String JWT_SECRET_KEY = "JWT_SECRET";
    public static final String JWT_SECRET_DEFAULT_VALUE = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
    public static final String JWT_HEADER = "Authorization";

    public static final String Default_CSRF_HEADER_NAME = "X-CSRF-TOKEN";
    public static final String Default_CSRF_PARAM_NAME = "_csrf";
    public static final String Default_CSRF_COOKIE_NAME = "XSRF-TOKEN";
    public static final String ROLE_JOB_SEEKER = "ROLE_JOB_SEEKER";

}
