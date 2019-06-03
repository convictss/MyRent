package com.convict.util.site;

import org.apache.fontbox.ttf.CmapSubtable;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Convict
 * @Date 2019/1/27 22:31
 */
public class FiveEightTongChengUtil {

    /**
     * 返回解密后double类型的价格
     * @param base64Str base64编码字符
     * @param cipher 对应密文
     * @return
     */
    public static String decodeByBase64(String base64Str, String cipher) {
        List<String> mapper = getMapper(base64Str);
        String decryption = "";
        for (int i = 0; i < cipher.length(); i++) {
            String curStr = cipher.substring(i, i + 1);
            if (mapper.contains(curStr)) {
                decryption += mapper.indexOf(curStr);
            } else {
                decryption += curStr;
            }
        }
        return decryption;
    }

    /**
     * 返回base64解密后的数字对应中文列表
     * @param base64Str
     * @return
     */
    public static List<String> getMapper(String base64Str) {
        List<String> dic = new ArrayList<>();
        BASE64Decoder decoder = new BASE64Decoder();
        TTFParser parser = new TTFParser();
        try {
            byte[] bytes = decoder.decodeBuffer(base64Str);
            InputStream inputStream = new ByteArrayInputStream(bytes);

            // 二进制输入流解析
            TrueTypeFont ttf = parser.parse(inputStream);
            if (ttf != null && ttf.getCmap() != null && ttf.getCmap().getCmaps() != null
                    && ttf.getCmap().getCmaps().length > 0) {
                CmapSubtable[] tables = ttf.getCmap().getCmaps();
                CmapSubtable table = tables[0];// No matter what
                for (int i = 1; i <= 10; i++) {
                    // getCharacterCode deprecated
                    // dic.add(String.valueOf((char) (int) (table.getCharacterCode(i))));
                    dic.add(String.valueOf((char) (int) (table.getCharCodes(i).get(0))));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dic;
    }

    /**
     * 获取房子类型
     * @param origin
     * @return
     */
    public static String getHouseTypeByStr(String origin) {
        String houseType = origin.substring(0, origin.indexOf(" "));
        return houseType;
    }

    public static String getAreaByStr(String origin) {
        String area = origin.substring(origin.indexOf("   ") + 3, origin.lastIndexOf("平") + 1);
        return area;
    }

    public static String getFloorByStr(String origin) {
        String floor = origin.substring(origin.indexOf("  ") + 2);
        return floor;
    }
}
