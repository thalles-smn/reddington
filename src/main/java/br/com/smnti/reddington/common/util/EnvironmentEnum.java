package br.com.smnti.reddington.common.util;

public enum EnvironmentEnum {

    DEVELOPMENT("development"),
    TEST("test"),
    STAGING("staging"),
    PRODUCTION("production");

    private final String text;

    EnvironmentEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
