package com.ruoyi.common.utils.file;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.FileCopyUtils;

import java.io.*;

public class WatermarkedMultipartFile implements MultipartFile {
    private final File file;
    private final String originalFilename;

    public WatermarkedMultipartFile(File file, String originalFilename) {
        this.file = file;
        this.originalFilename = originalFilename;
    }

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        // 根据文件扩展名返回适当的content type
        String fileName = getOriginalFilename();
        if (fileName != null) {
            if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
                return "image/jpeg";
            } else if (fileName.toLowerCase().endsWith(".png")) {
                return "image/png";
            } else if (fileName.toLowerCase().endsWith(".gif")) {
                return "image/gif";
            }
        }
        return "image/jpeg"; // 默认
    }

    @Override
    public boolean isEmpty() {
        return file.length() == 0;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return org.springframework.util.FileCopyUtils.copyToByteArray(file);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        FileCopyUtils.copy(file, dest);
    }
}
