package com.pad.server.web.security;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.pad.server.base.common.ServerConstants;
import com.pad.server.base.common.ServerUtil;
import com.pad.server.base.exceptions.PADException;
import com.pad.server.base.exceptions.PADValidationException;

public class MyUserDetailsService implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(MyUserDetailsService.class);

    @Autowired
    private JdbcTemplate        jdbcTemplate;
    private static final String queryString;

    static {

        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT");
        builder.append("     operators.id,");
        builder.append("     operators.code,");
        builder.append("     operators.account_id,");
        builder.append("     operators.port_operator_id,");
        builder.append("     operators.independent_port_operator_id,");
        builder.append("     operators.first_name,");
        builder.append("     operators.last_name,");
        builder.append("     operators.username,");
        builder.append("     operators.password,");
        builder.append("     operators.is_active,");
        builder.append("     operators.is_locked,");
        builder.append("     operators.login_failure_count,");
        builder.append("     operators.role_id,");
        builder.append("     operators.date_last_password,");
        builder.append("     operators.is_credentials_expired,");
        builder.append("     operators.date_locked,");
        builder.append("     operators.language_id,");
        builder.append("     system_parameters.login_lock_period,");
        builder.append("     system_parameters.login_password_valid_period,");
        builder.append("     accounts.number,");
        builder.append("     accounts.status,");
        builder.append("     accounts.type,");
        builder.append("     accounts.payment_terms_type");

        builder.append(" FROM operators");
        builder.append(" INNER JOIN system_parameters ON 1=1");
        builder.append(" LEFT JOIN accounts ON operators.account_id = accounts.id");

        builder.append(" WHERE");
        builder.append("    operators.is_deleted = 0 ");
        builder.append("    AND");
        builder.append("    operators.is_active = 1 ");
        builder.append("    AND");
        builder.append("    operators.role_id != ").append(ServerConstants.OPERATOR_ROLE_API);
        builder.append("    AND");
        builder.append("    operators.username = ? ");

        builder.append(" LIMIT 1");

        queryString = builder.toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

        int accountType = ServerConstants.DEFAULT_INT;

        try {
            accountType = Integer
                .parseInt((String) RequestContextHolder.getRequestAttributes().getAttribute(MyLoginParameterFilter.PARAMETER_NAME_ACCOUNT_TYPE, RequestAttributes.SCOPE_REQUEST));

        } catch (NumberFormatException nfe) {
            throw new UsernameNotFoundException("Invalid account type provided");
        }

        logger.info("loadUserByUsername#accountType=" + accountType + ", username=" + username);

        if (accountType != ServerConstants.ACCOUNT_TYPE_COMPANY && accountType != ServerConstants.ACCOUNT_TYPE_INDIVIDUAL && accountType != ServerConstants.ACCOUNT_TYPE_OPERATOR)
            throw new UsernameNotFoundException("Invalid account type provided");

        if (accountType == ServerConstants.ACCOUNT_TYPE_COMPANY
            && (username.length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_64 || !username.matches(ServerConstants.REGEX_EMAIL)))
            throw new UsernameNotFoundException("User with email not found.");

        else if (accountType == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL
            && (username.length() > ServerConstants.DEFAULT_VALIDATION_LENGTH_16 || !username.matches(ServerConstants.REGEX_MSISDN)))
            throw new UsernameNotFoundException("User with mobile number not found.");

        else if (accountType == ServerConstants.ACCOUNT_TYPE_OPERATOR && (username.matches(ServerConstants.REGEX_EMAIL) || username.matches(ServerConstants.REGEX_MSISDN)))
            throw new UsernameNotFoundException("Operator with username not found.");

        if (accountType == ServerConstants.ACCOUNT_TYPE_INDIVIDUAL) {

            try {
                username = ServerUtil.getValidNumber(username, "loadUserByUsername");

            } catch (PADException pe) {
                throw new UsernameNotFoundException("Invalid username");

            } catch (PADValidationException pve) {
                throw new UsernameNotFoundException("Invalid username");
            }
        }

        MyUserDetails myUserDetails = jdbcTemplate.query(queryString, new ResultSetExtractor<MyUserDetails>() {

            @Override
            public MyUserDetails extractData(ResultSet rs) throws SQLException, DataAccessException {
                MyUserDetails myUserDetails = null;
                if (rs.next()) {
                    myUserDetails = new MyUserDetails(rs.getLong("id"), rs.getString("code"), rs.getLong("account_id"),
                        rs.getLong("port_operator_id"), rs.getLong("independent_port_operator_id"), rs.getString("first_name"),
                        rs.getString("last_name"), rs.getString("username"), rs.getString("password"), rs.getBoolean("is_active"), rs.getBoolean("is_locked"),
                        rs.getInt("login_failure_count"), rs.getInt("role_id"), rs.getTimestamp("date_last_password"), rs.getBoolean("is_credentials_expired"),
                        rs.getTimestamp("date_locked"), rs.getLong("number"), rs.getInt("status"), rs.getInt("type"), rs.getInt("payment_terms_type"),
                        rs.getInt("login_lock_period"), rs.getInt("login_password_valid_period"), rs.getLong("language_id"));
                }
                return myUserDetails;
            }
        }, username);

        if (myUserDetails == null)
            throw new UsernameNotFoundException("User not found.");

        return myUserDetails;
    }
}
