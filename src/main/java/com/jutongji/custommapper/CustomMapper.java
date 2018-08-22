package com.jutongji.custommapper;

import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/8/22 15:24
 */
public interface CustomMapper<T>  extends tk.mybatis.mapper.common.Mapper<T>,MySqlMapper<T> {
}
