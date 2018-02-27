package com.example.xmlpull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.internal.cache.DiskLruCache;

public class TaskControllerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_controller);
        MyApi myApi = RequestUtils.getApi();
        myApi.getDetail("BorrowDetail","3171")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseRespData<BorrowBean>>() {
                    @Override
                    public void accept(BaseRespData<BorrowBean> borrowBeanBaseRespData) throws Exception {
                        Log.i("wanlijun",borrowBeanBaseRespData.errorCode);
                        Log.i("wanlijun",borrowBeanBaseRespData.errorMess);
//                        Log.i("wanlijun",borrowBeanBaseRespData.data.title);
                    }
                });
        myApi.getInfo("BorrowInfo","3171")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseRespData<BorrowBean>>() {
                    @Override
                    public void accept(BaseRespData<BorrowBean> borrowBeanBaseRespData) throws Exception {
                        Log.i("wanlijun",borrowBeanBaseRespData.errorCode);
                        Log.i("wanlijun",borrowBeanBaseRespData.errorMess);
                    }
                });
    }
}
