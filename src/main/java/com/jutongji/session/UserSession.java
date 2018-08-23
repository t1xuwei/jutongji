package com.jutongji.session;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 放入session的用户对象 因为在负载均衡时，Session将被放入共享存储区（如数据库），所以这个对象应该尽量小，
 * 目前这个对象只设计了3个成员变量，按照用户是否登录，这三个变量的取值如下：
 * ------------------------------------------------------------------- 描述 用户未登录时
 * 用户登录后 ------------------------------------------------------------------- 1.
 * userId 用户的ID 0 该User对应的ID
 * ------------------------------------------------------------------- 2.
 * userName 用户注册账号， 上一次登录后保存 该User对应的userName 如果非空，则应该 在客户端Cookie 在用户登录页面中 中的内容
 * 自动填写到用户名 框中
 * ------------------------------------------------------------------- 3. nick
 * 用户的昵称，如果 上一次登录后保存 该User对应的Nick 非空，则应该显示 在客户端Cookie 在页面导航栏中 中的内容
 * -------------------------------------------------------------------
 * 
 * @author Administrator
 */
public class UserSession implements Serializable
{

    private static final long serialVersionUID = -467384204844452123L;

    /** 用户ID */
    private int userId = 0;

    /** 用户名 */
    private String userName = null;

    /** 昵称 */
    private String nick = null;

    /** 真实姓名 **/
    private String userRealname = null;
    
    /**处理后用户名**/
    private String userDigestedName = null;
    
    /** 邮件地址 */
    private String email = null;

    /** 最后登录时间 */
    private Date lastLoginTime = null;

    /** 头像 **/
    private String headImg = null;

    /** 用户类型 **/
    private int userType = 1;

    /** 用户IP **/
    private String ip;

    private String phone;

    private Integer emailValidate;

    private Integer phoneValidate;

    private Map<String,Object> customData;
    
    public String getUserDigestedName()
    {
        return userDigestedName;
    }

    public void setUserDigestedName(String userDigestedName)
    {
        this.userDigestedName = userDigestedName;
    }

    public Integer getEmailValidate()
    {
        return emailValidate;
    }

    public void setEmailValidate(Integer emailValidate)
    {
        this.emailValidate = emailValidate;
    }

    public Integer getPhoneValidate()
    {
        return phoneValidate;
    }

    public void setPhoneValidate(Integer phoneValidate)
    {
        this.phoneValidate = phoneValidate;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getName()
    {
        if (userName.indexOf('@') > 0)
        {
            return userName.substring(0, userName.indexOf('@'));
        }
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getNick()
    {
        return nick;
    }

    public void setNick(String nick)
    {
        this.nick = nick;
    }

    public String getUserRealname()
    {
        return userRealname;
    }

    public void setUserRealname(String userRealname)
    {
        this.userRealname = userRealname;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Date getLastLoginTime()
    {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime)
    {
        this.lastLoginTime = lastLoginTime;
    }

    public String getHeadImg()
    {
        return headImg;
    }

    public void setHeadImg(String headImg)
    {
        this.headImg = headImg;
    }

    public int getUserType()
    {
        return userType;
    }

    public void setUserType(int userType)
    {
        this.userType = userType;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public Map getCustomData()
    {
        if(customData==null)
        {
            customData = new HashMap<String,Object>();
        }
        return customData;
    }

    public void setCustomData(Map customData) {
        this.customData = customData;
    }

    // public User getUser() {
    // if (getUserId() > 0) {
    // try {
    // return SiteBeanFactory.getUserManager().findUserById(getUserId());
    // } catch (ServiceException e) {
    // e.printStackTrace();
    // return null;
    // }
    // }
    // else {
    // return UserFactory.createGuest(getUserName(), getNick());
    // }
    // }

}
