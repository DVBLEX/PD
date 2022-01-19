package com.pad.server.web.configuration;

public class TestData {

    public static final String ACCOUNT_STATEMENTS_REQUEST_JSON = "{\n" +
            "  \"accountCode\":\"33dec95c8788f4805a823fd93ec9bbc4008a60a67cafd045ec04dfdc4d1b53ee\",\n" +
            "  \"dateFromString\":\"01/01/2019\",\n" +
            "  \"sortColumn\":\"\",\n" +
            "  \"sortAsc\":true,\n" +
            "  \"currentPage\":1,\n" +
            "  \"pageCount\":20\n" +
            "}";

    public static final String ACCOUNT_LOW_AMOUNT_BALANCE_WARN_REQUEST_JSON = "{\n" +
        "  \"code\":\"33dec95c8788f4805a823fd93ec9bbc4008a60a67cafd045ec04dfdc4d1b53ee\",\n" +
        "  \"isSendLowAccountBalanceWarn\":\"true\",\n" +
        "  \"amountLowAccountBalanceWarn\":\"6000\"\n" +
        "}";

    public static final String ACCOUNT_EMPTY_LOW_AMOUNT_BALANCE_WARN_TRUE_REQUEST_JSON = "{\n" +
        "  \"code\":\"33dec95c8788f4805a823fd93ec9bbc4008a60a67cafd045ec04dfdc4d1b53ee\",\n" +
        "  \"isSendLowAccountBalanceWarn\":\"true\"" +
        "}";

    public static final String ACCOUNT_IS_LOW_AMOUNT_BALANCE_WARN_FALSE_REQUEST_JSON = "{\n" +
        "  \"code\":\"33dec95c8788f4805a823fd93ec9bbc4008a60a67cafd045ec04dfdc4d1b53ee\",\n" +
        "  \"isSendLowAccountBalanceWarn\":\"false\",\n" +
        "  \"amountLowAccountBalanceWarn\":\"6000\"\n" +
        "}";

    public static final String ACCOUNT_NEGATIVE_LOW_AMOUNT_BALANCE_WARN_REQUEST_JSON = "{\n" +
        "  \"code\":\"33dec95c8788f4805a823fd93ec9bbc4008a60a67cafd045ec04dfdc4d1b53ee\",\n" +
        "  \"isSendLowAccountBalanceWarn\":\"true\",\n" +
        "  \"amountLowAccountBalanceWarn\":\"-6000\"\n" +
        "}";

    public static final String EMPTY_JSON = "{}";

    public static final String SESSION_JSON = "{\"code\":\"55252ad8cad3ac7ea064c492d55ac2e7c97316dd93b0caf5c8fc0ca541b2bff0\"}";

    public static final String TRANSACTION_TYPE_FLAGS_JSON = "{\"portOperatorId\":1,\"transactionType\":1,\"isDirectToPort\":true,\"isAllowMultipleEntries\":false,\"missionCancelSystemAfterMinutes\":180,\"isTripCancelSystem\":false,\"tripSystemCancelAfterMinutes\":180,\"portOperatorGateId\":1004}";
    public static final String TRANSACTION_TYPE_FLAGS_WITHOUT_GATE_JSON = "{\"portOperatorId\":1,\"transactionType\":1,\"isDirectToPort\":true,\"isAllowMultipleEntries\":false,\"missionCancelSystemAfterMinutes\":180,\"isTripCancelSystem\":false,\"tripSystemCancelAfterMinutes\":180}";
}
