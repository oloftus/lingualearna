package com.lingualearna.web.security.ownership;

public class SecuredConfigAttributes {

    /**
     * Allow only the owner of the secured object (specified by type or ID as
     * first parameter & @SecuredType annotation) to call this method.
     */
    public static final String ALLOW_OWNER = "ALLOW_OWNER";
}
