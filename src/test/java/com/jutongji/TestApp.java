package com.jutongji;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class TestApp {

    public static void main(String[] args) {
        String str =  "20/王二";
        System.out.println(checkMatchPattern(str));
    }

    private static boolean checkMatchPattern(String str) {
        Pattern pattern = Pattern.compile("(\\d)+/([\\u4e00-\\u9fa5])+");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


}
