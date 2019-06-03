package com.convict.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.List;

public class FileUtil {

    /**
     * 批量下载图片
     *
     * @param urls
     * @param filePath
     * @throws IOException
     */
    public static void downloadFileByUrls(List<String> urls, String filePath) {
        try {
            for (int i = 0; i < urls.size(); i++) {
                String url = urls.get(i);
                Connection.Response response = Jsoup.connect(url).ignoreContentType(true).execute();
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                try {
                    byte[] img = response.bodyAsBytes();
                    File dir = new File(filePath);
                    // 目录不存在新建目录
                    if (!dir.exists() && !dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    File file = new File(filePath + "\\\\" + i + ".jpg");
                    fos = new FileOutputStream(file);
                    bos = new BufferedOutputStream(fos);
                    bos.write(img);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null) {
                        bos.close();
                    }
                    if (fos != null) {

                        fos.close();

                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载单个图片
     *
     * @param url
     * @param filePath
     * @throws IOException
     */
    public static void downloadFileByUrl(String url, String filePath, String fileName) throws IOException {
        Connection.Response response = Jsoup.connect(url).ignoreContentType(true).execute();
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            byte[] img = response.bodyAsBytes();
            File dir = new File(filePath);
            // 目录不存在新建目录
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
            }
            File file = new File(filePath + "\\\\" + fileName + ".jpg");
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(img);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 二进制流转图片并保存
     * @param bytes
     * @param filePath
     * @throws IOException
     */
    public static void saveFileByBytes(byte[] bytes, String filePath) throws IOException {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File file = new File(filePath);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 复制图片
     * @param source
     * @param target
     * @throws IOException
     */
    public static void copyFileBySourceAndTarget(String source, String target) throws IOException {
        File sourceFile = new File(source);
        File targetFile = new File(target);
        InputStream ins = null;
        OutputStream ous = null;
        try {
            ins = new FileInputStream(sourceFile);
            ous = new FileOutputStream(targetFile);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = ins.read(buf)) != -1) {
                ous.write(buf, 0, bytesRead);
            }
        } finally {
            ins.close();
            ous.close();
        }
    }

    /**
     * 删除图片
     * @param path
     */
    public static void deleteFileByPath(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                for (File temp : dir.listFiles()) {
                    if (temp.isDirectory()) {
                        deleteFileByPath(path + "/" + temp.getName());
                    } else {
                        temp.delete();
                    }
                }
            } else {
                dir.delete();
            }
            dir.delete();
        }
    }

}
