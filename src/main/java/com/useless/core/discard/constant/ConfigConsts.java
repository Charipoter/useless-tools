package com.useless.core.discard.constant;

public class ConfigConsts {

    public static final String REDIS_HOST = "localhost";
    public static final int REDIS_PORT = 6379;
    public static final String MYBATIS_CONFIG = "discard/dao/configuration.xml";

    public static final String[] FXMLS = new String[]{"login", "register", "catalogue", "calculator", "guessNumber",
            "perimeterAndArea"};

    private ConfigConsts() {}

}

