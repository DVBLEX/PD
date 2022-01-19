package com.pad.server.base.exceptions;

public class PADException extends Exception {

    private static final long serialVersionUID = 1L;

    private int               responseCode;
    private String            responseText;
    private String            responseSource;

    public PADException() {
        super();
    }

    public PADException(int responseCode, String responseText, String responseSource) {
        super(responseText);
        this.responseCode = responseCode;
        this.responseText = responseText;
        this.responseSource = responseSource;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public String getResponseSource() {
        return responseSource;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PADException [responseCode=");
        builder.append(responseCode);
        builder.append(", responseText=");
        builder.append(responseText);
        builder.append(", responseSource=");
        builder.append(responseSource);
        builder.append("]");
        return builder.toString();
    }

}
