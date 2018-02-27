package com.example.xmlpull;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author wanlijun
 * @description  接口
 * @time 2018/2/12 17:46
 */

public interface MyApi {
    //参数标签为@Part时，必须加上@Multipart，切retrofit初始化时要加这句addConverterFactory(ScalarsConverterFactory.create())，否则提示参数错误
    @Multipart
    @POST("borrow/detail")
    Observable<BaseRespData<BorrowBean>> getDetail(
            @Part("cmd") String cmd,
            @Part("borrowId") String borrowId
    );

    //参数标签为@Field时，必须加上@FormUrlEncoded，retrofit可以不要addConverterFactory(ScalarsConverterFactory.create())这句
    @FormUrlEncoded
    @POST("borrow/info")
    Observable<BaseRespData<BorrowBean>> getInfo(
            @Field("cmd") String cmd,
            @Field("borrowId") String borrowId
    );
}
