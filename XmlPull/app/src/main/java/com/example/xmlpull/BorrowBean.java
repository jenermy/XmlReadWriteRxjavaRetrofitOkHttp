package com.example.xmlpull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author wanlijun
 * @description  对象与json字符串转换
 * @time 2018/2/12 17:14
 */

public class BorrowBean {
    String id;   //标ID
    String title;   //标名称
    String fstatus;   //标的状态
    String ftype;   //标种类型
    String style;    //还款方式
    String time_limit;   //标的期限（月标）
    String is_day;   //是否天标：1-是  0-否
    String time_limit_day;   //标的期限（天标）
    String account;   //借款总额
    String account_yes;   //已借到金额
    String shiming;   //是否实名
    String hk_status;   //标的筹集状况："0"=>'招标中', "1"=>'已还款', "2"=>'正在还款中'
    String is_new;   //否新手标：0-否，1-是

    public static BorrowBean gsonToBean(String jsonStr){
        Gson gson = new Gson();
        return gson.fromJson(jsonStr,BorrowBean.class);
    }

    public static List<BorrowBean> gsonToBeanList(String jsonStr){
        Gson gson = new Gson();
        return gson.fromJson(jsonStr,new TypeToken<List<BorrowBean>>(){}.getType());
    }

    public static String beanToGson(BorrowBean borrowBean){
        Gson gson = new Gson();
        return gson.toJson(borrowBean);
    }

}
