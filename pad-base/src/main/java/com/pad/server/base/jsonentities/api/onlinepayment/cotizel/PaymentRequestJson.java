package com.pad.server.base.jsonentities.api.onlinepayment.cotizel;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequestJson {

    private String callback;
    private String groupe;
    private String hashcode;
    private String login;
    private String montant;
    private String nom;
    private String operateur;
    private String password;
    private String prenom;
    private String reference;
    private String telephone;
    private String token;

    @JsonIgnore
    private long   mnoId;

    public PaymentRequestJson(String callback, String groupe, String login, String montant, String nom, String operateur, String password, String prenom, String reference,
        String telephone, String token, long mnoId) {
        super();
        this.callback = callback;
        this.groupe = groupe;
        this.login = login;
        this.montant = montant;
        this.nom = nom;
        this.operateur = operateur;
        this.password = password;
        this.prenom = prenom;
        this.reference = reference;
        this.telephone = telephone;
        this.token = token;
        this.mnoId = mnoId;
        this.hashcode = getHashcode();
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getHashcode() {
        return DigestUtils.sha256Hex("callback=" + callback + "&groupe=" + groupe + "&login=" + login + "&montant=" + montant + "&nom=" + nom + "&operateur=" + operateur
            + "&password=" + password + "&prenom=" + prenom + "&reference=" + reference + "&telephone=" + telephone + "&token=" + token);
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getMnoId() {
        return mnoId;
    }

    public void setMnoId(long mnoId) {
        this.mnoId = mnoId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PaymentRequestJson [callback=");
        builder.append(callback);
        builder.append(", groupe=");
        builder.append(groupe);
        builder.append(", hashcode=");
        builder.append(hashcode);
        builder.append(", login=");
        builder.append(login);
        builder.append(", montant=");
        builder.append(montant);
        builder.append(", nom=");
        builder.append(nom);
        builder.append(", operateur=");
        builder.append(operateur);
        builder.append(", password=");
        builder.append(password == null ? "null" : "xxxxxx-" + password.length());
        builder.append(", prenom=");
        builder.append(prenom);
        builder.append(", reference=");
        builder.append(reference);
        builder.append(", telephone=");
        builder.append(telephone);
        builder.append(", token=");
        builder.append(token);
        builder.append(", mnoId=");
        builder.append(mnoId);
        builder.append("]");
        return builder.toString();
    }

}
