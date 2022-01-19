package com.pad.server.web.services.padanpr;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.jsonentities.api.AnprParameterJson;
import com.pad.server.base.jsonentities.api.PortOperatorTransactionTypeJson;
import com.pad.server.base.jsonentities.api.SystemParamJson;

@Service
@Transactional
public class PadAnprServiceImpl implements PadAnprService {

    private static final Logger logger = Logger.getLogger(PadAnprServiceImpl.class);

    private ObjectMapper        mapper;

    @Value("${padanpr.api.url.local}")
    private String              padanprApiUrlLocal;

    @Value("${padanpr.update.anpr.parameter.url.endpoint}")
    private String              padanprUpdateAnprParameterUrlEndpoint;

    @Value("${padanpr.update.transaction.type.flag.url.endpoint}")
    private String              padanprUpdateTransactionTypeFlagUrlEndpoint;

    @Value("${padanpr.update.system.parameter.url.endpoint}")
    private String              padanprUpdateSystemParameterUrlEndpoint;

    @Value("${padanpr.api.client.id}")
    private String              padanprApiClientId;

    @Value("${padanpr.api.secret}")
    private String              padanprApiSecret;

    @Override
    public void updateAnprParameterPadanpr(AnprParameterJson anprParameterJson) throws PADException {

        String padanprUpdateAnprParameterUrl = padanprApiUrlLocal + padanprUpdateAnprParameterUrlEndpoint;

        try {
            anprParameterJson.setApiCientId(padanprApiClientId);
            anprParameterJson.setApiSecret(padanprApiSecret);

            String responseJson = callPadAnprServlet(padanprUpdateAnprParameterUrl, anprParameterJson, null, null);

            logger.info("updateAnprParameterPadanpr###" + padanprUpdateAnprParameterUrlEndpoint + "#Response: " + responseJson);

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("updateAnprParameterPadanpr###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "updateAnprParameterPadanpr#2");
        }

    }

    @Override
    public void updateTransactionTypeFlagPadanpr(PortOperatorTransactionTypeJson portOperatorTransactionTypeJson) throws PADException {

        String padanprUpdateTransactionTypeFlagUrl = padanprApiUrlLocal + padanprUpdateTransactionTypeFlagUrlEndpoint;

        try {
            portOperatorTransactionTypeJson.setApiClientId(padanprApiClientId);
            portOperatorTransactionTypeJson.setApiClientSecret(padanprApiSecret);

            String responseJson = callPadAnprServlet(padanprUpdateTransactionTypeFlagUrl, null, portOperatorTransactionTypeJson, null);

            logger.info("updateTransactionTypeFlagPadanpr###" + padanprUpdateTransactionTypeFlagUrlEndpoint + "#Response: " + responseJson);

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("updateTransactionTypeFlagPadanpr###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "updateTransactionTypeFlagPadanpr#2");
        }

    }

    @Override
    public void updateSystemParameterPadanpr(SystemParamJson systemParamJson) throws PADException {

        String padanprUpdateSystemParameterUrl = padanprApiUrlLocal + padanprUpdateSystemParameterUrlEndpoint;

        try {
            systemParamJson.setApiClientId(padanprApiClientId);
            systemParamJson.setApiClientSecret(padanprApiSecret);

            String responseJson = callPadAnprServlet(padanprUpdateSystemParameterUrl, null, null, systemParamJson);

            logger.info("updateSystemParameterPadanpr###" + padanprUpdateSystemParameterUrlEndpoint + "#Response: " + responseJson);

        } catch (PADException pade) {
            throw pade;

        } catch (Exception e) {
            logger.error("updateSystemParameterPadanpr###Exception: ", e);
            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "updateSystemParameterPadanpr#2");
        }

    }

    private String callPadAnprServlet(String url, AnprParameterJson anprParameterJson, PortOperatorTransactionTypeJson portOperatorTransactionTypeJson,
        SystemParamJson systemParamJson) throws PADException {

        String responseSource = "callPadAnprServlet#";

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        RequestConfig requestConfig = null;
        String responseText = null;
        try {
            HttpPost httpPost = new HttpPost(url);

            mapper = new ObjectMapper();

            if (url.equals(padanprApiUrlLocal + padanprUpdateAnprParameterUrlEndpoint) && anprParameterJson != null) {
                httpPost.setEntity(new StringEntity(mapper.writeValueAsString(anprParameterJson)));

            } else if (url.equals(padanprApiUrlLocal + padanprUpdateTransactionTypeFlagUrlEndpoint) && portOperatorTransactionTypeJson != null) {
                httpPost.setEntity(new StringEntity(mapper.writeValueAsString(portOperatorTransactionTypeJson)));

            } else if (url.equals(padanprApiUrlLocal + padanprUpdateSystemParameterUrlEndpoint) && systemParamJson != null) {
                httpPost.setEntity(new StringEntity(mapper.writeValueAsString(systemParamJson)));
            }

            httpPost.setHeader("Content-type", "application/json;charset=UTF-8");

            httpClient = HttpClients.createDefault();

            requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).setConnectionRequestTimeout(30000).build();

            httpPost.setConfig(requestConfig);

            httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                HttpEntity httpResponseEntity = httpResponse.getEntity();
                if (httpResponseEntity != null) {
                    ByteArrayOutputStream httpResponseByteArrayOutputStream = new ByteArrayOutputStream();
                    httpResponseEntity.writeTo(httpResponseByteArrayOutputStream);
                    responseText = httpResponseByteArrayOutputStream.toString();
                }
            } else {

                logger.info(responseSource + "#Response: [StatusCode=" + httpResponse.getStatusLine().getStatusCode() + ", ReasonPhrase="
                    + httpResponse.getStatusLine().getReasonPhrase() + "]");
                throw new PADException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, responseSource + "Response: [StatusCode="
                    + httpResponse.getStatusLine().getStatusCode() + ", ReasonPhrase=" + httpResponse.getStatusLine().getReasonPhrase() + "]", responseSource);
            }
        } catch (PADException pade) {
            throw pade;
        } catch (Exception e) {
            logger.error(responseSource + "###Exception: ", e);
            throw new PADException(ServerResponseConstants.EXTERNAL_API_CONNECTION_FAILURE_CODE, e.getClass() + "###" + e.getMessage(), responseSource + "##Exception");
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }

                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                logger.error(responseSource + "###finally#Exception: ", e);
            }
        }

        return responseText;
    }

}
