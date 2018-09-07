package com.jutongji.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/9/7 15:53
 */
@Data
@Component
@ConfigurationProperties(
        prefix = "jutongji.path"
)
public class PathConfig {
    private String websitePath;


}
