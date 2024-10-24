package com.cykj.controller;

import com.cykj.service.QrCodeService;
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
//    @RequestMapping("/getQrCode")
    public void getQrCode(@RequestParam("file") MultipartFile file, String id,HttpServletRequest request, HttpServletResponse response){
        qrCodeService.getQrCode(file,request,response);
    }

}
