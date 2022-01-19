package com.pad.server.base.services.translation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pad.server.base.jsonentities.api.LanguageJson;

@Service
@Transactional
public class TranslationServiceImpl implements TranslationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getTranslationsMap() {

        final Map<String, Object> languageMap = new HashMap<>();

        jdbcTemplate.query("SELECT * FROM languages", new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Map<String, String> keyMap = new HashMap<>();

                jdbcTemplate.query("SELECT translate_key, translate_value FROM language_keys WHERE language_id = ?", new RowCallbackHandler() {

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        keyMap.put(rs.getString("translate_key"), rs.getString("translate_value"));
                    }
                }, rs.getInt("id"));

                LanguageJson languageJson = new LanguageJson();
                languageJson.setBtnDesc(rs.getString("btn_description"));
                languageJson.setTranslationKeys(keyMap);

                languageMap.put(rs.getString("language"), languageJson);

            }
        });

        return languageMap;
    }
}
