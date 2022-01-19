package com.pad.server.web.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.pad.server.base.common.ServerConstants;

public class MyUserDetails implements UserDetails {

    private static final long   serialVersionUID = 1L;

    private static final ZoneId currentZone      = ZoneId.systemDefault();

    private long                id;
    private String              code;
    private long                accountId;
    private long                portOperatorId;
    private long                independentPortOperatorId;
    private String              username;
    private String              firstname;
    private String              lastname;
    private String              password;
    private boolean             isEnabled;
    private boolean             isLocked;
    private int                 loginFailureCount;
    private int                 role;
    private Date                dateLastPassword;
    private Date                dateLocked;
    private boolean             isCredentialsExpired;
    private long                accountNumber;
    private int                 accountStatus;
    private int                 accountType;
    private int                 accountPaymentTermsType;
    private int                 loginLockHours;
    private int                 passwordValidDays;
    private long                languageId;

    public MyUserDetails(long id, String code, long accountId, long portOperatorId, long independentPortOperatorId, String firstname, String lastname, String username, String password, boolean enabled,
        boolean isLocked, int loginFailureCount, int role, Date dateLastPassword, boolean isCredentialsExpired, Date dateLocked, long accountNumber, int accountStatus,
        int accountType, int accountPaymentTermsType, int loginLockHours, int passwordValidDays, long languageId) {

        this.id = id;
        this.code = code;
        this.accountId = accountId;
        this.portOperatorId = portOperatorId;
        this.independentPortOperatorId = independentPortOperatorId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.isEnabled = enabled;
        this.isLocked = isLocked;
        this.loginFailureCount = loginFailureCount;
        this.role = role;
        this.dateLastPassword = dateLastPassword;
        this.isCredentialsExpired = isCredentialsExpired;
        this.dateLocked = dateLocked;
        this.accountNumber = accountNumber;
        this.accountStatus = accountStatus;
        this.accountType = accountType;
        this.accountPaymentTermsType = accountPaymentTermsType;
        this.loginLockHours = loginLockHours;
        this.passwordValidDays = passwordValidDays;
        this.languageId = languageId;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(ServerConstants.SPRING_SECURITY_ROLE_PREFIX + String.valueOf(role)));
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !getIsLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {

        if (!this.isCredentialsExpired) {

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastPasswordDate = LocalDateTime.ofInstant(this.dateLastPassword.toInstant(), currentZone);

            return lastPasswordDate.isAfter(now.minusDays(passwordValidDays));

        } else
            return false;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean getIsLocked() {

        if (isLocked) {

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lockDate = LocalDateTime.ofInstant(this.dateLocked.toInstant(), currentZone);

            return lockDate.isAfter(now.minusHours(loginLockHours));

        } else
            return false;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getLoginFailureCount() {
        return loginFailureCount;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getPortOperatorId() {
        return portOperatorId;
    }

    public long getIndependentPortOperatorId() {
        return independentPortOperatorId;
    }

    public int getRole() {
        return this.role;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public int getAccountType() {
        return accountType;
    }

    public int getAccountPaymentTermsType() {
        return accountPaymentTermsType;
    }

    public boolean isAdmin() {
        return this.role == ServerConstants.OPERATOR_ROLE_ADMIN;
    }

    public long getLanguageId() {
        return this.languageId;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MyUserDetails other = (MyUserDetails) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}
