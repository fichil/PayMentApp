package com.cykj.service.Impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.cykj.mapper.PaymentMerchantMapper;
import com.cykj.service.MerchantService;
import com.cykj.service.QrCodeService;
import com.cykj.utils.ConvertToMultipartFile;
import com.cykj.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

@Service
public class QrCodeServiceImpl implements QrCodeService {

    @Autowired
    MerchantService merchantService;

    @Autowired
    PaymentMerchantMapper paymentMerchantMapper;

    /**
     * @description: 生成新的二维码，存入七牛云
     * @param: merchantNumber：商户编号
     * @return: 图片外链接
     */
    @Override
    public ResponseDTO getQrCode(String merchantNumber) {
//        File file = new File("/home/logo.png");
        URL resourceUrl = QrCodeServiceImpl.class.getClassLoader().getResource("images/logo.png");

        try {
            File file = new File(resourceUrl.toURI());
            QrConfig qrConfig = new QrConfig();
            qrConfig.setImg(file);
            //上服务器后要改成服务器地址
            BufferedImage bufferedImage = generate("http://ccj.nat300.top/tohome?id="+merchantNumber+"&v="+UUID.randomUUID(), qrConfig);
            // 将bufferedImage写入字节数组输出流
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"jpg",baos);
            byte[] byteArray = baos.toByteArray();
            MultipartFile file2  = new ConvertToMultipartFile(byteArray, "test", "testtest", "png", byteArray.length);
            ResponseDTO dto = merchantService.nativeUpload(file2, String.valueOf(merchantNumber));
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @description: 改造generate，使其写回后返回生成的二维码
     * @param: String content 扫描跳转的地址, QrConfig config 宽高和图片, String imageType 图片后缀, OutputStream out 输出流
     * @return: 返回生成的二维码
     */
    public static BufferedImage generate(String content, QrConfig config, String imageType, OutputStream out) {
        BufferedImage image = QrCodeUtil.generate(content, config);
        ImgUtil.write(image, imageType, out);
        return image;
    }

    /**
     * @description: 改造generate，使其返回生成的二维码
     * @param: String content 扫描跳转的地址, QrConfig config 宽高和图片, String imageType 图片后缀
     * @return: 返回生成的二维码
     */
    public static BufferedImage generate(String content, QrConfig config) {
        BufferedImage image = QrCodeUtil.generate(content, config);
        return image;
    }
}
