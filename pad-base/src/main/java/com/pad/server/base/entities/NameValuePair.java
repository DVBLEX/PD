package com.pad.server.base.entities;

import java.io.Serializable;

public class NameValuePair implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            name;
    private Object            value;

    public NameValuePair(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
