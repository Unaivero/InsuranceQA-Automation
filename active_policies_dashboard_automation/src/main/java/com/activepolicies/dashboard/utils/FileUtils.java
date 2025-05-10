package com.activepolicies.dashboard.utils;

import com.opencsv.CSVReader;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class FileUtils {

    public static File waitForFile(String directory, String fileNameContains, int timeoutSeconds) throws InterruptedException {
        Path downloadDir = Paths.get(directory);
        File file = null;
        int waited = 0;

        while (waited < timeoutSeconds) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(downloadDir)) {
                for (Path entry : stream) {
                    if (entry.getFileName().toString().contains(fileNameContains)) {
                        file = entry.toFile();
                        if (!entry.toString().endsWith(".crdownload")) {
                            return file;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(1000);
            waited++;
        }
        throw new RuntimeException("Download failed or took too long.");
    }

    public static List<String[]> readCsv(File file) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            return reader.readAll();
        }
    }
}
