package com.pad.server.anpr.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class GetEntryLogRequest {

    private String              apiSecret;
    private int                 pageSize;
    private int                 page;
    private String              filter;

    private List<NameValuePair> nameValuePairs;

    public GetEntryLogRequest(String apiSecret, int pageSize, int page, String filter) {
        super();
        this.apiSecret = apiSecret;
        this.pageSize = pageSize;
        this.page = page;
        this.filter = filter;

        generateNameValuePairs();
    }

    private void generateNameValuePairs() {

        this.nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("apiSecret", this.getApiSecret()));
        nameValuePairs.add(new BasicNameValuePair("pageSize", String.valueOf(this.getPageSize())));
        nameValuePairs.add(new BasicNameValuePair("page", String.valueOf(this.getPage())));
        nameValuePairs.add(new BasicNameValuePair("filter", this.getFilter()));
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    public void setNameValuePairs(List<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GetEntryLogRequest [apiSecret=");
        builder.append(apiSecret == null ? "null" : "xxxxxx-" + apiSecret.length());
        builder.append(", pageSize=");
        builder.append(pageSize);
        builder.append(", page=");
        builder.append(page);
        builder.append(", filter=");
        builder.append(filter);
        builder.append(", nameValuePairs=");
        builder.append(nameValuePairs);
        builder.append("]");
        return builder.toString();
    }

}
