package com.cykj.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @className: QrCodeService
 * @author: 沈楠德
 * @date: 2024/09/28 16:38:27
 * @Version: 1.0
 * @description:
 */
public interface QrCodeService {
    public void getQrCode(MultipartFile file, HttpServletRequest request, HttpServletResponse response); //获取二维码，上传到七牛云
    public String getNewQrCode(MultipartFile file, int merchantNumber); //获取二维码，上传到七牛云
}
