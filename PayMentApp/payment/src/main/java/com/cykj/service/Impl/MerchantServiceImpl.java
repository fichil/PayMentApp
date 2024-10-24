package com.cykj.service.Impl;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.cykj.mapper.PaymentMerchantInfoMapper;
import com.cykj.mapper.PaymentMerchantMapper;
import com.cykj.service.MerchantService;
import com.cykj.utils.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    PaymentMerchantInfoMapper paymentMerchantInfoMapper;

    @Autowired
    PaymentMerchantMapper paymentMerchantMapper;

    /**
     * @description: 获取所有商户
     * @param: 无
     * @return: 返回所有商户信息
     */
    public ResponseDTO getAll() {
        Page<Object> page = PageHelper.startPage(0, 5);
        paymentMerchantMapper.getAll();
        PageInfo<Object> pageInfo = new PageInfo<>(page);
        PageResult pageResult = PageUtil.initPageResult(pageInfo);
        return ResponseDTO.default_success(pageResult);
    }

    /**
     * @description: 获取七牛云上传凭证
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
     * @description: 上传文件到七牛云
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
        String key = request.getHeader("token");
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
     * @description: 查看图片，用于商户获取二维码
     * @param: HttpServletRequest request，用于从请求中获取图片名
     * @return: 返回获取图片名的有效链接，有效期10分钟
     */
    @Override
    public ResponseDTO getResource(HttpServletRequest request) {
//        String fileName = request.getParameter("finaName");
        String fileName = "1qrcode";
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
     * @description: 本地上传，目前只用于上传二维码。
     * @param:
     * @return:
     */
    @Override
    public ResponseDTO nativeUpload(MultipartFile file, String merchantNumber) {
        // 构造一个带指定Region对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        cfg.useHttpsDomains = false;
        // 上传对象
        UploadManager uploadManager = new UploadManager(cfg);

        // key指定文件名，在http请求中获取token并解析获取商户id
        String key = merchantNumber + "qr" + UUID.randomUUID().toString();
        // 生成覆盖上传凭证，准备上传
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
            String outerChainName = "http://slomdt3kd.hn-bkt.clouddn.com/"+key;
            System.out.println(outerChainName);
//            存储访问链接到表
            paymentMerchantMapper.updateOuterChainName(merchantNumber,outerChainName);
            return ResponseDTO.default_success(outerChainName);
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
        Claims claims = JwtUtils.parseToken(token);
        return ResponseDTO.default_success();
    }

}
