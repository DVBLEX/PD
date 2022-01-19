package com.pad.server.base.jsonentities.api;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author rafael
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DayOfWeekJson {

    private int     id;
    private String  label;
    private String  dateString;
    private boolean isAllowUpdate;

    public DayOfWeekJson() {
        super();
    }

    public DayOfWeekJson(int id, String label, String dateString, boolean isAllowUpdate) {
        super();
        this.id = id;
        this.label = label;
        this.dateString = dateString;
        this.isAllowUpdate = isAllowUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public boolean getIsAllowUpdate() {
        return isAllowUpdate;
    }

    public void setIsAllowUpdate(boolean isAllowUpdate) {
        this.isAllowUpdate = isAllowUpdate;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DayOfWeekJson [id=");
        builder.append(id);
        builder.append(", label=");
        builder.append(label);
        builder.append(", dateString=");
        builder.append(dateString);
        builder.append(", isAllowUpdate=");
        builder.append(isAllowUpdate);
        builder.append("]");
        return builder.toString();
    }

}
