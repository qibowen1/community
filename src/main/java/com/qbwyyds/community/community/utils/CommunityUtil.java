package com.qbwyyds.community.community.utils;

import com.alibaba.fastjson.JSONObject;
import io.micrometer.common.util.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

public class CommunityUtil {
    //随机字符串 激活码
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密 hello-> asdad135
    //md5+salt hello+salt ->asdad135gedas665as
    public static String md5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    //将字符串封装成json格式的字符串
    public static String getJsonString(int code, String msg, Map<String,Object> map){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        if (map!=null){
            for (String key: map.keySet()){
                jsonObject.put(key,map.get(key));
            }
        }
        return jsonObject.toJSONString();//将json对象转为json格式的字符串
    }
    //重载
    public static String getJsonString(int code, String msg){
        return getJsonString(code,msg,null);
    }
    //重载
    public static String getJsonString(int code){
        return getJsonString(code, null, null);
    }
}
