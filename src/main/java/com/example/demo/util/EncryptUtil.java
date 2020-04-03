package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @version V1.0.0
 * @ClassName: {@link EncryptUtil}
 * @Description: md5加密
 * @author: 兰州
 * @date: 2019/7/16 10:12
 * @Copyright:2019 All rights reserved.
 */
public class EncryptUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String encoderPwdByMd5(String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            String encodingCharToUse = Charset.defaultCharset().name();
            LOGGER.warn("Using {} as the character encoding algorithm to update the digest", encodingCharToUse);
            messageDigest.update(password.getBytes(encodingCharToUse));
            byte[] digest = messageDigest.digest();
            return getFormattedText(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4 & 0xF)]);
            buf.append(HEX_DIGITS[(aByte & 0xF)]);
        }
        return buf.toString();
    }


    /*public static void main(String[] args) {
        System.out.println(encoderPwdByMd5("123456"));
    }*/


}
