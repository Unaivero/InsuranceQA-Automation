package com.activepolicies.dashboard.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class ImageComparator {

    public static boolean compareImages(File baseline, File current, File diffOutput, int threshold) throws IOException {
        BufferedImage img1 = ImageIO.read(baseline);
        BufferedImage img2 = ImageIO.read(current);

        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }

        BufferedImage diff = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_RGB);
        boolean isIdentical = true;
        int diffPixels = 0;

        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);
                if (rgb1 != rgb2) {
                    diff.setRGB(x, y, Color.RED.getRGB());
                    isIdentical = false;
                    diffPixels++;
                } else {
                    diff.setRGB(x, y, rgb1);
                }
            }
        }

        double totalPixels = img1.getWidth() * img1.getHeight();
        double diffPercent = (diffPixels / totalPixels) * 100.0;

        if (!isIdentical && diffPercent > threshold) {
            ImageIO.write(diff, "png", diffOutput);
            return false;
        }

        return true;
    }
}
