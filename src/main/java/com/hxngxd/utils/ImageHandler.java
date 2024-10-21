package com.hxngxd.utils;

import com.hxngxd.ui.StageManager;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

public class ImageHandler {
    private static final Logger logger = LogManager.getLogger(ImageHandler.class);

    private ImageHandler() {
    }

    public static Image byteToImage(byte[] imageBytes) {
        ByteArrayInputStream bits = new ByteArrayInputStream(imageBytes);
        return new Image(bits);
    }

    public static Image cropImageByRatio(Image image,
                                         double widthRatio, double heightRatio) {
        double originalWidth = image.getWidth();
        double originalHeight = image.getHeight();

        double originalRatio = originalWidth / originalHeight;

        double cropWidth, cropHeight;
        double targetRatio = widthRatio / heightRatio;

        if (originalRatio > targetRatio) {
            cropHeight = originalHeight;
            cropWidth = cropHeight * targetRatio;
        } else {
            cropWidth = originalWidth;
            cropHeight = cropWidth / targetRatio;
        }

        double centerX = (originalWidth - cropWidth) / 2;
        double centerY = (originalHeight - cropHeight) / 2;

        PixelReader reader = image.getPixelReader();

        return new WritableImage(reader,
                (int) centerX,
                (int) centerY,
                (int) cropWidth,
                (int) cropHeight);
    }

    public static Image cropImageByRatio(byte[] imageBytes,
                                         double widthRatio, double heightRatio) {
        Image image = byteToImage(imageBytes);
        return cropImageByRatio(image, widthRatio, heightRatio);
    }

    public static Image loadImageFromPath(String path) {
        try {
            URL url = Paths.get(path).toUri().toURL();
            InputStream inputStream = url.openStream();
            Image image = new Image(inputStream);
            inputStream.close();
            return image;
        } catch (Exception e) {
            logger.error(LogMsg.fail("loadOnce image: " + path), e);
        }
        return null;
    }

    public static File chooseImage(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Tất cả file ảnh", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter(
                        "File JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter(
                        "File PNG", "*.png"),
                new FileChooser.ExtensionFilter(
                        "File GIF", "*.gif"),
                new FileChooser.ExtensionFilter(
                        "File BMP", "*.bmp")
        );
        return fileChooser.showOpenDialog(StageManager.getInstance().getMainStage());
    }
}