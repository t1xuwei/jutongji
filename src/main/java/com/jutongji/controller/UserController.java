package com.jutongji.controller;

import com.google.code.kaptcha.Constants;
import com.jutongji.config.mail.MailCfg;
import com.jutongji.dto.UserLogin;
import com.jutongji.dto.UserReg;
import com.jutongji.exception.ServiceException;
import com.jutongji.mail.BindingMailEntry;
import com.jutongji.mail.MailSchedulerFactory;
import com.jutongji.mail.RegistMailEntry;
import com.jutongji.mail.ResetMailEntry;
import com.jutongji.model.User;
import com.jutongji.service.IUserService;
import com.jutongji.session.UserSession;
import com.jutongji.session.UserSessionFactory;
import com.jutongji.util.*;
import com.jutongji.util.security.DigestUtils;
import com.jutongji.util.str.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/8/23 14:33
 */
@Controller
@RequestMapping("user")
public class UserController {


    @Autowired
    private IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    protected final static String errorInfo = "errorInfo";

    private static final String registerView = "/user/register";

    private static final String successView = "index";

    private static final String registerResult = "user/registerResult";

    private static final String centerView = "user/index";

    private static final String loginView = "login";

    private static final String findPwdView = "user/findPwd";

    private static final String tofindPwdView = "user/password";

    private static final String findPwdInfoView = "user/findInfo";

    private static final String updateInfo = "user/modifyUser";

    private static final String activeFail = "user/activeFail";

    private static final String activeSuccess = "user/active_success";

    private static final String findPwdFail = "user/findPwdFail";

    private static final String bindingSuccess = "user/binding_success";

    private static final String bindingFail = "user/bindingFail";

    private static final String reActive = "user/reActive";

    private static final Integer ACTIVEEMAIL = Integer.valueOf(1);

    private static final Integer RESETEMAIL = Integer.valueOf(2);

    private static final Integer BINGDINGEMAIL = Integer.valueOf(3);


    protected final static String addInfo = "addInfo";

    @PostMapping("/ajax/login")
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

    @GetMapping("/login")
    public String login(@Valid UserLogin userReg, BindingResult bindingResult, Model model, HttpSession session, HttpServletRequest request,
                        HttpServletResponse response) {
        String from = userReg.getFrom();
        boolean isRe = false;
        String url = loginView;
        Map<String, String> errors = new HashMap<String, String>();

        String toUrl = (String) session.getAttribute("toUrl");
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
            return "index";
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

    @GetMapping("/register")
    public String register(@Valid UserReg userReg, BindingResult bindingResult, Model model, RedirectAttributes attrs, HttpSession session,
                           RedirectAttributes attr, HttpServletResponse response, HttpServletRequest request)
    {
        if (bindingResult.hasErrors())
        {
            return error(bindingResult, model, registerView);
        }
        User user = new User();
        try
        {
           // 邮箱注册
            if (session.getAttribute(Constants.KAPTCHA_SESSION_KEY) == null)
            {
                model.addAttribute(errorInfo, "验证码错误,请确认您的浏览器已开启Cookie功能！");
                return registerView;
            }
            if (!codeValidate(userReg.getCode(), session))
            {
                model.addAttribute(errorInfo, "验证码错误！");
                return registerView;
            }
            user.setEmail(userReg.getUsername());
            user.setUserStatus(User.USER_STATUS_NEW);
            user.setUserName(userReg.getUsername());
            user.setPassword(userReg.getPassword());
            user.setUserNo(TimeUtil.formatDate(new Date(), TimeUtil.FORMAT_YYYYMMDD) + RandomUtil.genRandomNumberString(6));
            User oldUser = userService.findUserByName(userReg.getUsername());
            if (null != oldUser && oldUser.getUserStatus().equals(User.USER_STATUS_NEW))
            { // 未激活的用户
                oldUser.setPassword(userReg.getPassword());
                user = userService.updateUserPassword(oldUser);
            }
            else
            {
                user = userService.insert(user);
            }
            if (!StringValidator.isNumber(userReg.getUsername()))
            {
                if (sendEmail(userReg.getUsername(), ACTIVEEMAIL, response, session))
                {
                    model.addAttribute(addInfo, "我们已经向您的邮箱" + user.getEmail().split("@")[0]
                            + "发送了一封激活邮件，请点击邮件中的链接完成注册！"); // TODO
                    attrs.addFlashAttribute("registerEmail", user.getEmail());
                };
            }
        }
        catch (ServiceException e)
        {
            logger.error(e.getMessage(), e);
            model.addAttribute("errorinfo", e.getMessage());
            return error(bindingResult, model, registerView);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            model.addAttribute("errorinfo", e.getMessage());
            return error(bindingResult, model, registerView);
        }
        model.addAttribute("from", userReg.getFrom());

        try
        {
            if (StringValidator.isNumber(userReg.getUsername()))
            {
                UserSession userSession = new UserSession();
                userSession.setIp(RequestUtil.getRequestIP(request));
                userService.digestUserName(user);
                copyProperties(userSession, user);
                UserSessionFactory.updateUserSession(request.getSession(), userSession);
                return  successView ;
            }

        }
        catch (Exception e)
        {
            logger.info("日志生成异常!");
            e.printStackTrace();
        }
        return  registerResult ;
    }

    /**
     * activate：邮件激活
     *
     * @param acode
     *            激活码
     * @param account
     *            账户
     * @param model
     * @return
     * @see <参见的内容>
     */
    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public String activate(@RequestParam
                                   String acode, @RequestParam
                                   String account, Model model, HttpServletRequest request, HttpServletResponse response)
    {
        if (StringUtils.isEmpty(acode) || StringUtils.isEmpty(account))
        {
            model.addAttribute(errorInfo, "请求参数有误,激活失败!");
            return activeFail;
        }
        try
        {
            User user = userService.findUserByName(account);
            if (null == user)
            {
                logger.error("未查询到改用户,激活失败");
                model.addAttribute(errorInfo, "请求数据有误,激活失败");
                return activeFail;
            }
            if (User.USER_STATUS_INACTIVE.equals(user.getUserStatus()))
            {
                model.addAttribute(errorInfo, User.USER_STATUS_INACTIVE_NAME);
                logger.error("改用户已被禁用,激活失败");
                return activeFail;
            }
            if (User.USER_STATUS_ACTIVE.equals(user.getUserStatus()))
            {
                model.addAttribute(errorInfo, "该用户已经激活,^_^");
                return activeFail;
            }
            String date = TimeUtil.formatDate(new Date(), "yyyyMMdd");
            String hashStr = user.getUserNo() + user.getUserId() + date;
            String hash = DigestUtils.md5(DigestUtils.md5(hashStr.getBytes("UTF-8")).getBytes("UTF-8"));
            if (!acode.equals(hash))
            {
                model.addAttribute(errorInfo, "激活链接已过期,请重新激活!");
                model.addAttribute("activeUser", user); // 用于重新发送激活邮件
                logger.error("激活链接过期或参数错误!");
                return reActive;
            }
            user.setUserStatus(User.USER_STATUS_ACTIVE);
            userService.update(user);
            setUserSession(user, model, request);
            model.addAttribute("activeInfo", "恭喜您成功激活该账户!");
            return activeSuccess;
        }
        catch (ServiceException e)
        {
            model.addAttribute(errorInfo, "请求数据有误,激活失败");
            logger.error("请求参数有误,激活失败!", e);
            e.printStackTrace();
            return activeFail;
        }
        catch (Exception e)
        {
            logger.error("系统异常!", e);
            e.printStackTrace();
            return activeFail;
        }
    }

    public Boolean sendEmail(String account, Integer type, HttpServletResponse response, HttpSession session)
            throws ServiceException
    {
        User user = new User();
        try
        {
            if (account == null || account.equals(""))
            {
                return false;
            }
            // userNo + userId + date TODO
            if ( BINGDINGEMAIL.equals(type))
            {
                UserSession userSession = UserSessionFactory.getUserSession(session);
                user = userService.findUserByUserId(userSession.getUserId());
                user.setEmail(account);
            }
            else
            {
                user = userService.findUserByName(account);
                if (null == user)
                {
                    return false;
                }
            }
            String date = TimeUtil.formatDate(new Date(), "yyyyMMdd");
            String hashStr = user.getUserNo() + user.getUserId() + date;
            String hash = null;
            try
            {
                hash = SecureUtil.md5(SecureUtil.md5(hashStr));
            } catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
            String receiver = user.getEmail();
            String url = null;
            if ( ACTIVEEMAIL.equals(type))
            {
                url = MailCfg.getInstance().getRegistUrl() + "=" + hash + "&account=" + receiver;
                RegistMailEntry entry = new RegistMailEntry(receiver, response.encodeURL(url));
                logger.info("用户" + user.getEmail() + "开始进入(激活)邮件发送队列!");
                MailSchedulerFactory.getMailScheduler().putMail(entry);
            }
            if (RESETEMAIL.equals(type))
            {
                url = MailCfg.getInstance().getResetUrl() + "=" + hash + "&account=" + receiver;
                ResetMailEntry entry = new ResetMailEntry(receiver, response.encodeURL(url));
                logger.info("用户" + user.getEmail() + "开始进入(重置密码)邮件发送队列!");
                MailSchedulerFactory.getMailScheduler().putMail(entry);
            }
            if (BINGDINGEMAIL.equals(type))
            {
                url = MailCfg.getInstance().getBindingUrl() + "=" + hash + "&account=" + receiver;
                BindingMailEntry entry = new BindingMailEntry(receiver, response.encodeURL(url));
                logger.info("用户" + user.getEmail() + "开始进入(绑定邮箱)邮件发送队列!");
                MailSchedulerFactory.getMailScheduler().putMail(entry);
            }
        }
        catch (Exception e)
        {
            throw new ServiceException(0, "发送邮件至" + user.getEmail() + "失败");
        }
        return true;
    }
    protected void copyProperties(UserSession userSession, User user)
    {
        userSession.setEmail(user.getEmail());
        userSession.setLastLoginTime(user.getLastLogintime());
        userSession.setUserId(user.getUserId());
        userSession.setUserName(user.getUserName());
    }

    @ResponseBody
    @GetMapping("/code/validate")
    public Boolean codeValidate(String code, HttpSession session)
    {
        if(code.length()==5 && code.startsWith(","))
        {
            code = code.substring(1);
        }

        String validateCode = session.getAttribute(Constants.KAPTCHA_SESSION_KEY).toString();
        if (!code.equalsIgnoreCase(validateCode))
        {
            return false;
        }
        return true;
    }

}
