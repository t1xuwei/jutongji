package com.jutongji.config.kaptcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author: xuw
 * @Description: 验证码的配置
 * @Date: 2018/8/31 16:00
 */
@Component
public class CaptchaProducerHolder  {

    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.textproducer.font.names","Arial, Helvetica");
        properties.setProperty("kaptcha.border","no");
        properties.setProperty("kaptcha.border.color","0,0,0");
        properties.setProperty("kaptcha.textproducer.font.color","21,107,139");
        properties.setProperty("kaptcha.textproducer.font.size","40");
        properties.setProperty("kaptcha.image.width","160");
        properties.setProperty("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.WaterRipple");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        properties.setProperty("kaptcha.textproducer.char.string","zxcvbnmasdfghjkqwertyupZXCVBNMASDFGHJKLQWERTYUP23456789");
        properties.setProperty("kaptcha.word.impl","com.google.code.kaptcha.text.impl.DefaultWordRenderer");
        properties.setProperty("kaptcha.textproducer.impl","com.google.code.kaptcha.text.impl.DefaultTextCreator");
        properties.setProperty("kaptcha.word.impl","com.google.code.kaptcha.text.impl.DefaultWordRenderer");
        properties.setProperty("kaptcha.background.clear.from","255,255,255");
        properties.setProperty("kaptcha.background.clear.to","255,255,255");
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }


}
