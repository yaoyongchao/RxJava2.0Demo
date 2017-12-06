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
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: Page
 * Create: 17-12-6 上午10:35
 * Description: Rxjava2.0demo练习
 *
 * Observable：在观察者模式中称为“被观察者”；
 * Observer：观察者模式中的“观察者”，可接收Observable发送的数据；
 * subscribe：订阅，观察者与被观察者，通过subscribe()方法进行订阅；
 * Subscriber：也是一种观察者，在2.0中 它与Observer没什么实质的区别，不同的是 Subscriber要与Flowable(也是一种被观察者)联合使用，该部分内容是2.0新增的
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Observable<String> observable1;
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

        observable
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        log("doOnSubscribe--" + getT());
                    }
                })
                .subscribeOn(Schedulers.io())
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        log("doFinally--" + getT());

                        observable1
                                .doOnSubscribe(new Consumer<Disposable>() {
                                    @Override
                                    public void accept(@NonNull Disposable disposable) throws Exception {
                                        log("doOnSubscribe**--" + getT());
                                    }
                                })
                                .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                log("onSubscribe**--" + getT());
            }

            @Override
            public void onNext(@NonNull String s) {
                log("onNext**--" + getT());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                log("onError**--" + getT());
            }

            @Override
            public void onComplete() {
                log("onComplete**--" + getT());
            }
        });

                    }
                })
//                .subscribeOn(Schedulers.newThread())
                .subscribe(observer);




        //创建被观察者
        observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                log("我是被观察者--subscribe---" + getT());
                //通知观察者
                e.onNext("通知观察者1");
                e.onComplete();
            }
        });

//        observable1.subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//                log("onSubscribe**--" + getT());
//            }
//
//            @Override
//            public void onNext(@NonNull String s) {
//                log("onNext**--" + getT());
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                log("onError**--" + getT());
//            }
//
//            @Override
//            public void onComplete() {
//                log("onComplete**--" + getT());
//            }
//        });
    }

    public void log(String str) {
        Log.e(TAG,str);
    }
    public String getT() {
        return ""+Thread.currentThread().getName();
    }
}


