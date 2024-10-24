package com.cykj.controller;

import com.cykj.annotation.Logable;
import com.cykj.feign.PlatformServiceFeignClient;
import com.cykj.pojo.Admin;
import com.cykj.service.MerchantService;
import com.cykj.utils.ImageCodeUtils;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.CheckOutMerchantInfoVO;
import com.cykj.vo.LoginVo;
import com.cykj.vo.MerchantAddWithdrawOrderVo;
import com.cykj.vo.RegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/merchant")
public class MerchantController {
    //自动注入验证码生成器
    @Autowired
    private ImageCodeUtils imageCodeUtils;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private PlatformServiceFeignClient platformServiceFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/getAll")
    public ResponseDTO getAll(){
        ResponseDTO all = merchantService.getAll();
        return ResponseDTO.default_success(all);
    }

    @RequestMapping("/getUserInfo")
    public ResponseDTO getUserInfo(HttpServletRequest request){
        String token = request.getHeader("admin_token");
        ResponseDTO userInfo = merchantService.getUserInfo(token);
        return userInfo;
    }

    @RequestMapping("/getToken")
    public String getToken(){
        String uploadToken = merchantService.getUploadToken();
        return uploadToken;
    }

    @RequestMapping("/uploadResource")
    public ResponseDTO uploadResource(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        ResponseDTO responseDTO = merchantService.uploadResource(file,request);
        return responseDTO;
    }

    @RequestMapping("/coverUploadResource")
    public ResponseDTO coverUploadResource(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        ResponseDTO responseDTO = merchantService.coverUploadResource(file,request);
        return responseDTO;
    }

    @RequestMapping("/getResource")
    public ResponseDTO downloadResource(String merchantNum) throws UnsupportedEncodingException {
        ResponseDTO dto = merchantService.getResource(merchantNum);
        return dto;
    }

    /*
    * 商户登陆
    * */
    @RequestMapping("/login")
    public ResponseDTO login(@RequestBody LoginVo vo,HttpSession session,HttpServletRequest request){
//        Object o = redisTemplate.opsForValue().get(session.getId());
//        System.out.println("通过Redis取"+o);
        return merchantService.merchantLogin(vo,session,request);
    }

    /*
    * 商户的验证码
    * */
    @RequestMapping("/code")
    public void createCode(HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException {
        // 生成验证码的图像
        imageCodeUtils.createImage();
        // 获取验证码字符串
        String code = imageCodeUtils.getCode();
        request.setAttribute("trueCode",code);
        redisTemplate.opsForValue().set(session.getId(),code,5, TimeUnit.MINUTES);
        System.out.println("生成的验证码是：" + code);

        // 将验证码存储到当前用户的 Session 中
        session.setAttribute("verificationCode", code);  // 存入 session 中，键为 "verificationCode"

        // 将图像输出到响应的输出流
        imageCodeUtils.write(response.getOutputStream());
    }

    //注册账户
    @RequestMapping("/register")
    public ResponseDTO register(@RequestBody RegisterVo vo,HttpSession session){
        return merchantService.merchantRegister(vo,session);
    }

    @RequestMapping("/uploadMerchantIDCardFront")
    public ResponseDTO uploadMerchantIDCardFront(@RequestParam("merchantNumber") String merchantNumber,
                                                 @RequestParam("adminId") String adminId,
                                                 @RequestParam("file") MultipartFile file){
        return merchantService.uploadMerchantIDCardFront(merchantNumber, adminId,file);
    };
    @RequestMapping("/uploadMerchantIDCardReverse")
    public ResponseDTO uploadMerchantIDCardReverse(@RequestParam("merchantNumber") String merchantNumber,@RequestParam("adminId") String adminId,@RequestParam("file") MultipartFile file){
        return merchantService.uploadMerchantIDCardReverse(merchantNumber,adminId,file);
    };
    @RequestMapping("/uploadMerchantLicense")
    public ResponseDTO uploadMerchantLicense(@RequestParam("merchantNumber") String merchantNumber,@RequestParam("adminId") String adminId,@RequestParam("file") MultipartFile file){
        return merchantService.uploadMerchantLicense(merchantNumber,adminId, file);
    };

    @RequestMapping("/uploadMerchantInfo")
//    public ResponseDTO uploadMerchantInfo(@RequestParam("adminId") String adminId,
//                                         @RequestParam("legalPersonName") String legalPersonName,
//                                          @RequestParam("legalPersonPhone") String legalPersonPhone,
//                                          @RequestParam("qrImg") String qrImg){
        public ResponseDTO uploadMerchantInfo(@RequestBody CheckOutMerchantInfoVO vo){
        ResponseDTO dto = merchantService.uploadMerchantInfo(vo.getLegalPersonName(), vo.getLegalPersonPhone(), vo.getQrImg(), vo.getAdminId());
        return dto;
    };
    /**
     * 修改商户管理员信息
     * @param admin
     * @param response
     * @return
     */
    @Logable("商户端修改商户管理员信息")
    @RequestMapping("/updateAdmin")
    public ResponseDTO updateAdmin(@RequestBody Admin admin,HttpServletResponse response){
        if(admin.getId() == 1){
            return ResponseDTO.default_fail("非法的修改，没有权限");
        }
        return platformServiceFeignClient.updateAdmin(admin,response.getHeader("admin_token"));
    }






}
