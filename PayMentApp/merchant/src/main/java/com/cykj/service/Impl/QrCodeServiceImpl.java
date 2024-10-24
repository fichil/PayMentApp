package com.cykj.service.Impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.cykj.pojo.MerchantInfo;
import com.cykj.service.MerchantService;
import com.cykj.service.QrCodeService;

import com.cykj.utils.ConvertToMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QrCodeServiceImpl implements QrCodeService {

    @Autowired
    MerchantService merchantService;

    /**
     * @description: 获取二维码，存入七牛云
     * @param:
     * @return:
     */
    @Override
    public void getQrCode(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
//          部署到服务器上后要改成服务器本地磁盘的地址
        File file1 = new File("D:\\images\\" + UUID.randomUUID().toString() + ".png");
        System.out.println(file1.getPath());
        try {
            file.transferTo(file1);
            QrConfig qrConfig = new QrConfig();
            qrConfig.setImg(file1);
            BufferedImage bufferedImage = generate("http://s4w5h4.natappfree.cc/imgs/test.html", qrConfig, "png", response.getOutputStream());
            // 将bufferedImage写入字节数组输出流
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"jpg",baos);
            byte[] byteArray = baos.toByteArray();
            MultipartFile file2  = new ConvertToMultipartFile(byteArray, "test", "testtest", "png", byteArray.length);

//            创建测试MerchantInfo对象，后期要在token里面取对象
            MerchantInfo merchantInfo = new MerchantInfo();
            merchantInfo.setId(1);
            merchantService.nativeUpload(file2,merchantInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            file1.delete();
        }

    }

    /**
     * @description: 生成新的二维码，存入七牛云
     * @param:
     * @return:
     */
    @Override
    public String getNewQrCode(MultipartFile file, int merchantNumber) {
//          部署到服务器上后要改成服务器本地磁盘的地址
        File file1 = new File("D:\\images\\" + UUID.randomUUID().toString() + ".png");
        System.out.println(file1.getPath());
        try {
            file.transferTo(file1);
            QrConfig qrConfig = new QrConfig();
            qrConfig.setImg(file1);
            BufferedImage bufferedImage = generate("http://ccj.nat300.top/tohome?id="+merchantNumber, qrConfig, "png");
            // 将bufferedImage写入字节数组输出流
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"jpg",baos);
            byte[] byteArray = baos.toByteArray();
            MultipartFile file2  = new ConvertToMultipartFile(byteArray, "test", "testtest", "png", byteArray.length);

//            创建测试MerchantInfo对象，后期要在token里面取对象
            MerchantInfo merchantInfo = new MerchantInfo();
            merchantInfo.setId(1);
            merchantService.nativeUpload(file2,merchantInfo);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            file1.delete();
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
    public static BufferedImage generate(String content, QrConfig config, String imageType) {
        BufferedImage image = QrCodeUtil.generate(content, config);
        return image;
    }
}
