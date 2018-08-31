/**
 * @(#)KaptchaController.java
 *
 * @author huawei
 * @version 1.0 2015-4-24
 *
 * Copyright (C) 2012,2015 , PING' AN, Inc.
 */
package com.jutongji.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.jutongji.util.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * Purpose:
 * 
 * @see
 * @since 1.1.0
 */
@Controller
@RequestMapping("kaptcha")
public class KaptchaController
{
    @Autowired
    private Producer captchaProducer = null;

    public Data<?> getCode(HttpSession session)
    {
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        return Data.success(capText);
    }

    @RequestMapping("code")
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        HttpSession session = request.getSession();
        // String code =
        // (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        // create the text for the image
        String capText = captchaProducer.createText();
        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try
        {
            out.flush();
            response.flushBuffer();
        }
        finally
        {
            out.close();
        }
        return null;
    }
}

/**
 * $Log: KaptchaController.java,v $
 * 
 * @version 1.0 2015-4-24
 */
