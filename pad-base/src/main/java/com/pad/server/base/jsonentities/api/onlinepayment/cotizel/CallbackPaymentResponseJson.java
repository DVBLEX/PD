package com.pad.server.base.jsonentities.api.onlinepayment.cotizel;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallbackPaymentResponseJson {

    private String nom;
    private String telephone;
    private String reference;
    private String hashcode;
    private String username;
    private String password;
    private String transaction;
    private String datetimepaiement;
    private String montant;
    private String codeags;
    private String status;

    public CallbackPaymentResponseJson() {

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
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

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getDatetimepaiement() {
        return datetimepaiement;
    }

    public void setDatetimepaiement(String datetimepaiement) {
        this.datetimepaiement = datetimepaiement;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getCodeags() {
        return codeags;
    }

    public void setCodeags(String codeags) {
        this.codeags = codeags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSha256Hashcode() {
        return DigestUtils.sha256Hex("codeags=" + codeags + "&datetimepaiement=" + datetimepaiement + "&montant=" + montant + "&nom=" + nom + "&password=" + password
            + "&reference=" + reference + "&status=" + status + "&telephone=" + telephone + "&transaction=" + transaction + "&username=" + username);
    }

    public String getShaString() {
        return "codeags=" + codeags + "&datetimepaiement=" + datetimepaiement + "&montant=" + montant + "&nom=" + nom + "&password=" + password + "&reference=" + reference
            + "&status=" + status + "&telephone=" + telephone + "&transaction=" + transaction + "&username=" + username;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CallbackPaymentResponseJson [nom=");
        builder.append(nom);
        builder.append(", telephone=");
        builder.append(telephone);
        builder.append(", reference=");
        builder.append(reference);
        builder.append(", hashcode=");
        builder.append(hashcode);
        builder.append(", username=");
        builder.append(username);
        builder.append(", password=");
        builder.append(password == null ? "null" : "xxxxxx-" + password.length());
        builder.append(", transaction=");
        builder.append(transaction);
        builder.append(", datetimepaiement=");
        builder.append(datetimepaiement);
        builder.append(", montant=");
        builder.append(montant);
        builder.append(", codeags=");
        builder.append(codeags);
        builder.append(", status=");
        builder.append(status);
        builder.append("]");
        return builder.toString();
    }

}
