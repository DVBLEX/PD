package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "operators")
public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long              id;

    @Column(name = "code")
    private String            code;

    @Column(name = "account_id")
    private long              accountId;

    @Column(name = "port_operator_id")
    private long              portOperatorId;

    @Column(name = "independent_port_operator_id")
    private long              independentPortOperatorId;

    @Column(name = "first_name")
    private String            firstName;

    @Column(name = "last_name")
    private String            lastName;

    @Column(name = "email")
    private String            email;

    @Column(name = "msisdn")
    private String            msisdn;

    @Column(name = "username")
    private String            username;

    @Column(name = "password")
    private String            password;

    @Column(name = "role_id")
    private int               roleId;

    @Column(name = "is_active")
    private boolean           isActive;

    @Column(name = "is_deleted")
    private boolean           isDeleted;

    @Column(name = "is_locked")
    private boolean           isLocked;

    @Column(name = "login_failure_count")
    private int               loginFailureCount;

    @Column(name = "date_locked")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateLocked;

    @Column(name = "date_last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateLastLogin;

    @Column(name = "date_last_attempt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateLastAttempt;

    @Column(name = "count_passwd_forgot_requests")
    private int               countPasswdForgotRequests;

    @Column(name = "date_last_passwd_forgot_request", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateLastPasswdForgotRequest;

    @Column(name = "date_password_forgot_reported", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date              datePasswordForgotReported;

    @Column(name = "date_last_password")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateLastPassword;

    @Column(name = "date_last_passwd_set_up")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateLastPasswdSetUp;

    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "language_id")
    private long              languageId;

    @Column(name = "is_credentials_expired")
    private boolean           isCredentialsExpired;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getPortOperatorId() {
        return portOperatorId;
    }

    public void setPortOperatorId(long portOperatorId) {
        this.portOperatorId = portOperatorId;
    }

    public long getIndependentPortOperatorId() {
        return independentPortOperatorId;
    }

    public void setIndependentPortOperatorId(long independentPortOperatorId) {
        this.independentPortOperatorId = independentPortOperatorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public int getLoginFailureCount() {
        return loginFailureCount;
    }

    public void setLoginFailureCount(int loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    public Date getDateLocked() {
        return dateLocked;
    }

    public void setDateLocked(Date dateLocked) {
        this.dateLocked = dateLocked;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public Date getDateLastLogin() {
        return dateLastLogin;
    }

    public void setDateLastLogin(Date dateLastLogin) {
        this.dateLastLogin = dateLastLogin;
    }

    public Date getDateLastAttempt() {
        return dateLastAttempt;
    }

    public void setDateLastAttempt(Date dateLastAttempt) {
        this.dateLastAttempt = dateLastAttempt;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    public int getCountPasswdForgotRequests() {
        return countPasswdForgotRequests;
    }

    public void setCountPasswdForgotRequests(int countPasswdForgotRequests) {
        this.countPasswdForgotRequests = countPasswdForgotRequests;
    }

    public Date getDateLastPasswdForgotRequest() {
        return dateLastPasswdForgotRequest;
    }

    public void setDateLastPasswdForgotRequest(Date dateLastPasswdForgotRequest) {
        this.dateLastPasswdForgotRequest = dateLastPasswdForgotRequest;
    }

    public Date getDatePasswordForgotReported() {
        return datePasswordForgotReported;
    }

    public void setDatePasswordForgotReported(Date datePasswordForgotReported) {
        this.datePasswordForgotReported = datePasswordForgotReported;
    }

    public Date getDateLastPassword() {
        return dateLastPassword;
    }

    public void setDateLastPassword(Date dateLastPassword) {
        this.dateLastPassword = dateLastPassword;
    }

    public Date getDateLastPasswdSetUp() {
        return dateLastPasswdSetUp;
    }

    public void setDateLastPasswdSetUp(Date dateLastPasswdSetUp) {
        this.dateLastPasswdSetUp = dateLastPasswdSetUp;
    }

    public boolean getIsCredentialsExpired() {
        return isCredentialsExpired;
    }

    public void setIsCredentialsExpired(boolean isCredentialsExpired) {
        this.isCredentialsExpired = isCredentialsExpired;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Operator [id=");
        builder.append(id);
        builder.append(", code=");
        builder.append(code);
        builder.append(", accountId=");
        builder.append(accountId);
        builder.append(", portOperatorId=");
        builder.append(portOperatorId);
        builder.append(", independentPortOperatorId=");
        builder.append(independentPortOperatorId);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", email=");
        builder.append(email);
        builder.append(", msisdn=");
        builder.append(msisdn);
        builder.append(", username=");
        builder.append(username);
        builder.append(", password=");
        builder.append(password == null ? "null" : "xxxxxx-" + password.length());
        builder.append(", roleId=");
        builder.append(roleId);
        builder.append(", isActive=");
        builder.append(isActive);
        builder.append(", isDeleted=");
        builder.append(isDeleted);
        builder.append(", isLocked=");
        builder.append(isLocked);
        builder.append(", loginFailureCount=");
        builder.append(loginFailureCount);
        builder.append(", dateLocked=");
        builder.append(dateLocked);
        builder.append(", dateLastLogin=");
        builder.append(dateLastLogin);
        builder.append(", dateLastAttempt=");
        builder.append(dateLastAttempt);
        builder.append(", countPasswdForgotRequests=");
        builder.append(countPasswdForgotRequests);
        builder.append(", dateLastPasswdForgotRequest=");
        builder.append(dateLastPasswdForgotRequest);
        builder.append(", datePasswordForgotReported=");
        builder.append(datePasswordForgotReported);
        builder.append(", dateLastPassword=");
        builder.append(dateLastPassword);
        builder.append(", dateLastPasswdSetUp=");
        builder.append(dateLastPasswdSetUp);
        builder.append(", operatorId=");
        builder.append(operatorId);
        builder.append(", languageId=");
        builder.append(languageId);
        builder.append(", isCredentialsExpired=");
        builder.append(isCredentialsExpired);
        builder.append(", dateCreated=");
        builder.append(dateCreated);
        builder.append(", dateEdited=");
        builder.append(dateEdited);
        builder.append("]");
        return builder.toString();
    }

}
