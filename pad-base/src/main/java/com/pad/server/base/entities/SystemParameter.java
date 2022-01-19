package com.pad.server.base.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author matos
 *
 */
@Entity
@Table(name = "system_parameters")
public class SystemParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "errors_from_email")
    private String            errorsFromEmail;

    @Column(name = "errors_from_email_password")
    private String            errorsFromEmailPassword;

    @Column(name = "errors_to_email")
    private String            errorsToEmail;

    @Column(name = "password_forgot_email_limit")
    private int               passwordForgotEmailLimit;

    @Column(name = "reg_email_code_send_limit")
    private int               regEmailCodeSendLimit;

    @Column(name = "reg_email_verification_limit")
    private int               regEmailVerificationLimit;

    @Column(name = "reg_email_code_valid_minutes")
    private int               regEmailCodeValidMinutes;

    @Column(name = "reg_email_verification_valid_hours")
    private int               regEmailVerificationValidHours;

    @Column(name = "reg_sms_code_send_limit")
    private int               regSMSCodeSendLimit;

    @Column(name = "reg_sms_verification_limit")
    private int               regSMSVerificationLimit;

    @Column(name = "reg_sms_code_valid_minutes")
    private int               regSMSCodeValidMinutes;

    @Column(name = "reg_sms_verification_valid_hours")
    private int               regSMSVerificationValidHours;

    @Column(name = "login_lock_count_failed")
    private int               loginLockCountFailed;

    @Column(name = "login_lock_period")
    private int               loginLockPeriod;

    @Column(name = "login_password_valid_period")
    private int               loginPasswordValidPeriod;

    @Column(name = "allowed_hosts")
    private String            allowedHosts;

    @Column(name = "client_id")
    private String            clientId;

    @Column(name = "client_secret")
    private String            clientSecret;

    @Column(name = "tax_percentage")
    private BigDecimal        taxPercentage;

    @Column(name = "kiosk_account_topup_min_amount")
    private BigDecimal        kioskAccountTopupMinAmount;

    @Column(name = "kiosk_account_topup_max_amount")
    private BigDecimal        kioskAccountTopupMaxAmount;

    @Column(name = "finance_account_topup_min_amount")
    private BigDecimal        financeAccountTopupMinAmount;

    @Column(name = "finance_account_topup_max_amount")
    private BigDecimal        financeAccountTopupMaxAmount;

    @Column(name = "finance_session_initial_float_min_amount")
    private BigDecimal        financeSessionInitialFloatMinAmount;

    @Column(name = "finance_session_initial_float_max_amount")
    private BigDecimal        financeSessionInitialFloatMaxAmount;

    @Column(name = "maximum_overdraft_limit_min_amount")
    private BigDecimal        maximumOverdraftLimitMinAmount;

    @Column(name = "maximum_overdraft_limit_max_amount")
    private BigDecimal        maximumOverdraftLimitMaxAmount;

    @Column(name = "in_transit_validity_minutes")
    private int               inTransitValidityMinutes;

    @Column(name = "exit_prematurely_validity_minutes")
    private int               exitPrematurelyValidityMinutes;

    @Column(name = "trip_slot_start_range_days")
    private int               tripSlotStartRangeDays;

    @Column(name = "booking_limit_per_hour")
    private int               bookingLimitPerHour;

    @Column(name = "is_booking_limit_check_enabled")
    private boolean           isBookingLimitCheckEnabled;

    @Column(name = "is_port_entry_filtering")
    private boolean           isPortEntryFiltering;

    @Column(name = "is_parking_supervisor_readonly_enabled")
    private boolean           isParkingSupervisorReadonlyEnabled;

    @Column(name = "receipt_link_expire_days")
    private int               receiptLinkExpireDays;

    @Column(name = "receipt_lock_count_failed")
    private int               receiptLockCountFailed;

    @Column(name = "receipt_lock_period")
    private int               receiptLockPeriod;

    @Column(name = "booking_check_date_slot_approved_in_advance_hours")
    private int               bookingCheckDateSlotApprovedInAdvanceHours;

    @Column(name = "booking_check_date_slot_approved_afterwards_hours")
    private int               bookingCheckDateSlotApprovedAfterwardsHours;

    @Column(name = "ags_operations_email")
    private String            agsOperationsEmail;

    @Column(name = "ags_finance_email")
    private String            agsFinanceEmail;

    @Column(name = "auto_release_exit_capacity_percentage")
    private int               autoReleaseExitCapacityPercentage;

    @Column(name = "drop_off_empty_night_mission_start_time")
    private String            dropOffEmptyNightMissionStartTime;

    @Column(name = "drop_off_empty_night_mission_end_time")
    private String            dropOffEmptyNightMissionEndTime;

    @Column(name = "drop_off_empty_triangle_mission_start_time")
    private String            dropOffEmptyTriangleMissionStartTime;

    @Column(name = "drop_off_empty_triangle_mission_end_time")
    private String            dropOffEmptyTriangleMissionEndTime;

    @Column(name = "printer_socket_timeout")
    private int               printerSocketTimeout;

    @Column(name = "printer_connect_timeout")
    private int               printerConnectTimeout;

    @Column(name = "printer_conn_request_timeout")
    private int               printerConnRequestTimeout;

    @Column(name = "invoice_statement_schedule_hour")
    private int               invoiceStatementScheduleHour;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getErrorsFromEmail() {
        return errorsFromEmail;
    }

    public void setErrorsFromEmail(String errorsFromEmail) {
        this.errorsFromEmail = errorsFromEmail;
    }

    public String getErrorsFromEmailPassword() {
        return errorsFromEmailPassword;
    }

    public void setErrorsFromEmailPassword(String errorsFromEmailPassword) {
        this.errorsFromEmailPassword = errorsFromEmailPassword;
    }

    public String getErrorsToEmail() {
        return errorsToEmail;
    }

    public void setErrorsToEmail(String errorsToEmail) {
        this.errorsToEmail = errorsToEmail;
    }

    public int getPasswordForgotEmailLimit() {
        return passwordForgotEmailLimit;
    }

    public void setPasswordForgotEmailLimit(int passwordForgotEmailLimit) {
        this.passwordForgotEmailLimit = passwordForgotEmailLimit;
    }

    public int getRegEmailCodeSendLimit() {
        return regEmailCodeSendLimit;
    }

    public void setRegEmailCodeSendLimit(int regEmailCodeSendLimit) {
        this.regEmailCodeSendLimit = regEmailCodeSendLimit;
    }

    public int getRegEmailVerificationLimit() {
        return regEmailVerificationLimit;
    }

    public void setRegEmailVerificationLimit(int regEmailVerificationLimit) {
        this.regEmailVerificationLimit = regEmailVerificationLimit;
    }

    public int getRegEmailCodeValidMinutes() {
        return regEmailCodeValidMinutes;
    }

    public void setRegEmailCodeValidMinutes(int regEmailCodeValidMinutes) {
        this.regEmailCodeValidMinutes = regEmailCodeValidMinutes;
    }

    public int getRegEmailVerificationValidHours() {
        return regEmailVerificationValidHours;
    }

    public void setRegEmailVerificationValidHours(int regEmailVerificationValidHours) {
        this.regEmailVerificationValidHours = regEmailVerificationValidHours;
    }

    public int getRegSMSCodeSendLimit() {
        return regSMSCodeSendLimit;
    }

    public void setRegSMSCodeSendLimit(int regSMSCodeSendLimit) {
        this.regSMSCodeSendLimit = regSMSCodeSendLimit;
    }

    public int getRegSMSVerificationLimit() {
        return regSMSVerificationLimit;
    }

    public void setRegSMSVerificationLimit(int regSMSVerificationLimit) {
        this.regSMSVerificationLimit = regSMSVerificationLimit;
    }

    public int getRegSMSCodeValidMinutes() {
        return regSMSCodeValidMinutes;
    }

    public void setRegSMSCodeValidMinutes(int regSMSCodeValidMinutes) {
        this.regSMSCodeValidMinutes = regSMSCodeValidMinutes;
    }

    public int getRegSMSVerificationValidHours() {
        return regSMSVerificationValidHours;
    }

    public void setRegSMSVerificationValidHours(int regSMSVerificationValidHours) {
        this.regSMSVerificationValidHours = regSMSVerificationValidHours;
    }

    public int getLoginLockCountFailed() {
        return loginLockCountFailed;
    }

    public void setLoginLockCountFailed(int loginLockCountFailed) {
        this.loginLockCountFailed = loginLockCountFailed;
    }

    public int getLoginLockPeriod() {
        return loginLockPeriod;
    }

    public void setLoginLockPeriod(int loginLockPeriod) {
        this.loginLockPeriod = loginLockPeriod;
    }

    public int getLoginPasswordValidPeriod() {
        return loginPasswordValidPeriod;
    }

    public void setLoginPasswordValidPeriod(int loginPasswordValidPeriod) {
        this.loginPasswordValidPeriod = loginPasswordValidPeriod;
    }

    public String getAllowedHosts() {
        return allowedHosts;
    }

    public void setAllowedHosts(String allowedHosts) {
        this.allowedHosts = allowedHosts;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getKioskAccountTopupMinAmount() {
        return kioskAccountTopupMinAmount;
    }

    public void setKioskAccountTopupMinAmount(BigDecimal kioskAccountTopupMinAmount) {
        this.kioskAccountTopupMinAmount = kioskAccountTopupMinAmount;
    }

    public BigDecimal getKioskAccountTopupMaxAmount() {
        return kioskAccountTopupMaxAmount;
    }

    public void setKioskAccountTopupMaxAmount(BigDecimal kioskAccountTopupMaxAmount) {
        this.kioskAccountTopupMaxAmount = kioskAccountTopupMaxAmount;
    }

    public BigDecimal getFinanceAccountTopupMinAmount() {
        return financeAccountTopupMinAmount;
    }

    public void setFinanceAccountTopupMinAmount(BigDecimal financeAccountTopupMinAmount) {
        this.financeAccountTopupMinAmount = financeAccountTopupMinAmount;
    }

    public BigDecimal getFinanceAccountTopupMaxAmount() {
        return financeAccountTopupMaxAmount;
    }

    public void setFinanceAccountTopupMaxAmount(BigDecimal financeAccountTopupMaxAmount) {
        this.financeAccountTopupMaxAmount = financeAccountTopupMaxAmount;
    }

    public BigDecimal getFinanceSessionInitialFloatMinAmount() {
        return financeSessionInitialFloatMinAmount;
    }

    public void setFinanceSessionInitialFloatMinAmount(BigDecimal financeSessionInitialFloatMinAmount) {
        this.financeSessionInitialFloatMinAmount = financeSessionInitialFloatMinAmount;
    }

    public BigDecimal getFinanceSessionInitialFloatMaxAmount() {
        return financeSessionInitialFloatMaxAmount;
    }

    public void setFinanceSessionInitialFloatMaxAmount(BigDecimal financeSessionInitialFloatMaxAmount) {
        this.financeSessionInitialFloatMaxAmount = financeSessionInitialFloatMaxAmount;
    }

    public BigDecimal getMaximumOverdraftLimitMinAmount() {
        return maximumOverdraftLimitMinAmount;
    }

    public void setMaximumOverdraftLimitMinAmount(BigDecimal maximumOverdraftLimitMinAmount) {
        this.maximumOverdraftLimitMinAmount = maximumOverdraftLimitMinAmount;
    }

    public BigDecimal getMaximumOverdraftLimitMaxAmount() {
        return maximumOverdraftLimitMaxAmount;
    }

    public void setMaximumOverdraftLimitMaxAmount(BigDecimal maximumOverdraftLimitMaxAmount) {
        this.maximumOverdraftLimitMaxAmount = maximumOverdraftLimitMaxAmount;
    }

    public int getInTransitValidityMinutes() {
        return inTransitValidityMinutes;
    }

    public void setInTransitValidityMinutes(int inTransitValidityMinutes) {
        this.inTransitValidityMinutes = inTransitValidityMinutes;
    }

    public int getExitPrematurelyValidityMinutes() {
        return exitPrematurelyValidityMinutes;
    }

    public void setExitPrematurelyValidityMinutes(int exitPrematurelyValidityMinutes) {
        this.exitPrematurelyValidityMinutes = exitPrematurelyValidityMinutes;
    }

    public int getTripSlotStartRangeDays() {
        return tripSlotStartRangeDays;
    }

    public void setTripSlotStartRangeDays(int tripSlotStartRangeDays) {
        this.tripSlotStartRangeDays = tripSlotStartRangeDays;
    }

    public int getBookingLimitPerHour() {
        return bookingLimitPerHour;
    }

    public void setBookingLimitPerHour(int bookingLimitPerHour) {
        this.bookingLimitPerHour = bookingLimitPerHour;
    }

    public boolean getIsBookingLimitCheckEnabled() {
        return isBookingLimitCheckEnabled;
    }

    public void setIsBookingLimitCheckEnabled(boolean isBookingLimitCheckEnabled) {
        this.isBookingLimitCheckEnabled = isBookingLimitCheckEnabled;
    }

    public boolean getIsPortEntryFiltering() {
        return isPortEntryFiltering;
    }

    public void setIsPortEntryFiltering(boolean portEntryFiltering) {
        isPortEntryFiltering = portEntryFiltering;
    }

    public boolean getIsParkingSupervisorReadonlyEnabled() {
        return isParkingSupervisorReadonlyEnabled;
    }

    public void setIsParkingSupervisorReadonlyEnabled(boolean isParkingSupervisorReadonlyEnabled) {
        this.isParkingSupervisorReadonlyEnabled = isParkingSupervisorReadonlyEnabled;
    }

    public int getReceiptLinkExpireDays() {
        return receiptLinkExpireDays;
    }

    public void setReceiptLinkExpireDays(int receiptLinkExpireDays) {
        this.receiptLinkExpireDays = receiptLinkExpireDays;
    }

    public int getReceiptLockCountFailed() {
        return receiptLockCountFailed;
    }

    public void setReceiptLockCountFailed(int receiptLockCountFailed) {
        this.receiptLockCountFailed = receiptLockCountFailed;
    }

    public int getReceiptLockPeriod() {
        return receiptLockPeriod;
    }

    public void setReceiptLockPeriod(int receiptLockPeriod) {
        this.receiptLockPeriod = receiptLockPeriod;
    }

    public int getBookingCheckDateSlotApprovedInAdvanceHours() {
        return bookingCheckDateSlotApprovedInAdvanceHours;
    }

    public void setBookingCheckDateSlotApprovedInAdvanceHours(int bookingCheckDateSlotApprovedInAdvanceHours) {
        this.bookingCheckDateSlotApprovedInAdvanceHours = bookingCheckDateSlotApprovedInAdvanceHours;
    }

    public int getBookingCheckDateSlotApprovedAfterwardsHours() {
        return bookingCheckDateSlotApprovedAfterwardsHours;
    }

    public void setBookingCheckDateSlotApprovedAfterwardsHours(int bookingCheckDateSlotApprovedAfterwardsHours) {
        this.bookingCheckDateSlotApprovedAfterwardsHours = bookingCheckDateSlotApprovedAfterwardsHours;
    }

    public String getAgsOperationsEmail() {
        return agsOperationsEmail;
    }

    public void setAgsOperationsEmail(String agsOperationsEmail) {
        this.agsOperationsEmail = agsOperationsEmail;
    }

    public String getAgsFinanceEmail() {
        return agsFinanceEmail;
    }

    public void setAgsFinanceEmail(String agsFinanceEmail) {
        this.agsFinanceEmail = agsFinanceEmail;
    }

    public int getAutoReleaseExitCapacityPercentage() {
        return autoReleaseExitCapacityPercentage;
    }

    public void setAutoReleaseExitCapacityPercentage(int autoReleaseExitCapacityPercentage) {
        this.autoReleaseExitCapacityPercentage = autoReleaseExitCapacityPercentage;
    }

    public String getDropOffEmptyNightMissionStartTime() {
        return dropOffEmptyNightMissionStartTime;
    }

    public void setDropOffEmptyNightMissionStartTime(String dropOffEmptyNightMissionStartTime) {
        this.dropOffEmptyNightMissionStartTime = dropOffEmptyNightMissionStartTime;
    }

    public String getDropOffEmptyNightMissionEndTime() {
        return dropOffEmptyNightMissionEndTime;
    }

    public void setDropOffEmptyNightMissionEndTime(String dropOffEmptyNightMissionEndTime) {
        this.dropOffEmptyNightMissionEndTime = dropOffEmptyNightMissionEndTime;
    }

    public String getDropOffEmptyTriangleMissionStartTime() {
        return dropOffEmptyTriangleMissionStartTime;
    }

    public void setDropOffEmptyTriangleMissionStartTime(String dropOffEmptyTriangleMissionStartTime) {
        this.dropOffEmptyTriangleMissionStartTime = dropOffEmptyTriangleMissionStartTime;
    }

    public String getDropOffEmptyTriangleMissionEndTime() {
        return dropOffEmptyTriangleMissionEndTime;
    }

    public void setDropOffEmptyTriangleMissionEndTime(String dropOffEmptyTriangleMissionEndTime) {
        this.dropOffEmptyTriangleMissionEndTime = dropOffEmptyTriangleMissionEndTime;
    }

    public int getPrinterSocketTimeout() {
        return printerSocketTimeout;
    }

    public void setPrinterSocketTimeout(int printerSocketTimeout) {
        this.printerSocketTimeout = printerSocketTimeout;
    }

    public int getPrinterConnectTimeout() {
        return printerConnectTimeout;
    }

    public void setPrinterConnectTimeout(int printerConnectTimeout) {
        this.printerConnectTimeout = printerConnectTimeout;
    }

    public int getPrinterConnRequestTimeout() {
        return printerConnRequestTimeout;
    }

    public void setPrinterConnRequestTimeout(int printerConnRequestTimeout) {
        this.printerConnRequestTimeout = printerConnRequestTimeout;
    }

    public int getInvoiceStatementScheduleHour() {
        return invoiceStatementScheduleHour;
    }

    public void setInvoiceStatementScheduleHour(int invoiceStatementScheduleHour) {
        this.invoiceStatementScheduleHour = invoiceStatementScheduleHour;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SystemParameter [id=");
        builder.append(id);
        builder.append(", errorsFromEmail=");
        builder.append(errorsFromEmail);
        builder.append(", errorsFromEmailPassword=");
        builder.append(errorsFromEmailPassword != null ? errorsFromEmailPassword.length() : "null");
        builder.append(", errorsToEmail=");
        builder.append(errorsToEmail);
        builder.append(", passwordForgotEmailLimit=");
        builder.append(passwordForgotEmailLimit);
        builder.append(", regEmailCodeSendLimit=");
        builder.append(regEmailCodeSendLimit);
        builder.append(", regEmailVerificationLimit=");
        builder.append(regEmailVerificationLimit);
        builder.append(", regEmailCodeValidMinutes=");
        builder.append(regEmailCodeValidMinutes);
        builder.append(", regEmailVerificationValidHours=");
        builder.append(regEmailVerificationValidHours);
        builder.append(", regSMSCodeSendLimit=");
        builder.append(regSMSCodeSendLimit);
        builder.append(", regSMSVerificationLimit=");
        builder.append(regSMSVerificationLimit);
        builder.append(", regSMSCodeValidMinutes=");
        builder.append(regSMSCodeValidMinutes);
        builder.append(", regSMSVerificationValidHours=");
        builder.append(regSMSVerificationValidHours);
        builder.append(", loginLockCountFailed=");
        builder.append(loginLockCountFailed);
        builder.append(", loginLockPeriod=");
        builder.append(loginLockPeriod);
        builder.append(", loginPasswordValidPeriod=");
        builder.append(loginPasswordValidPeriod);
        builder.append(", allowedHosts=");
        builder.append(allowedHosts);
        builder.append(", clientId=");
        builder.append(clientId != null ? clientId.length() : "null");
        builder.append(", clientSecret=");
        builder.append(clientSecret != null ? clientSecret.length() : "null");
        builder.append(", taxPercentage=");
        builder.append(taxPercentage);
        builder.append(", kioskAccountTopupMinAmount=");
        builder.append(kioskAccountTopupMinAmount);
        builder.append(", kioskAccountTopupMaxAmount=");
        builder.append(kioskAccountTopupMaxAmount);
        builder.append(", financekAccountTopupMaxAmount=");
        builder.append(financeAccountTopupMaxAmount);
        builder.append(", financeSessionInitialFloatMinAmount=");
        builder.append(financeSessionInitialFloatMinAmount);
        builder.append(", financeSessionInitialFloatMaxAmount=");
        builder.append(financeSessionInitialFloatMaxAmount);
        builder.append(", maximumOverdraftLimitMinAmount=");
        builder.append(maximumOverdraftLimitMinAmount);
        builder.append(", maximumOverdraftLimitMaxAmount=");
        builder.append(maximumOverdraftLimitMaxAmount);
        builder.append(", inTransitValidityMinutes=");
        builder.append(inTransitValidityMinutes);
        builder.append(", exitPrematurelyValidityMinutes=");
        builder.append(exitPrematurelyValidityMinutes);
        builder.append(", tripSlotStartRangeDays=");
        builder.append(tripSlotStartRangeDays);
        builder.append(", bookingLimitPerHour=");
        builder.append(bookingLimitPerHour);
        builder.append(", isBookingLimitCheckEnabled=");
        builder.append(isBookingLimitCheckEnabled);
        builder.append(", isPortEntryFiltering=");
        builder.append(isPortEntryFiltering);
        builder.append(", isParkingSupervisorReadonlyEnabled=");
        builder.append(isParkingSupervisorReadonlyEnabled);
        builder.append(", receiptLinkExpireDays=");
        builder.append(receiptLinkExpireDays);
        builder.append(", receiptLockCountFailed=");
        builder.append(receiptLockCountFailed);
        builder.append(", receiptLockPeriod=");
        builder.append(receiptLockPeriod);
        builder.append(", bookingCheckDateSlotApprovedInAdvanceHours=");
        builder.append(bookingCheckDateSlotApprovedInAdvanceHours);
        builder.append(", bookingCheckDateSlotApprovedAfterwardsHours=");
        builder.append(bookingCheckDateSlotApprovedAfterwardsHours);
        builder.append(", agsOperationsEmail=");
        builder.append(agsOperationsEmail);
        builder.append(", agsFinanceEmail=");
        builder.append(agsFinanceEmail);
        builder.append(", autoReleaseExitCapacityPercentage=");
        builder.append(autoReleaseExitCapacityPercentage);
        builder.append(", dropOffEmptyNightMissionStartTime=");
        builder.append(dropOffEmptyNightMissionStartTime);
        builder.append(", dropOffEmptyNightMissionEndTime=");
        builder.append(dropOffEmptyNightMissionEndTime);
        builder.append(", dropOffEmptyTriangleMissionStartTime=");
        builder.append(dropOffEmptyTriangleMissionStartTime);
        builder.append(", dropOffEmptyTriangleMissionEndTime=");
        builder.append(dropOffEmptyTriangleMissionEndTime);
        builder.append(", printerSocketTimeout=");
        builder.append(printerSocketTimeout);
        builder.append(", printerConnectTimeout=");
        builder.append(printerConnectTimeout);
        builder.append(", printerConnRequestTimeout=");
        builder.append(printerConnRequestTimeout);
        builder.append(", invoiceStatementScheduleHour=");
        builder.append(invoiceStatementScheduleHour);
        builder.append("]");
        return builder.toString();
    }

}
