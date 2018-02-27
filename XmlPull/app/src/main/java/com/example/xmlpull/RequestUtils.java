package com.example.xmlpull;

import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * @author wanlijun
 * @description 网络请求方法封装
 * @time 2018/2/12 17:04
 */

public class RequestUtils {
    private  static OkHttpClient httpClient;
    private  static Retrofit retrofit;
    public  static MyApi getApi(){
        httpClient = new OkHttpClient.Builder()
                //以下注释的地方可以添加一些共同的请求参数
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request();
//                        Request.Builder requestBuilder = request.newBuilder();
//                        FormBody.Builder formBodyBuilder = new FormBody.Builder();
//                        RequestBody formBody = formBodyBuilder.build();
//                        String postBodyString = bodyToString(request.body());
//                        postBodyString += ((postBodyString.length() > 0) ? "&" : "") +  bodyToString(formBody);
//                        return chain.proceed(requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString)).build());
//                    }
//                })
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.i("wanlijun",message);
                        //打印出请求参数，请求头，请求返回的结果等日志信息，非常有利于错误定位
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY)) //设置为Level.BODY比较好，解决了200 OK http://xinet.duobaodai.com/lcapi/index.php/v1/borrow/detail (60ms, unknown-length body)不知道内容长度的bug
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                //传输的参数没有加密时，不加下面的addConverterFactory(ScalarsConverterFactory.create())这句，老是提示参数非法
                .addConverterFactory(ScalarsConverterFactory.create())//去掉双引号的,明文传输的时候 retorfit有个bug  默认带个引号
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build();
        return retrofit.create(MyApi.class);
    }

    private static boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
    private static String bodyToString(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if(copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }
}
