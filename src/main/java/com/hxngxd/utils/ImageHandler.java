package com.hxngxd.utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.io.ByteArrayInputStream;

public class ImageHandler {

    private ImageHandler() {
    }

    // Hàm chuyển đổi byte array thành Image
    public static Image toImage(byte[] imageBytes) {
        ByteArrayInputStream bits = new ByteArrayInputStream(imageBytes);
        return new Image(bits);
    }

    // Hàm crop ảnh theo tỷ lệ (widthRatio / heightRatio)
    public static Image cropImageByRatio(Image image, double widthRatio, double heightRatio) {
        // Lấy chiều rộng và chiều cao gốc của ảnh
        double originalWidth = image.getWidth();
        double originalHeight = image.getHeight();

        // Tính tỷ lệ gốc của ảnh
        double originalRatio = originalWidth / originalHeight;

        // Tính toán cropWidth và cropHeight dựa trên tỷ lệ mong muốn
        double cropWidth, cropHeight;
        double targetRatio = widthRatio / heightRatio;

        if (originalRatio > targetRatio) {
            // Nếu ảnh gốc có tỷ lệ rộng hơn, giảm chiều rộng để khớp với tỷ lệ mong muốn
            cropHeight = originalHeight;
            cropWidth = cropHeight * targetRatio;
        } else {
            // Nếu ảnh gốc có tỷ lệ cao hơn, giảm chiều cao để khớp với tỷ lệ mong muốn
            cropWidth = originalWidth;
            cropHeight = cropWidth / targetRatio;
        }

        // Tính toán crop từ trung tâm ảnh
        double centerX = (originalWidth - cropWidth) / 2;
        double centerY = (originalHeight - cropHeight) / 2;

        // Lấy PixelReader từ ảnh gốc
        PixelReader reader = image.getPixelReader();

        // Tạo ảnh mới đã được crop

        return new WritableImage(reader,
                (int) centerX,
                (int) centerY,
                (int) cropWidth,
                (int) cropHeight);
    }

    // Nạp chồng hàm crop ảnh từ byte[] và theo tỷ lệ (widthRatio / heightRatio)
    public static Image cropImageByRatio(byte[] imageBytes, double widthRatio, double heightRatio) {
        // Chuyển byte[] thành Image
        Image image = toImage(imageBytes);

        // Crop ảnh dựa theo tỷ lệ mong muốn
        return cropImageByRatio(image, widthRatio, heightRatio);
    }
}