package com.hxngxd.utils;

import com.hxngxd.enums.LogMsg;
import com.hxngxd.ui.StageManager;
import com.hxngxd.ui.UIManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;

public final class ImageHandler {

    private static final Logger log = LogManager.getLogger(ImageHandler.class);

    private ImageHandler() {
    }

    public static Image byteArrayToImage(byte[] imageBytes) {
        ByteArrayInputStream bits = new ByteArrayInputStream(imageBytes);
        return new Image(bits);
    }

    public static byte[] fileToByteArray(File file) {
        byte[] result = null;
        try (InputStream inputStream = new FileInputStream(file)) {
            result = inputStream.readAllBytes();
        } catch (FileNotFoundException e) {
            log.error(LogMsg.FILE_NOT_FOUND.msg(file.getAbsolutePath()), e);
        } catch (IOException e) {
            log.error(LogMsg.FILE_IO_ERROR.msg(file.getAbsolutePath()), e);
        }
        return result;
    }

    public static Image cropImageByRatio(Image image, double widthRatio, double heightRatio) {
        if (image == null) {
            log.info(LogMsg.GENERAL_IS_NULL.msg("Image"));
            return null;
        }

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
                (int) centerX, (int) centerY,
                (int) cropWidth, (int) cropHeight);
    }

    public static void circleCrop(ImageView imageView, double size) {
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);
        imageView.setClip(new Circle(size / 2, size / 2, size / 2));
    }

    public static Image cropImageByRatio(byte[] imageBytes, double widthRatio, double heightRatio) {
        Image image = byteArrayToImage(imageBytes);
        return cropImageByRatio(image, widthRatio, heightRatio);
    }

    public static Image loadImageFromFile(File file) {
        if (file == null) {
            log.info(LogMsg.FILE_IS_NULL.msg("File"));
            return null;
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            return new Image(inputStream);
        } catch (FileNotFoundException e) {
            log.error(LogMsg.FILE_NOT_FOUND.msg(file.getAbsolutePath()), e);
        } catch (IOException e) {
            log.error(LogMsg.FILE_IO_ERROR.msg(file.getAbsolutePath()), e);
        } catch (Exception e) {
            log.error(LogMsg.GENERAL_SOMETHING_WENT_WRONG.msg(file.getAbsolutePath()), e);
        }
        return null;
    }

    public static File chooseImage(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tất cả file ảnh", "*.jpg", "*.jpeg", "*.png"),
                new FileChooser.ExtensionFilter("File JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("File PNG", "*.png")
        );
        return fileChooser.showOpenDialog(StageManager.getInstance().getMainStage());
    }

    public static Image loadImageFromResource(String resourcePath) {
        try {
            URL resourceURL = UIManager.class.getResource(resourcePath);
            if (resourceURL != null) {
                return new Image(resourceURL.toExternalForm());
            } else {
                log.error(LogMsg.FILE_NOT_FOUND.msg(resourcePath));
                return null;
            }
        } catch (Exception e) {
            log.error(LogMsg.FILE_IO_ERROR.msg(resourcePath));
            return null;
        }
    }

}