package com.cykj.service;

import com.cykj.pojo.MerchantInfo;
import com.cykj.utils.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @className: MerchantService
 * @author: 沈楠德
 * @date: 2024/09/26 17:28:18
 * @Version: 1.0
 * @description:
 */
public interface MerchantService {
    public ResponseDTO getAll();

    public String getUploadToken();
    public String getCoverUploadToken(String key);
    public ResponseDTO uploadResource(MultipartFile file, HttpServletRequest request);
    public ResponseDTO getResource(HttpServletRequest request) throws UnsupportedEncodingException;
    public ResponseDTO coverUploadResource(MultipartFile file, HttpServletRequest request);
    public ResponseDTO nativeUpload(MultipartFile file, String merchantNumber);
    public ResponseDTO getUserInfo(String token);
}
