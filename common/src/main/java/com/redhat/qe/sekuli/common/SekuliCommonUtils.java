package com.redhat.qe.sekuli.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SekuliCommonUtils {

    public static final Gson GSON = new Gson();

    private SekuliCommonUtils() {

    }

    public static String toJson(Object data) {
        return GSON.toJson(data);
    }

    public static String imageToBase64(BufferedImage bufferedImage) throws IOException {
        return imageToBase64(bufferedImage, "png");
    }

    public static String imageToBase64(BufferedImage bufferedImage, String type) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, type.toLowerCase(), outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } finally {
            outputStream.close();
        }
    }

    public static BufferedImage imageFromBase64(String base64png) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64png);
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        try {
            return ImageIO.read(inputStream);
        } finally {
            inputStream.close();
        }
    }

    public static String base64(String path, String... more) throws IOException {
        return Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(path, more)));
    }

    public static void saveToDisk(String base64Data, String path, String... more) throws IOException {
        byte[] data = Base64.getDecoder().decode(base64Data);
        Path actualPath = Paths.get(path, more);
        Files.write(actualPath, data);
    }

}
