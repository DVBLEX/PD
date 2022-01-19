package com.pad.server.anpr.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetEntryLogResponse {

    private boolean        success;
    private String         message;

    @JsonDeserialize(as = ArrayList.class, contentAs = EntryLog.class)
    private List<EntryLog> entrylog;

    private int            count;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<EntryLog> getEntrylog() {
        return entrylog;
    }

    public void setEntrylog(List<EntryLog> entrylog) {
        this.entrylog = entrylog;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
