package com.pad.server.api.services.api;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerResponseConstants;
import com.pad.server.base.entities.ApiRemoteAddr;
import com.pad.server.base.entities.Operator;
import com.pad.server.base.exceptions.PADException;

@Service
public class ApiServiceImpl implements ApiService {

    private static final Logger logger = Logger.getLogger(ApiServiceImpl.class);

    @Autowired
    private HibernateTemplate   hibernateTemplate;

    @Override
    public Operator validateApiRequest(String authHeader, String remoteAddr) throws PADException, Exception {

        if (StringUtils.isBlank(authHeader) || authHeader.length() < 8)
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#1");

        authHeader = authHeader.substring(6, authHeader.length());

        authHeader = new String(Base64.decode(authHeader.getBytes("UTF-8")), "UTF-8");

        if (authHeader.indexOf(":") == -1 || authHeader.length() <= authHeader.indexOf(":"))
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "#2");

        String username = authHeader.substring(0, authHeader.indexOf(":"));
        String password = authHeader.substring(authHeader.indexOf(":") + 1);

        Operator operator = getValidApiOperator(username, password);

        validateApiRemoteAddr(operator.getId(), remoteAddr);

        return operator;
    }

    @SuppressWarnings("unchecked")
    private Operator getApiOperator(String username) {

        Operator apiOperator = null;
        try {

            String[] paramNames = { "username", "roleId" };
            Object[] paramValues = { username, ServerConstants.OPERATOR_ROLE_API };

            List<Operator> apiOperatorList = (List<Operator>) hibernateTemplate.findByNamedParam("FROM Operator WHERE username = :username AND roleId = :roleId", paramNames,
                paramValues);

            if (apiOperatorList != null && !apiOperatorList.isEmpty()) {
                apiOperator = apiOperatorList.get(0);
            }
        } catch (Exception e) {
            logger.error("getApiOperator###Exception: ", e);
        }

        return apiOperator;
    }

    private Operator getValidApiOperator(String username, String password) throws PADException {

        if (StringUtils.isBlank(username))
            throw new PADException(ServerResponseConstants.MISSING_USERNAME_CODE, ServerResponseConstants.MISSING_USERNAME_TEXT, "getValidApiUser#1");

        if (StringUtils.isBlank(password))
            throw new PADException(ServerResponseConstants.MISSING_PASSWORD_CODE, ServerResponseConstants.MISSING_PASSWORD_TEXT, "getValidApiUser#2");

        Operator apiOperator = getApiOperator(username);
        if (apiOperator == null)
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "getValidApiUser#3");
        else if (!apiOperator.getIsActive())
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "getValidApiUser#4");
        else if (!apiOperator.getPassword().equals(password))
            throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "getValidApiUser#5");

        return apiOperator;
    }

    @SuppressWarnings("unchecked")
    private List<ApiRemoteAddr> getApiRemoteAddr(long operatorId) {

        List<ApiRemoteAddr> apiRemoteAddrs = null;
        try {

            apiRemoteAddrs = (List<ApiRemoteAddr>) hibernateTemplate.findByNamedParam("FROM ApiRemoteAddr WHERE operatorId = :operatorId AND isAllowed = 1", "operatorId",
                operatorId);

        } catch (Exception e) {
            logger.error("getApiRemoteAddr###Exception: ", e);
        }

        return apiRemoteAddrs;
    }

    private boolean isApiRemoteAddrInRange(String apiRemoteAddrRangeList, String remoteAddr) {

        boolean isInRange = false;

        for (String apiRemoteAddrFromList : apiRemoteAddrRangeList.split(",")) {
            try {
                SubnetUtils subnetUtils = new SubnetUtils(apiRemoteAddrFromList);
                subnetUtils.setInclusiveHostCount(true);

                if (subnetUtils.getInfo().isInRange(remoteAddr)) {
                    isInRange = true;
                    break;
                }
            } catch (Exception e) {
                isInRange = false;
            }
        }

        return isInRange;
    }

    private void validateApiRemoteAddr(long operatorId, String remoteAddr) throws PADException {

        if (remoteAddr.equals("127.0.0.1") || remoteAddr.equals("0:0:0:0:0:0:0:1"))
            return;
        else {

            List<ApiRemoteAddr> apiRemoteAddrs = getApiRemoteAddr(operatorId);

            if (apiRemoteAddrs == null || apiRemoteAddrs.isEmpty())
                throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT, "validateApiRemoteAddr#1");
            else {
                boolean isInRange = false;

                for (ApiRemoteAddr apiRemoteAddr : apiRemoteAddrs) {
                    if (isApiRemoteAddrInRange(apiRemoteAddr.getRemoteAddr(), remoteAddr)) {
                        isInRange = true;
                        break;
                    }
                }

                if (!isInRange)
                    throw new PADException(ServerResponseConstants.API_AUTHENTICATION_FAILURE_CODE, ServerResponseConstants.API_AUTHENTICATION_FAILURE_TEXT,
                        "validateApiRemoteAddr#2");
            }
        }
    }

}
