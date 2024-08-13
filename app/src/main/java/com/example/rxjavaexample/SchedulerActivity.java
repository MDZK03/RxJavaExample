package com.example.rxjavaexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SchedulerActivity extends AppCompatActivity {

    private Disposable subscription;
    private ProgressBar progressBar;
    private TextView messageArea;
    private View button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        configureLayout();
        createObservable();
    }

    private void createObservable() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isDisposed()) subscription.dispose();
    }

    private void configureLayout() {
        setContentView(R.layout.activity_scheduler);
        progressBar = findViewById(R.id.progressBar);
        messageArea = findViewById(R.id.messageArea);
        button  = findViewById(R.id.btnLongOperation);
        button.setOnClickListener(v -> Observable.fromCallable(callable).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                doOnSubscribe(disposable ->
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            button.setEnabled(false);
                            messageArea.append("\n" +"Progressbar visible" + "\n");
                        }
                ).
                subscribe(getDisposableObserver()));
    }

    Callable<String> callable = () -> {
        SystemClock.sleep(2000);
        return "Hello";
    };

    private DisposableObserver<String> getDisposableObserver() {
        return new DisposableObserver<String>() {

            @Override
            public void onComplete() {
                messageArea.append("\n" + "onComplete: ");
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                messageArea.append("Hiding Progressbar" + "\n");
            }

            @Override
            public void onError(Throwable e) {
                messageArea.append("\n" +"OnError: " + "\n");
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                messageArea.append("Hiding Progressbar" + "\n");
            }

            @Override
            public void onNext(String message) {
                messageArea.append("\n" + "onNext: " + message + "\n");
            }
        };
    }
}