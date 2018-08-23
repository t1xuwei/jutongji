package com.jutongji.exception;

public class ErrorCode
{
    /**
     * 常规错误定义
     */
    public static final int UNKNOWN_INTERNAL_ERROR = 1;                 /**常规错误：系统内部错误*/
    public static final int DB_ERROR = 2;                               /**常规错误：数据库操作错误*/   
    public static final int NULL_INPUT = 3;                             /**常规错误：输入参数为空*/
    public static final int ILLEGAL_REQUEST = 4;                        /**常规错误：非法请求，参数格式不合法*/
    public static final int CHARACTER_ENCODE = 5;                       /**常规错误：字符串编码错误*/
    public static final int CHARACTER_DENCODE = 6;                      /**常规错误：字符串解码错误*/
    public static final int JSON_ERROR = 7;                             /**常规错误：JSON错误*/
    public static final int NULL_OUTPUT = 8;                            /**常规错误：输出参数为空*/
    public static final int OTHER_ERROR = 9;                            /**常规错误：其他错误*/
    public static final int DB_OPTIMISTIC_LOCK_UPDATE_FAIL = 10;        /**常规错误：数据库同步更新错误 （乐观锁实现过程中可能发生的错误）*/

    static public final int BASE64_ENCODED_ERROR = 11;                  /**Base64编码错误*/
    static public final int BASE64_DECODED_ERROR = 12;                  /**Base64解码错误*/
    public static final int VALIDATOR_PARAM_ERROR = 14;                 /**常规错误：请求参数格式错误*/
    public static final int REPEAT_RECORD_ERROR = 15;                   /**重复的业务记录*/
    public static final int USER_NOT_SUPPORT_BUSINESS = 16;             /**用户不支持此项活动*/
    
    public static final int TEMPLATE_MODEL_ERROR = 21;                  /**数据模版文件错误*/
    public static final int TEMPLATE_MODEL_INVALID_ID = 22;             /**数据模版ID错误*/
    public static final int TEMPLATE_MODEL_INVALID_TYPE = 23;           /**数据模版类型错误*/
    public static final int TEMPLATE_ITEM_TYPE_ERROR = 24;              /**数据模版中ITEM类型错误*/
    public static final int TEMPLATE_ITEM_MAPPED_ERROR = 25;            /**数据模版中ITEM映射错误*/
    public static final int TEMPLATE_ITEM_MAPPED_DUP = 26;              /**数据模版中ITEM映射重复错误*/
    
    public static final int TEMPLATE_LAYOUT_ERROR = 31;                 /**布局模版文件错误*/
}
