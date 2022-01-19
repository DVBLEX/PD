package com.pad.server.base.common;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.pad.server.base.exceptions.PADException;

public class MessageFormatterUtil {

    private static final Logger logger = Logger.getLogger(MessageFormatterUtil.class);

    public static String formatText(String regex, String textTemplate, HashMap<String, Object> params) throws PADException {

        final StringBuffer sb = new StringBuffer();
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(textTemplate);

        try {
            while (matcher.find()) {

                String expressionVariable = matcher.group(1);
                Object variableValue = params.get(expressionVariable);

                if (variableValue == null) {
                    logger.error("formatText###Exception: Parameter " + expressionVariable + " not found.");
                    continue;
                }

                matcher.appendReplacement(sb, variableValue.toString());
            }
            matcher.appendTail(sb);

        } catch (Exception e) {

            logger.error("MessageFormatterUtil##formatText##Exception: ", e);

            throw new PADException(ServerResponseConstants.API_FAILURE_CODE, ServerResponseConstants.API_FAILURE_TEXT, "");
        }

        return sb.toString();
    }

    public static String formatText(String textTemplate, HashMap<String, Object> params) throws PADException {
        return formatText(ServerConstants.REGEX_MESSAGE_FORMAT, textTemplate, params);
    }

}
