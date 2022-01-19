package com.pad.server.base.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pad.server.base.entities.Account;
import com.pad.server.base.exceptions.PADException;

public class PreparedJDBCQuery {

    public static int     QUERY_TYPE_COUNT  = 1;
    public static int     QUERY_TYPE_SELECT = 2;
    private StringBuilder query             = new StringBuilder();
    List<Object>          queryParameters   = new ArrayList<>();

    public Object[] getQueryParameters() {
        return queryParameters.toArray();
    }

    public void addQueryParameter(Object p) {
        this.queryParameters.add(p);
    }

    public String getQueryString() {
        return query.toString();
    }

    public StringBuilder append(Object o) {
        return query.append(o);
    }

    public void setSortParameters(String sortColumn, Boolean isSortAsc, String tableAlias, String defaultSortingField, String defaultSortMethodForBlankSortColumn) {
        if (defaultSortMethodForBlankSortColumn == null && isSortAsc != null) {
            defaultSortMethodForBlankSortColumn = isSortAsc ? "ASC" : "DESC";
        }
        if (StringUtils.isBlank(sortColumn)) {
            this.query.append(" ORDER BY ").append(tableAlias).append(".").append(defaultSortingField).append(" ").append(defaultSortMethodForBlankSortColumn);

        } else {
            query.append(" ORDER BY ").append(tableAlias).append(".").append(sortColumn).append(" ").append(isSortAsc ? "ASC" : "DESC");
        }
    }

    public void setLimitParameters(int currentPage, int pageCount) {
        if (currentPage != 0) {
            this.query.append(" LIMIT ")
                    .append(ServerUtil.getStartLimitPagination(currentPage, pageCount))
                    .append(", ")
                    .append(pageCount);
        }
    }

    public void setAccountParameters(Account account, String dateFromString, String dateToString) throws PADException {
        this.setAccountParameters(account, dateFromString);
        Date dateTo = ServerUtil.getDateFromStringWithShiftedDays(dateToString, 1, "#2");

        addQueryParameter(dateTo);
    }

    public void setAccountParameters(Account account, String dateFromString) throws PADException {
        Date dateFrom = ServerUtil.getDateFromStringWithShiftedDays(dateFromString, 0, "#1");
        addQueryParameter(account.getId());
        addQueryParameter(dateFrom);

    }
}
