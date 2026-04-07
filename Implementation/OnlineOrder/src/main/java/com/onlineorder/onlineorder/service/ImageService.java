package com.onlineorder.onlineorder.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png");
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    private static final int MIN_DIMENSION = 300;
    private static final int MAX_DIMENSION = 2000;

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Saves a restaurant cover image.
     * Returns the URL path to the saved file, or null if no file provided.
     */
    public String saveRestaurantImage(MultipartFile file, Long restaurantId) throws IOException {
        if (file == null || file.isEmpty()) return null;

        validateFormat(file);
        BufferedImage original = readAndValidate(file);

        String ext = getExtension(file.getOriginalFilename());
        String filename = "restaurant_" + restaurantId + "_" + UUID.randomUUID() + "." + ext;
        Path dir = Paths.get(uploadDir, "restaurants", restaurantId.toString(), "cover");
        Files.createDirectories(dir);

        byte[] imageBytes = processImage(original, ext);
        Files.write(dir.resolve(filename), imageBytes);

        return "/uploads/restaurants/" + restaurantId + "/cover/" + filename;
    }

    /**
     * Saves a menu item image.
     * Returns the URL path to the saved file, or null if no file provided.
     */
    public String saveMenuItemImage(MultipartFile file, Long restaurantId, Long menuItemId) throws IOException {
        if (file == null || file.isEmpty()) return null;

        validateFormat(file);
        BufferedImage original = readAndValidate(file);

        String ext = getExtension(file.getOriginalFilename());
        String filename = "menuitem_" + restaurantId + "_" + menuItemId + "_" + UUID.randomUUID() + "." + ext;
        Path dir = Paths.get(uploadDir, "restaurants", restaurantId.toString(), "menu");
        Files.createDirectories(dir);

        byte[] imageBytes = processImage(original, ext);
        Files.write(dir.resolve(filename), imageBytes);

        return "/uploads/restaurants/" + restaurantId + "/menu/" + filename;
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private void validateFormat(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                "Unsupported image format: " + contentType + ". Please upload a JPEG or PNG file."
            );
        }
    }

    private BufferedImage readAndValidate(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IllegalArgumentException("Cannot read image file. The file may be corrupted.");
        }
        int width = image.getWidth();
        int height = image.getHeight();
        if (width < MIN_DIMENSION || height < MIN_DIMENSION) {
            throw new IllegalArgumentException(
                "Image is too small (" + width + "x" + height + " px). " +
                "Minimum required size is " + MIN_DIMENSION + "x" + MIN_DIMENSION + " px."
            );
        }
        return image;
    }

    private byte[] processImage(BufferedImage original, String ext) throws IOException {
        int width = original.getWidth();
        int height = original.getHeight();

        // Scale down if either dimension exceeds the maximum
        int targetWidth = Math.min(width, MAX_DIMENSION);
        int targetHeight = Math.min(height, MAX_DIMENSION);

        // First attempt: high quality (85%)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(original)
                .size(targetWidth, targetHeight)
                .keepAspectRatio(true)
                .outputFormat(ext)
                .outputQuality(0.85)
                .toOutputStream(baos);

        byte[] imageBytes = baos.toByteArray();

        // If still over 2MB, reduce quality to 65%
        if (imageBytes.length > MAX_FILE_SIZE) {
            baos = new ByteArrayOutputStream();
            Thumbnails.of(original)
                    .size(targetWidth, targetHeight)
                    .keepAspectRatio(true)
                    .outputFormat(ext)
                    .outputQuality(0.65)
                    .toOutputStream(baos);
            imageBytes = baos.toByteArray();
        }

        return imageBytes;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "jpg";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
