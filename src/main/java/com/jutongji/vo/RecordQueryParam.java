package com.jutongji.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/9/11 14:02
 */
@Data
public class RecordQueryParam implements Serializable{

    private Integer subjectId;

    private String month;

    private String year;

    private Integer monthVal;

    public static final Map<String,Integer> monthMap = new HashMap();

    static{
        monthMap.put("一月",1);
        monthMap.put("二月",2);
        monthMap.put("三月",3);
        monthMap.put("四月",4);
        monthMap.put("五月",5);
        monthMap.put("六月",6);
        monthMap.put("七月",7);
        monthMap.put("八月",8);
        monthMap.put("九月",9);
        monthMap.put("十月",10);
        monthMap.put("十一月",11);
        monthMap.put("十二月",12);
    }

}
