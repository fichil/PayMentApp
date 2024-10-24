package com.cykj.utils;

import com.qiniu.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
/**
 * @className: ConvertToMultipartFile
 * @author: 沈楠德
 * @date: 2024/09/28 23:13:43
 * @Version: 1.0
 * @description:
 */
public class ConvertToMultipartFile implements MultipartFile {
    private byte[] fileBytes;
    String name;
    String originalFilename;
    String contentType;
    boolean isEmpty;
    long size;

    public ConvertToMultipartFile(byte[] fileBytes, String name, String originalFilename, String contentType,
                                  long size) {
        this.fileBytes = fileBytes;
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.size = size;
        this.isEmpty = false;
    }

    public static MultipartFile ConvertFileToMultipartFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] byteArray = IOUtils.toByteArray(fileInputStream);
            MultipartFile multipartFile = new ConvertToMultipartFile(byteArray, "1", "1", "png", byteArray.length);
            return multipartFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileBytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileBytes);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(fileBytes);
    }
}
