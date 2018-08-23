package com.jutongji.controller;

import com.jutongji.dto.UserLogin;
import com.jutongji.exception.ServiceException;
import com.jutongji.model.User;
import com.jutongji.service.IUserService;
import com.jutongji.session.UserSession;
import com.jutongji.session.UserSessionFactory;
import com.jutongji.util.CookieUtil;
import com.jutongji.util.RequestUtil;
import com.jutongji.util.ResultJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/8/23 14:33
 */
@Controller
public class UserController {

    private static final String loginView = "login";
    @Autowired
    private IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    protected final static String errorInfo = "errorInfo";

    @RequestMapping("/ajax/login")
    @ResponseBody
    public ResultJson ajaxLogin(@Valid UserLogin userReg, BindingResult bindingResult, Model model, HttpServletRequest request, HttpSession session) {
        ResultJson resultJson = new ResultJson();
        if (bindingResult.hasErrors()) {
            resultJson.setStatusCode(ResultJson.STATUS_CODE_PARAM_ERROR);
            resultJson.setStatusMessage("参数异常！");
            return resultJson;
        }

        /*
         * if (session.getAttribute(Constants.KAPTCHA_SESSION_KEY) == null) {
         * resultJson.setStatusCode(ResultJson.STATUS_CODE_PARAM_ERROR);
         * resultJson.setStatusMessage("验证码错误,请确认您的浏览器已开启Cookie功能"); return
         * resultJson; } String code =
         * session.getAttribute(Constants.KAPTCHA_SESSION_KEY).toString(); if
         * (!userReg.getCode().equalsIgnoreCase(code)) {
         * resultJson.setStatusCode(ResultJson.STATUS_CODE_PARAM_ERROR);
         * resultJson.setStatusMessage("验证码填写错误！"); return resultJson; }
         */

        try {
            User user = userService.login(userReg.getName(), userReg.getPassword());
            if (user == null) {
                resultJson.setStatusCode(ResultJson.STATUS_CODE_PARAM_ERROR);
                resultJson.setStatusMessage("你输入的邮箱地址或密码错误！");
                return resultJson;
            } else if (user.getUserStatus().equals(User.USER_STATUS_INACTIVE)) {
                resultJson.setStatusCode(ResultJson.STATUS_CODE_FAIL);
                resultJson.setStatusMessage("您的账户已被禁用！");
                return resultJson;
            }
            setUserSession(user, model, request);

            resultJson.setStatusCode(ResultJson.STATUS_CODE_SUCCESS);
            resultJson.setStatusMessage("用户登录成功！");
            return resultJson;
        } catch (Exception e) {
            model.addAttribute("errorinfo", e.getMessage());
            resultJson.setStatusCode(ResultJson.STATUS_CODE_EXCEPTION);
            resultJson.setStatusMessage(e.getMessage());
        }
        return resultJson;
    }

    @RequestMapping("/login")
    public String login(@Valid UserLogin userReg, BindingResult bindingResult, Model model, HttpSession session, HttpServletRequest request,
                        HttpServletResponse response) {
        String from = userReg.getFrom();
        boolean isRe = false;
        String url = loginView;
        Map<String, String> errors = new HashMap<String, String>();

        String toUrl = (String) session.getAttribute("toUrl");
        if (null != toUrl && toUrl.endsWith(".jsp")) {
            System.out.println(toUrl);
        }
        if (!StringUtils.isEmpty(from)) {
            url = "redirect:" + from.replace("#", "");
            isRe = true;
        }
        if (bindingResult.hasErrors()) {
            if (isRe) {
                return redirectError(bindingResult, errors, url);
            }
            return error(bindingResult, model, url);
        }

        /*
         * if (session.getAttribute(Constants.KAPTCHA_SESSION_KEY) == null) {
         * errors.put(errorInfo, "验证码错误,请确认您的浏览器已开启Cookie功能"); if (isRe) return
         * redirectError(bindingResult, errors, url);
         * model.addAllAttributes(errors); return error(bindingResult, model,
         * url); } if (!codeValidate(userReg.getCode(), session)) {
         * errors.put(errorInfo, "验证码填写错误！"); if (isRe) return
         * redirectError(bindingResult, errors, url);
         * model.addAllAttributes(errors); return error(bindingResult, model,
         * url); }
         */
        try {
            User user = userService.login(userReg.getName(), userReg.getPassword());
            if (user == null) {
                errors.put(errorInfo, "你输入的用户名或密码错误！");
                if (isRe)
                    return redirectError(bindingResult, errors, url);
                model.addAllAttributes(errors);
                return url;
            } else if (user.getUserStatus().equals(User.USER_STATUS_INACTIVE)) {
                errors.put(errorInfo, "该账户已被禁用,请联系管理员");
                if (isRe)
                    return redirectError(bindingResult, errors, url);
                model.addAllAttributes(errors);
                return url;
            } else if (user.getUserStatus().equals(User.USER_STATUS_NEW)) {
                errors.put(errorInfo, "该账户未激活,请去邮箱激活");
                if (isRe)
                    return redirectError(bindingResult, errors, url);
                model.addAllAttributes(errors);
                return url;
            }

            if (userReg.getAutoLogin() != null && userReg.getAutoLogin() == 1) { // remind password
                CookieUtil.addCookie(response, "autoUserName", userReg.getName(), 30); // 30天
                CookieUtil.addCookie(response, "autoPassword", userReg.getPassword(), 30); // 30天
            }
            setUserSession(user, model, request);
        } catch (ServiceException e) {
            errors.put(errorInfo, e.getMessage());
            if (isRe)
                return redirectError(bindingResult, errors, url);
            model.addAllAttributes(errors);
            return url;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (isRe)
            return redirectError(bindingResult, errors, url);
        model.addAllAttributes(errors);
        if (null != session.getAttribute("fromURL")) {
            String fromURL = (String) session.getAttribute("fromURL");
            return "redirect:" + fromURL;
        } else {
            return "redirect:/index.jsp";
        }
    }

    public String redirectError(BindingResult bindingResult, Map<String, String> model, String path)
    {

        StringBuffer buffer = new StringBuffer(path);

        try
        {
            if (bindingResult.hasErrors())
            {
                List<FieldError> errors = bindingResult.getFieldErrors();
                char interval = '?';
                if (path.indexOf('?') > 0)
                {
                    interval = '&';
                }
                buffer.append(interval);
                for (FieldError error : errors)
                {
                    buffer.append(error.getField()).append("=").append(error.getDefaultMessage()).append(interval);
                }
            }

            if (model != null && !model.isEmpty())
            {
                char interval = '?';
                if (path.indexOf('?') > 0)
                {
                    interval = '&';
                }
                buffer.append(interval);
                for (Map.Entry<String, String> entry : model.entrySet())
                {
                    String value = java.net.URLEncoder.encode(java.net.URLEncoder.encode(entry.getValue(), "UTF-8"),
                            "UTF-8");
                    buffer.append(entry.getKey()).append("=").append(value).append(interval);
                }
            }
            path = buffer.toString().replaceAll("％", "%");
            if (path.endsWith("&"))
            {
                return path.substring(0, path.length() - 1);
            }

            return path;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return path;
        }
    }

    protected void setUserSession(User user, Model model, HttpServletRequest request) throws Exception
    {
        user = userService.updateUserLoginTime(user);
        UserSession userSession = new UserSession();
        userSession.setIp(RequestUtil.getRequestIP(request));
        userService.digestUserName(user);
        BeanUtils.copyProperties(user, userSession);
        UserSessionFactory.updateUserSession(request.getSession(), userSession);

    }

    public String error(BindingResult bindingResult, Model model, String path)
    {
        if (bindingResult.hasErrors())
        {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
                model.addAttribute(error.getField(), error.getDefaultMessage());
            return path;
        }
        return null;
    }

}
