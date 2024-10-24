package com.cykj.service;

import com.cykj.utils.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;



/**
 * @className: QrCodeService
 * @author: 沈楠德
 * @date: 2024/09/28 16:38:27
 * @Version: 1.0
 * @description:
 */
public interface QrCodeService {
    public ResponseDTO getQrCode(String merchantNumber); //获取新的二维码，上传到七牛云
}
