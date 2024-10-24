package com.cykj.utils;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;

/**
 * @author 李璟瑜
 * @date 2024/7/17 9:34
 * @description: 加密
 */
@Component
public class Md5 {
    public static String getEncrypted(String pwd) {
        String encryptedPwd = null;
        try {
            // MessageDigest是一个加密类，用于计算字符串的哈希值
            MessageDigest md = MessageDigest.getInstance("MD5");// 使用MD5算法
            // 将密码字符串转换为字节数组
            byte[] passwordBytes = pwd.getBytes();
            // 计算MD5哈希值以字节数组的形式返回
            byte[] hashBytes = md.digest(passwordBytes);
            // 将字节数组转换为十六进制字符串
            // 创建一个StringBuilder对象来存储结果（拼接加密后的字符串）
            StringBuilder stringBuilder = new StringBuilder();
            // 遍历哈希值的字节数组，将每个字节转换为十六进制字符串，并添加到StringBuilder对象中
            for (byte b : hashBytes) {
                //将b转换为两位数的十六进制字符串表示形式
                stringBuilder.append(String.format("%02x", b));
            }
            encryptedPwd = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedPwd;
    }
}
