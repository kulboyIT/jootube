package me.weekbelt.wetube.infra.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

public class VideoFile {
    public static String makeInherenceFile(String originalName) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + "_" + originalName;
    }

    public static void saveVideo(MultipartFile videoMultipartFile, String saveAddr) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(saveAddr);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             FileInputStream fileInputStream = (FileInputStream) videoMultipartFile.getInputStream();
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

            int readCount;
            byte[] buffer = new byte[1024];

            while ((readCount = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, readCount);
            }

        } catch (Exception e) {
            throw new RuntimeException("Video Save Error");
        }
    }
}
