package com.jc.android.template.presentation.viewmodel;

import android.content.Context;
import android.widget.Toast;


import com.jc.android.template.presentation.exception.ErrorMessageFactory;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public abstract class ProcessErrorSubscriber<T> extends Subscriber<T> {

    private final Context context;


    public ProcessErrorSubscriber() {
        context = null;
    }

    public ProcessErrorSubscriber(Context context) {
        this.context = context;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (context != null) {

            Toast.makeText(context, ErrorMessageFactory.create(context, (Exception) e), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onNext(T t) {

    }
}