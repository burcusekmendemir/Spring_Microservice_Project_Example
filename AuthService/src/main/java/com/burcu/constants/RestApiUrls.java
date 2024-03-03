package com.burcu.constants;

public class RestApiUrls {



    private static final String VERSION ="/v1";
    private static final String DEV="/dev";

    private static final String ROOT= DEV + VERSION;
    public static final String AUTH = ROOT + "/auth";


    public static final String ADD= "/add";
    public static final String FIND_ALL= "/find-all";
    public static final String FIND_BY_ID= "/find-by-id";
    public static final String DELETE_BY_TOKEN= "/delete-by-token";
    public static final String REGISTER_RABBITMQ ="/register-rabbitmq"; ;
    public static final String REGISTER= "/register";
    public static final String LOGIN= "/login";
    public static final String UPDATE= "/update";
    public static final String ACTIVATE_STATUS = "/activate-status" ;

    public static final String UPDATE_EMAIL= "/update-email";
}
