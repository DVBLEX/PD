package com.pad.server.web.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class BuildVersionNumberUpdater {

    public static void main(String[] args) throws Exception {

        String filePath = args[0];
        String buildVersionNumber = args[1];

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(new File(filePath)));
            List<String> lines = new ArrayList<>();

            String line = null;
            String buildNumberPart = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains(">v")) {
                    buildNumberPart = line.substring(line.indexOf(">v") + 2, line.lastIndexOf("</span>"));
                    line = line.replace(buildNumberPart, buildVersionNumber);
                }
                lines.add(line);
            }

            writer = new BufferedWriter(new FileWriter(new File(filePath)));
            for (String fileLine : lines) {
                writer.write(fileLine);
                writer.newLine();
            }
            writer.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }
}
