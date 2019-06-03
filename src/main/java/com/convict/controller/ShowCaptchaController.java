package com.convict.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @Author Convict
 * @Date 2018/12/25 15:51
 */
@Controller
@RequestMapping("/show")
//@SessionAttributes(value = {"rightCaptcha"})
// 会引发Cannot create a session after the response has been committed异常，改用原生request
public class ShowCaptchaController {

    @RequestMapping("/captcha")
    public String show(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        int width = 80;
        int height = 20;
        BufferedImage img = new BufferedImage(width, height, 1);
        Graphics g = img.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("\u5b8b\u4f53", 1, 18));
        Date d = new Date();
        Random r = new Random(d.getTime());
        String rightCaptcha = "";
        int i = 0;
        while (i < 4) {
            int a = r.nextInt(10);
            rightCaptcha = rightCaptcha + a;
            int y = 10 + r.nextInt(10);
            Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            g.setColor(c);
            g.drawString("" + a, 5 + i * width / 4, y);
            ++i;
        }
        g.setColor(Color.GREEN);
        i = 0;
        while (i < 5) {
            int x1 = r.nextInt(width);
            int y1 = r.nextInt(height);
            int x2 = r.nextInt(width);
            int y2 = r.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
            ++i;
        }
        request.getSession().setAttribute("rightCaptcha", rightCaptcha);
        g.dispose();
        ImageIO.write(img, "JPG", response.getOutputStream());
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.flush();
        responseOutputStream.close();
        return null;
    }
}
