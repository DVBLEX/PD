package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndependentPortOperatorJson {

    private String value;    // code. value is used for the name so the jquery autocomplete will work with name value pairs
    private String label;    // name. label is used for the name so the jquery autocomplete will work with name value pairs
    private String nameShort;

    public IndependentPortOperatorJson() {
    }

    public IndependentPortOperatorJson(String value, String label, String nameShort) {
        this.value = value;
        this.label = label;
        this.nameShort = nameShort;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IndependentPortOperatorJson [value=");
        builder.append(value);
        builder.append(", label=");
        builder.append(label);
        builder.append(", nameShort=");
        builder.append(nameShort);
        builder.append("]");
        return builder.toString();
    }

}
