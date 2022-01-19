package com.pad.server.base.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "email_config")
public class EmailConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long              id;

    @Column(name = "smtp_host")
    private String            smtpHost;

    @Column(name = "smtp_auth")
    private String            smtpAuth;

    @Column(name = "smtp_port")
    private String            smtpPort;

    @Column(name = "smtp_starttls_enable")
    private String           smtpStarttlsEnable;
    
    @Column(name = "smtp_ssl_protocols")
    private String            smtpSslProtocols;
    
    @Column(name = "operator_id")
    private long              operatorId;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateCreated;

    @Column(name = "date_edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date              dateEdited;

    @Transient
    private String            emailFrom;

    @Transient
    private String            emailFromPassword;

    @Transient
    private String            emailFromAlias;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

	public String getSmtpStarttlsEnable() {
		return smtpStarttlsEnable;
	}

	public void setSmtpStarttlsEnable(String smtpStarttlsEnable) {
		this.smtpStarttlsEnable = smtpStarttlsEnable;
	}

	public String getSmtpSslProtocols() {
		return smtpSslProtocols;
	}

	public void setSmtpSslProtocols(String smtpSslProtocols) {
		this.smtpSslProtocols = smtpSslProtocols;
	}

	public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
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

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailFromPassword() {
        return emailFromPassword;
    }

    public void setEmailFromPassword(String emailFromPassword) {
        this.emailFromPassword = emailFromPassword;
    }

    public String getEmailFromAlias() {
        return emailFromAlias;
    }

    public void setEmailFromAlias(String emailFromAlias) {
        this.emailFromAlias = emailFromAlias;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EmailConfig [id=");
        builder.append(id);
        builder.append(", smtpHost=");
        builder.append(smtpHost);
        builder.append(", smtpAuth=");
        builder.append(smtpAuth);
        builder.append(", smtpPort=");
        builder.append(smtpPort);
        builder.append(", smtpStarttlsEnable=");
        builder.append(smtpStarttlsEnable);
        builder.append(", smtpSslProtocols=");
        builder.append(smtpSslProtocols);
        builder.append("]");
        return builder.toString();
    }
}
