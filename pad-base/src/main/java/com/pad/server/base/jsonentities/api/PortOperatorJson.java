package com.pad.server.base.jsonentities.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortOperatorJson {

    private long                              id;
    private String                            name;
    private String                            nameShort;
    private boolean                           isActive;
    private List<IndependentPortOperatorJson> independentPortOperatorJsonList;

    public PortOperatorJson() {
    }

    public PortOperatorJson(long id, String name, String nameShort, boolean isActive) {
        this.id = id;
        this.name = name;
        this.nameShort = nameShort;
        this.isActive = isActive;
    }

    public PortOperatorJson(long id, String name, String nameShort, boolean isActive, List<IndependentPortOperatorJson> independentPortOperatorJsonList) {
        this.id = id;
        this.name = name;
        this.nameShort = nameShort;
        this.isActive = isActive;
        this.independentPortOperatorJsonList = independentPortOperatorJsonList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<IndependentPortOperatorJson> getIndependentPortOperatorJsonList() {
        return independentPortOperatorJsonList;
    }

    public void setIndependentPortOperatorJsonList(List<IndependentPortOperatorJson> independentPortOperatorJsonList) {
        this.independentPortOperatorJsonList = independentPortOperatorJsonList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PortOperatorJson [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", nameShort=");
        builder.append(nameShort);
        builder.append(", isActive=");
        builder.append(isActive);
        builder.append("]");
        return builder.toString();
    }

}
