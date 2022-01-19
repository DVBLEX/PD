package com.pad.server.base.jsonentities.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LanguageJson implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            btnDesc;
    Map<String, String>       translationKeys  = new HashMap<>();

    public Map<String, String> getTranslationKeys() {
        return translationKeys;
    }

    public void setTranslationKeys(Map<String, String> translationKeys) {
        this.translationKeys = translationKeys;
    }

    public String getBtnDesc() {
        return btnDesc;
    }

    public void setBtnDesc(String btnDesc) {
        this.btnDesc = btnDesc;
    }
}
