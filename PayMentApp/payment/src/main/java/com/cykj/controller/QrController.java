package com.cykj.controller;

import com.cykj.service.QrCodeService;
import com.cykj.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class QrController {

    @Autowired
    QrCodeService qrCodeService;

    @RequestMapping("/getQrCode")
    public ResponseDTO getQrCode(String merchantNumber){
        ResponseDTO dto = qrCodeService.getQrCode(merchantNumber);
        return dto;
    }
}
