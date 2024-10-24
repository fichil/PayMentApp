package com.cykj.service;

import com.cykj.pojo.MerchantInfo;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.LoginVo;
import com.cykj.vo.RegisterVo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public ResponseDTO coverUploadResource(MultipartFile file, HttpServletRequest request);
    public ResponseDTO auditUpload(MultipartFile file,String key);
    public ResponseDTO getResource(String merchantNum) throws UnsupportedEncodingException;
    public ResponseDTO nativeUpload(MultipartFile file, MerchantInfo merchantInfo);
    public ResponseDTO getUserInfo(String token);

    public ResponseDTO merchantLogin(LoginVo vo, HttpSession session,HttpServletRequest request);

    ResponseDTO merchantRegister(RegisterVo vo, HttpSession session);

    public ResponseDTO uploadMerchantIDCardFront(String merchantNumber,String adminId,MultipartFile file);
    public ResponseDTO uploadMerchantIDCardReverse(String merchantNumber,String adminId,MultipartFile file);
    public ResponseDTO uploadMerchantLicense(String merchantNumber,String adminId,MultipartFile file);

    public ResponseDTO uploadMerchantInfo(String legalPersonName, String legalPersonPhone,
                                          String qrImg, String adminId);

}
