package com.hxngxd.utils;

import com.hxngxd.enums.LogMessages;
import com.hxngxd.ui.StageManager;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class ImageHandler {
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
            e.printStackTrace();
            log.error(LogMessages.File.FILE_NOT_FOUND.getMessage(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(LogMessages.File.FILE_IO_ERROR.getMessage(file.getAbsolutePath()));
        }
        return result;
    }

    public static Image cropImageByRatio(Image image,
                                         double widthRatio, double heightRatio) {
        if (image == null) {
            log.info(LogMessages.General.IS_NULL.getMessage("Image"));
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

    public static Image cropImageByRatio(byte[] imageBytes,
                                         double widthRatio, double heightRatio) {
        Image image = byteArrayToImage(imageBytes);
        return cropImageByRatio(image, widthRatio, heightRatio);
    }

    public static Image loadImageFromFile(File file) {
        if (file == null) {
            log.info(LogMessages.General.IS_NULL.getMessage("File"));
            return null;
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            return new Image(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error(
                    LogMessages.File.FILE_NOT_FOUND.getMessage(
                            file.getAbsolutePath()), e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(
                    LogMessages.File.FILE_IO_ERROR.getMessage(
                            file.getAbsolutePath()), e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(
                    LogMessages.General.SOMETHING_WENT_WRONG.getMessage(
                            file.getAbsolutePath()), e);
        }
        return null;
    }

    public static File chooseImage(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Tất cả file ảnh",
                        "*.jpg", "*.jpeg", "*.png"),
                new FileChooser.ExtensionFilter(
                        "File JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter(
                        "File PNG", "*.png")
        );
        return fileChooser.showOpenDialog(StageManager.getInstance().getMainStage());
    }
}