package com.pad.server.base.tasks;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.tasks.SystemStatusCheckTimerTask.Data;

public class SystemStatusCheckTaskExecutor implements Runnable {

    private static final Logger logger = Logger.getLogger(SystemStatusCheckTaskExecutor.class);

    private JdbcTemplate        jdbcTemplate;
    private long                id;
    private String              name;
    private String              query;
    private String              queryParam;
    private String              configParam;
    private int                 comparisonOperator;
    private int                 comparisonValue;
    private Data                data;

    public SystemStatusCheckTaskExecutor(JdbcTemplate jdbcTemplate, long id, String name, String query, String queryParam, String configParam, int comparisonOperator,
        int comparisonValue, Data data) {
        this.jdbcTemplate = jdbcTemplate;
        this.id = id;
        this.name = name;
        this.query = query;
        this.queryParam = queryParam;
        this.configParam = configParam;
        this.comparisonOperator = comparisonOperator;
        this.comparisonValue = comparisonValue;
        this.data = data;
    }

    @Override
    public void run() {

        callServlet();
        processData();
    }

    private void callServlet() {

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            int configParamArray[] = { 30000, 30000, 30000 };

            if (StringUtils.isNotBlank(configParam)) {
                String configParamArrayString[] = configParam.split(",");
                for (int i = 0; i < configParamArrayString.length; i++) {
                    try {
                        configParamArray[i] = Integer.parseInt(configParamArrayString[i]);
                    } catch (NumberFormatException nfe) {

                    }
                }
            }

            if (StringUtils.isNotBlank(queryParam)) {
                queryParam = URLEncoder.encode(queryParam, StandardCharsets.UTF_8.name());
            }

            HttpGet httpGet = new HttpGet(query + "?" + queryParam);

            httpClient = HttpClients.createDefault();

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(configParamArray[0]).setConnectTimeout(configParamArray[1])
                .setConnectionRequestTimeout(configParamArray[2]).build();
            httpGet.setConfig(requestConfig);

            data.startTime = System.currentTimeMillis();
            httpResponse = httpClient.execute(httpGet);
            data.finishTime = System.currentTimeMillis();

            data.queryResult = httpResponse.getStatusLine().getStatusCode();

        } catch (ConnectException ce) {
            data.finishTime = System.currentTimeMillis();
            if (ce.getMessage().toLowerCase().contains("refused")) {
                data.queryResult = 1001;
            } else {
                data.queryResult = 1002;
            }
            logger.error("callServlet#id=" + id + ", name=" + name + "###ConnectException: ", ce);

        } catch (SocketException se) {
            data.finishTime = System.currentTimeMillis();
            if (se.getMessage().toLowerCase().contains("reset")) {
                data.queryResult = 1003;
            } else {
                data.queryResult = 1004;
            }
            logger.error("callServlet#id=" + id + ", name=" + name + "###SocketException: ", se);

        } catch (SocketTimeoutException ste) {
            data.finishTime = System.currentTimeMillis();
            data.queryResult = 1005;
            logger.error("callServlet#id=" + id + ", name=" + name + "###SocketTimeoutException: ", ste);

        } catch (ConnectTimeoutException cte) {
            data.finishTime = System.currentTimeMillis();
            data.queryResult = 1006;
            logger.error("callServlet#id=" + id + ", name=" + name + "###ConnectTimeoutException: ", cte);

        } catch (Exception e) {
            data.finishTime = System.currentTimeMillis();
            data.queryResult = 1000;
            logger.error("callServlet#id=" + id + ", name=" + name + "###Exception: ", e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }

                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                logger.error("callServlet#id=" + id + ", name=" + name + "###Exception[finally]: ", e);
            }
        }
    }

    private void processData() {

        if (data.queryResult == ServerConstants.DEFAULT_INT) {
            data.comparisonResult = true;
        } else {
            switch (comparisonOperator) {
                case ServerConstants.COMP_OP_EQUAL_TO:
                    data.comparisonResult = (data.queryResult == comparisonValue);
                    break;

                case ServerConstants.COMP_OP_LESS_THAN:
                    data.comparisonResult = (data.queryResult < comparisonValue);
                    break;

                case ServerConstants.COMP_OP_GREATER_THAN:
                    data.comparisonResult = (data.queryResult > comparisonValue);
                    break;

                case ServerConstants.COMP_OP_LESS_THAN_EQUAL_TO:
                    data.comparisonResult = (data.queryResult <= comparisonValue);
                    break;

                case ServerConstants.COMP_OP_GREATER_THAN_EQUAL_TO:
                    data.comparisonResult = (data.queryResult >= comparisonValue);
                    break;

                case ServerConstants.COMP_OP_NOT_EQUAL_TO:
                    data.comparisonResult = (data.queryResult != comparisonValue);
                    break;
            }
        }

        if (data.finishTime > 0) {
            data.queryExecutionTime = data.finishTime - data.startTime;
        }

        logger.info("run#finish#id=" + id + ", name=" + name + ", queryResult=" + data.queryResult + ", comparisonResult=" + data.comparisonResult + ", queryExecutionTime="
            + data.queryExecutionTime);

        jdbcTemplate.update("UPDATE system_checks SET query_result = ?, query_execution_time = ?, comparison_result = ?, date_edited = ? WHERE id = ?", data.queryResult,
            data.queryExecutionTime, data.comparisonResult, new Date(), id);
    }
}
