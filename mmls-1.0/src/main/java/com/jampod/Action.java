package com.jampod;

public class Action {
    private String type;
    private String parameters;
    public Action(String type, String parameters) {
        this.type = type;
        this.parameters = parameters;
    }
    public String getParameters() {
        return parameters;
    }
    public String getType() {
        return type;
    }
}
