package com.example.xmlpull;

/**
 * @author wanlijun
 * @description  网络请求返回的结果
 * @time 2018/2/13 14:02
 */

public class BaseRespData<E>{
    String errorCode;
    String errorMess;
    E data;
}
