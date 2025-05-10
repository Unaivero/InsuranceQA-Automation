package com.activepolicies.dashboard.utils;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDataLoader {

    public static Map<String, String> loadKeyValueCsv(File file) throws Exception {
        Map<String, String> data = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> rows = reader.readAll();
            for (String[] row : rows) {
                if (row.length >= 2) {
                    data.put(row[0], row[1]);
                }
            }
        }
        return data;
    }
}
