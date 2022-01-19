package com.pad.server.base.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pad.server.base.exceptions.PADException;

public class SecurityUtil {

    private static ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(256);

    private static final String       FORMATTER1_PATTERN = "yyyy.MM.dd.hh.ss";
    private static final String       FORMATTER2_PATTERN = "yyyy MM dd hh:ss";
    private static final String       FORMATTER3_PATTERN = "yyyy/MM-dd-hh ss";

    public static String getSystemUsername() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        return authentication.getName();
    }

    public static String generateDateBasedToken1(String username, Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat(FORMATTER1_PATTERN);
        SimpleDateFormat formatter2 = new SimpleDateFormat(FORMATTER2_PATTERN);
        SimpleDateFormat formatter3 = new SimpleDateFormat(FORMATTER3_PATTERN);

        StringBuilder sb = new StringBuilder();
        sb.append(ServerConstants.SYSTEM_TOKEN_PREFIX).append(".").append(username).append(".").append(formatter.format(date)).append(".");
        sb.append(formatter2.format(date)).append("-").append(formatter3.format(date)).append(".").append(ServerConstants.SYSTEM_TOKEN_SUFFIX1);

        return DigestUtils.sha256Hex(sb.toString());
    }

    public static String generateDateBasedToken2(String username, Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat(FORMATTER1_PATTERN);
        SimpleDateFormat formatter2 = new SimpleDateFormat(FORMATTER2_PATTERN);
        SimpleDateFormat formatter3 = new SimpleDateFormat(FORMATTER3_PATTERN);

        StringBuilder sb = new StringBuilder();
        sb.append(ServerConstants.SYSTEM_TOKEN_PREFIX).append("..").append(username).append("..").append(formatter3.format(date)).append("-");
        sb.append(formatter2.format(date)).append("..").append(formatter.format(date)).append("..").append(ServerConstants.SYSTEM_TOKEN_SUFFIX2);

        return DigestUtils.sha256Hex(sb.toString());
    }

    public static String generateUniqueCode() {

        return shaPasswordEncoder.encodePassword(UUID.randomUUID().toString().replaceAll("-", ""), System.currentTimeMillis());

    }

    public static void validateApiRemoteAddr(String allowedIpOrRangeList) throws PADException {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String remoteAddress = request.getRemoteAddr();

        if (remoteAddress.equalsIgnoreCase("0:0:0:0:0:0:0:1"))
            return;

        boolean isAllowed = Boolean.FALSE;
        for (String allowedIpOrRange : allowedIpOrRangeList.split(",")) {
            try {
                IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(allowedIpOrRange);
                if (ipAddressMatcher.matches(remoteAddress)) {
                    isAllowed = Boolean.TRUE;
                    break;
                }
            } catch (Exception e) {
                throw new PADException(ServerResponseConstants.ACCESS_DENIED_CODE, ServerResponseConstants.ACCESS_DENIED_TEXT, ".1");
            }
        }

        if (!isAllowed)
            throw new PADException(ServerResponseConstants.ACCESS_DENIED_CODE, ServerResponseConstants.ACCESS_DENIED_TEXT, ".2");
    }

}
