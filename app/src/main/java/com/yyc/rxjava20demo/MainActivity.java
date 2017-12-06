package com.yyc.rxjava20demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test1();
    }

    private void test1() {
        //创建被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                log("我是被观察者--subscribe---" + getT());
                //通知观察者
                e.onNext("通知观察者");
                e.onComplete();
            }
        });

        //创建观察者
        Observer observer = new Observer<String>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                log("onSubscribe--" + getT());
            }

            @Override
            public void onNext(@NonNull String s) {
                log("onNext--"  + getT() + "---" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                log("onError--"  + getT());
            }

            @Override
            public void onComplete() {
                log("onComplete--"  + getT());
            }
        };

        observable.subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    public void log(String str) {
        Log.e(TAG,str);
    }
    public String getT() {
        return ""+Thread.currentThread().getName();
    }
}
