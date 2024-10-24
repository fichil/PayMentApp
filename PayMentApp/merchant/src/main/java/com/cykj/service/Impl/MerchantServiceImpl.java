package com.cykj.service.Impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.cykj.mapper.MerchantAdminInfo;
import com.cykj.mapper.MerchantMerchantInfoMapper;
import com.cykj.mapper.MerchantMerchantMapper;
import com.cykj.pojo.Admin;
import com.cykj.pojo.CheckOut;
import com.cykj.pojo.MerchantInfo;
import com.cykj.pojo.MerchantInfoAndAdmin;
import com.cykj.service.MerchantService;
import com.cykj.utils.*;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.LoginVo;
import com.cykj.vo.RegisterVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MerchantServiceImpl implements MerchantService {
    //    Redis工具
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    MerchantMerchantInfoMapper merchantMerchantInfoMapper;

    @Autowired
    MerchantMerchantMapper merchantMerchantMapper;

    @Autowired
    MerchantAdminInfo merchantAdminInfo;

    /**
     * @description: 获取所有商户
     * @param: 无
     * @return: 返回所有商户信息
     */
    public ResponseDTO getAll() {
        Page<Object> page = PageHelper.startPage(0, 5);
        merchantMerchantMapper.getAll();
        PageInfo<Object> pageInfo = new PageInfo<>(page);
        PageResult pageResult = PageUtil.initPageResult(pageInfo);
        return ResponseDTO.default_success(pageResult);
    }

    /**
     * @description: 获取七牛云覆盖上传凭证
     * @param: 无
     * @return: 根据是否获取到token返回DTO
     */
    @Override
    public String getCoverUploadToken(String key) {
        String ak = QiNiuOSS.ak;
        String sk = QiNiuOSS.sk;
        String bucket = QiNiuOSS.bucketName;

        Auth auth = Auth.create(ak, sk);
        String token = auth.uploadToken(bucket,key);
        return token;
    }

    /**
     * @description: 获取七牛云简单上传凭证
     * @param: 无
     * @return: 根据是否获取到token返回DTO
     */
    @Override
    public String getUploadToken() {
        String ak = QiNiuOSS.ak;
        String sk = QiNiuOSS.sk;
        String bucket = QiNiuOSS.bucketName;

        Auth auth = Auth.create(ak, sk);
        String token = auth.uploadToken(bucket);
        return token;
    }

    /**
     * @description: 上传简单文件到七牛云
     * @param:
     * @return:
     */
    @Override
    public ResponseDTO uploadResource(MultipartFile file, HttpServletRequest request) {
        // 构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.useHttpsDomains = false;
        // 上传对象
        UploadManager uploadManager = new UploadManager(cfg);

        // key指定文件名，在http请求中获取token并解析获取商户id
//        String key = request.getHeader("token");
        String key = "001qr";
        // 生成上传凭证，然后准备上传
        String upToken = getUploadToken();

        InputStream inputStream = null;
        byte[] byteArray = null;
        try {
            inputStream = file.getInputStream();
            byteArray = IOUtils.toByteArray(inputStream);
            try {
                Response response = uploadManager.put(byteArray, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);
                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
            return ResponseDTO.default_success();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseDTO.default_fail();
        }

    }

    /**
     * @description: 覆盖上传文件到七牛云
     * @param:
     * @return:
     */
    @Override
    public ResponseDTO coverUploadResource(MultipartFile file, HttpServletRequest request) {
        // 构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.useHttpsDomains = false;
        // 上传对象
        UploadManager uploadManager = new UploadManager(cfg);

        // key指定文件名，在http请求中获取token并解析获取商户id
//        String key = request.getHeader("token");
        String key = "001qr";
        // 生成覆盖上传凭证，然后准备上传
        String upToken = getCoverUploadToken(key);

        InputStream inputStream = null;
        byte[] byteArray = null;
        try {
            inputStream = file.getInputStream();
            byteArray = IOUtils.toByteArray(inputStream);
            try {
                Response response = uploadManager.put(byteArray, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);
                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
            return ResponseDTO.default_success();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseDTO.default_fail();
        }
    }

    /**
     * @description: 审核时图片上传，自定义名称
     * @param: file 图片，merchantNumber 商户编号，key1 自定义名称
     * @return:
     */
    @Override
    public ResponseDTO auditUpload(@NotNull MultipartFile file, String key1) {
        if (file.isEmpty()){
            System.out.println("file为空");
        }
        System.out.println(file.getSize());

        // 构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.useHttpsDomains = false;
        // 上传对象
        UploadManager uploadManager = new UploadManager(cfg);

        // key指定文件名，在http请求中获取token并解析获取商户id
//        String key = request.getHeader("token");
        String key = key1;
        // 生成覆盖上传凭证，然后准备上传
        String upToken = getCoverUploadToken(key);

//        InputStream inputStream = null;
        byte[] byteArray = null;
        try {
//            inputStream = file.getInputStream();
//            byteArray = IOUtils.toByteArray(inputStream);
            byteArray = file.getBytes();
            try {
                Response response = uploadManager.put(byteArray, key, upToken);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);
                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
            return ResponseDTO.default_success("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseDTO.default_fail("上传失败");
        }
    }


    /**
     * @description: 查看图片，用于商户获取二维码
     * @param: HttpServletRequest request，用于从请求中获取图片名
     * @return: 返回获取图片名的有效链接，有效期10分钟
     */
    @Override
    public ResponseDTO getResource(String merchantNum) {
//        String fileName = request.getParameter("finaName");
        String fileName = merchantNum;
        String domainOfBucket = QiNiuOSS.domainName;
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);

        String accessKey = QiNiuOSS.ak;
        String secretKey = QiNiuOSS.sk;
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 600;//链接过期时间，以秒为单位，这里设置为10分钟内有效。
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(finalUrl);
        return ResponseDTO.default_success(finalUrl);

    }
/**
 * 下载示例
 * DownloadUrl url = new DownloadUrl(domain, useHttps, key);
 * url.setAttname(attname) // 配置 attname
 *    .setFop(fop) // 配置 fop
 *    .setStyle(style, styleSeparator, styleParam) // 配置 style
 * // 带有效期
 * long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
 * long deadline = System.currentTimeMillis()/1000 + expireInSeconds;
 * Auth auth = Auth.create("your access key", "your secret key");
 * String urlString = url.buildURL(auth, deadline);
 * System.out.println(urlString
 */
    /**
     * @description: 本地上传，目前只用于上传二维码
     * @param:
     * @return:
     */
    @Override
    public ResponseDTO nativeUpload(MultipartFile file, MerchantInfo merchantInfo) {
        // 构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.useHttpsDomains = false;
        // 上传对象
        UploadManager uploadManager = new UploadManager(cfg);

        // key指定文件名，在http请求中获取token并解析获取商户id
        String key = merchantInfo.getId() + "qrcode";
        // 生成上传凭证，然后准备上传
        String upToken = getUploadToken();

        InputStream inputStream = null;
        byte[] byteArray = null;
        try {
            inputStream = file.getInputStream();
            byteArray = IOUtils.toByteArray(inputStream);
            try {
                Response response = uploadManager.put(byteArray, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                ex.printStackTrace();
//                return ResponseDTO.default_fail("图片重复上传");
                if (ex.response != null) {
                    System.err.println(ex.response);
                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
            return ResponseDTO.default_success();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseDTO.default_fail();
        }
    }

    /**
     * @description: 解析token获取当前用户信息
     * @param: token
     * @return: 返回包含id,nickName.roleId的Admin对象
     */
    @Override
    public ResponseDTO getUserInfo(String token) {
        Claims claims = MerchantJwtUtils.parseToken(token);
        //取得ID
        Integer id = (Integer) claims.get("id");
        //获得数据
        MerchantInfoAndAdmin userInfo = merchantMerchantInfoMapper.getUserInfo(id);

        //覆盖真实的roleId
        userInfo.setId(id);

        return ResponseDTO.default_success(userInfo);
    }

    /**
     * @description: 商户登陆
     * @param: loginVo用户输入的内容
     * @return: 返回token
     */
    @Override
    public ResponseDTO merchantLogin(LoginVo vo, HttpSession session,HttpServletRequest request) {
        String trueCode = request.getHeader("trueCode");
        if (trueCode == null){
            // 从 Session 中获取存储的验证码
//            String verificationCode = (String) session.getAttribute("verificationCode");
            //清除session中的验证码
//            session.removeAttribute("verificationCode");
            //从redis里取验证码
            Object o = redisTemplate.opsForValue().get(session.getId());
            String verificationCode = o.toString();

            // 判断验证码是否与用户输入的匹配
            if (verificationCode == null || !verificationCode.equalsIgnoreCase(vo.getCode())) {
                // 如果验证码为空或者验证码不匹配，返回 dto
                System.out.println("验证码不正确");
                return new ResponseDTO(-300,"验证码不正确",null);
            }
        }


        String password = Md5.getEncrypted(vo.getPassword());
        vo.setPassword(password);
        Admin admin = merchantMerchantMapper.merchantLogin(vo);

        //账号密码错误
        if (admin == null || admin.getId() == null) {
            System.out.println("账号或密码错误");
            return new ResponseDTO(-200,"账号或密码错误",null);
        }
        //账号冻结
        if (admin.getState() != 1){
            return new ResponseDTO(-400,"账号冻结，请联系管理员",null);
        }

        //验证成功
        admin.setPassword(null);//隐藏密码
        HashMap<String,Object> data = new HashMap<>();
        data.put("id",admin.getId());
        redisTemplate.delete(session.getId());
        String token = MerchantJwtUtils.generateToken(data);

        //设置过去时间
        redisTemplate.opsForValue().set(token, JSON.toJSONString(admin),30, TimeUnit.MINUTES);

        return new ResponseDTO(200,"登陆成功",token);
    }

    @Override
    public ResponseDTO merchantRegister(RegisterVo vo, HttpSession session) {
        // 从 Session 中获取存储的验证码
        String verificationCode = (String) session.getAttribute("verificationCode");
        //清除session中的验证码
        session.removeAttribute("verificationCode");

        // 判断验证码是否与用户输入的匹配
        if (verificationCode == null || !verificationCode.equalsIgnoreCase(vo.getCode())) {
            // 如果验证码为空或者验证码不匹配，返回 dto
            System.out.println("验证码不正确");
            return new ResponseDTO(-300,"验证码不正确",null);
        }

        //检测是否有这个账号了
        Admin admin = merchantAdminInfo.getAdmin(vo);
        if (admin != null){
            return new ResponseDTO(-600,"账号已存在",null);
        }
        vo.setPassword(Md5.getEncrypted(vo.getPassword()));
        merchantAdminInfo.addAdmin(vo);
        Admin newAdmin = merchantAdminInfo.getAdmin(vo);

        HashMap<String,Object> data = new HashMap<>();
        data.put("id",newAdmin.getId());
        String token = MerchantJwtUtils.generateToken(data);

        //设置过去时间
        redisTemplate.opsForValue().set(token, JSON.toJSONString(admin),30, TimeUnit.MINUTES);

        return new ResponseDTO(200,"登陆成功",token);
    }


    @Override
    public ResponseDTO uploadMerchantIDCardFront(String merchantNumber,String adminId, MultipartFile file) {
        File file1 = new File("D:\\images\\"+UUID.randomUUID().toString() + ".jpg");
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
            file1.delete();
        }
        try {
            int ocr = AliCloudOCR.ocr(file1);
            if (ocr == 1) {
                String fileName = merchantNumber+"IDCardFront"+ UUID.randomUUID().toString();
                String fileName1 = QiNiuOSS.domainName+fileName;
                CheckOut one = merchantMerchantMapper.getOne(adminId);
                if (one == null){
                    int i = merchantMerchantMapper.insertAdminId(adminId);
                    if (i > 0){
                        int i1 = merchantMerchantMapper.updateIDCardFrontMsg(fileName1, adminId);
                        if (i1 > 0){
                            MultipartFile multipartFile = ConvertToMultipartFile.ConvertFileToMultipartFile(file1);
                            ResponseDTO dto = null;
                            if (multipartFile != null){
                                dto = auditUpload(multipartFile, fileName);
                            } else {
                                return ResponseDTO.default_fail("内部错误，请联系工作人员");
                            }
                            if (dto.getCode() == 200){
                                return dto;
                            }else {
                                int i2 = merchantMerchantMapper.rollbackIDCardFrontMsg(adminId);
                                if (i2 > 0){
                                    return ResponseDTO.default_fail("上传失败，数据已回滚");
                                }else {
                                    return ResponseDTO.default_fail("上传失败，数据未回滚，请重新上传覆盖");
                                }
                            }
                        }else {
                            return ResponseDTO.default_fail("上传失败，请重新尝试");
                        }
                    }else {
                        return ResponseDTO.default_fail("上传失败，请重新尝试");
                    }

                }else {
                    int i1 = merchantMerchantMapper.updateIDCardFrontMsg(fileName1, adminId);
                    if (i1 > 0){
                        MultipartFile multipartFile = ConvertToMultipartFile.ConvertFileToMultipartFile(file1);
                        ResponseDTO dto = null;
                        if (multipartFile != null){
                            dto = auditUpload(multipartFile, fileName);
                        } else {
                            return ResponseDTO.default_fail("内部错误，请联系工作人员");
                        }
                        if (dto.getCode() == 200){
                            return dto;
                        }else {
                            int i = merchantMerchantMapper.rollbackIDCardFrontMsg(adminId);
                            if (i > 0){
                                return ResponseDTO.default_fail("上传失败，数据已回滚");
                            }else {
                                return ResponseDTO.default_fail("上传失败，数据未回滚，请重新上传覆盖");
                            }
                        }

                    }else {
                        return ResponseDTO.default_fail("上传失败，请重新尝试");
                    }
                }
            }else if (ocr == 0){
                return ResponseDTO.default_fail("OCR接口初始化有误，请重新上传");
            }else {
                return ResponseDTO.default_fail("OCR识别结果为非身份证件，请上传正确的身份证件图片");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.default_fail("出现异常");
        }finally {
            file1.delete();
        }

    }


    @Override
    public ResponseDTO uploadMerchantIDCardReverse(String merchantNumber,String adminId, MultipartFile file) {
//        File file1 = new File("D:\\images\\"+UUID.randomUUID().toString() + ".jpg");
        File file1 = null;
        try {
            file1 = File.createTempFile(UUID.randomUUID().toString() , ".jpg");
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
            file1.delete();
        }

        try {
            int ocr = AliCloudOCR.ocr(file1);
            if (ocr == 1) {
                String fileName = QiNiuOSS.domainName+merchantNumber + "IDCardReverse" + UUID.randomUUID().toString();
                String fileName1 = QiNiuOSS.domainName+fileName;
                CheckOut one = merchantMerchantMapper.getOne(adminId);
                if (one == null){
                    int i = merchantMerchantMapper.insertAdminId(adminId);
                    if (i > 0){
                        int i1 = merchantMerchantMapper.updateIDCardBackMsg(fileName1, adminId);
                        if (i1 > 0){
                            MultipartFile multipartFile = ConvertToMultipartFile.ConvertFileToMultipartFile(file1);
                            ResponseDTO dto = null;
                            if (multipartFile != null){
                                dto = auditUpload(multipartFile, fileName);
                            } else {
                                return ResponseDTO.default_fail("内部错误，请联系工作人员");
                            }

                            if (dto.getCode() == 200){
                                return dto;
                            }else {
                                int i2 = merchantMerchantMapper.rollbackIDCardBackMsg(adminId);
                                if (i2 > 0){
                                    return ResponseDTO.default_fail("上传失败，数据已回滚");
                                }else {
                                    return ResponseDTO.default_fail("上传失败，数据未回滚，请重新上传覆盖");
                                }
                            }
                        }else {
                            return ResponseDTO.default_fail("上传失败，请重新尝试");
                        }
                    }else {
                        return ResponseDTO.default_fail("上传失败，请重新尝试");
                    }

                }else {
                    int i1 = merchantMerchantMapper.updateIDCardBackMsg(fileName1, adminId);
                    if (i1 > 0){
                        MultipartFile multipartFile = ConvertToMultipartFile.ConvertFileToMultipartFile(file1);
                        ResponseDTO dto = null;
                        if (multipartFile != null){
                            dto = auditUpload(multipartFile, fileName);
                        } else {
                            return ResponseDTO.default_fail("内部错误，请联系工作人员");
                        }
                        if (dto.getCode() == 200){
                            return dto;
                        }else {
                            int i = merchantMerchantMapper.rollbackIDCardBackMsg(adminId);
                            if (i > 0){
                                return ResponseDTO.default_fail("上传失败，数据已回滚");
                            }else {
                                return ResponseDTO.default_fail("上传失败，数据未回滚，请重新上传覆盖");
                            }
                        }

                    }else {
                        return ResponseDTO.default_fail("上传失败，请重新尝试");
                    }
                }
            }else if (ocr == 0){
                return ResponseDTO.default_fail("OCR接口初始化有误，请重新上传");
            }else {
                return ResponseDTO.default_fail("OCR识别结果为非身份证件，请上传正确的身份证件图片");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.default_fail("出现异常");
        }finally {
            file1.delete();
        }

    }

    @Override
    public ResponseDTO uploadMerchantLicense(String merchantNumber,String adminId, MultipartFile file) {
        String fileName = merchantNumber+"License"+ UUID.randomUUID().toString();
        String fileName1 = QiNiuOSS.domainName+fileName;
        CheckOut one = merchantMerchantMapper.getOne(adminId);
        if (one == null){
            int i = merchantMerchantMapper.insertAdminId(adminId);
            if (i > 0){
                int i1 = merchantMerchantMapper.updateLicenseMsg(fileName1, adminId);
                if (i1 > 0){
                    ResponseDTO dto = auditUpload(file, fileName);
                    if (dto.getCode() == 200){
                        return dto;
                    }else {
                        int i2 = merchantMerchantMapper.rollbackLicenseMsg(adminId);
                        if (i2 > 0){
                            return ResponseDTO.default_fail("上传失败，数据已回滚");
                        }else {
                            return ResponseDTO.default_fail("上传失败，数据未回滚，请重新上传覆盖");
                        }
                    }
                }else {
                    return ResponseDTO.default_fail("上传失败，请重新尝试");
                }
            }else {
                return ResponseDTO.default_fail("上传失败，请重新尝试");
            }

        }else {
            int i1 = merchantMerchantMapper.updateLicenseMsg(fileName1, adminId);
            if (i1 > 0){
                ResponseDTO dto = auditUpload(file, fileName);
                if (dto.getCode() == 200){
                    return dto;
                }else {
                    int i = merchantMerchantMapper.rollbackLicenseMsg(adminId);
                    if (i > 0){
                        return ResponseDTO.default_fail("上传失败，数据已回滚");
                    }else {
                        return ResponseDTO.default_fail("上传失败，数据未回滚，请重新上传覆盖");
                    }
                }

            }else {
                return ResponseDTO.default_fail("上传失败，请重新尝试");
            }
        }
    }

    @Override
    public ResponseDTO uploadMerchantInfo(String legalPersonName, String legalPersonPhone, String qrImg,String adminId) {
        CheckOut one = merchantMerchantMapper.getOne(adminId);
        System.out.println("one:"+one);
        if (one == null){
            merchantMerchantMapper.insertAdminId(adminId);
            int i = merchantMerchantMapper.updateMerchantInfo(legalPersonName, legalPersonPhone, qrImg, adminId);
            if (i > 0){
                return ResponseDTO.default_success("商户信息上传成功");
            }else {
                return ResponseDTO.default_fail("上传失败，请重新尝试");
            }

        }else {
            int i = merchantMerchantMapper.updateMerchantInfo(legalPersonName, legalPersonPhone, qrImg, adminId);
            if (i > 0){
                return ResponseDTO.default_success("商户信息上传成功");
            }else {
                return ResponseDTO.default_fail("上传失败，请重新尝试");
            }
        }
    }

}
