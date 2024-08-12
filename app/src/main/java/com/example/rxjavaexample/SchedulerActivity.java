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
        button  = findViewById(R.id.scheduleLongRunningOperation);
        button.setOnClickListener(v -> Observable.fromCallable(callable).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                doOnSubscribe(disposable ->
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            button.setEnabled(false);
                            String message = messageArea.getText().toString() + "\n" +"Progressbar visible" + "\n";
                            messageArea.setText(message);
                        }
                ).
                subscribe(getDisposableObserver()));
    }

    Callable<String> callable = () -> {
        SystemClock.sleep(1000);
        return "Hello";
    };

    private DisposableObserver<String> getDisposableObserver() {
        return new DisposableObserver<String>() {

            @Override
            public void onComplete() {
                String messageComplete = messageArea.getText().toString() + "\n" + "OnComplete" + "\n";
                messageArea.setText(messageComplete);
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                String messageProgressBar = messageArea.getText().toString()+ "\n" + "Hiding Progressbar";
                messageArea.setText(messageProgressBar);
            }

            @Override
            public void onError(Throwable e) {
                String messageError = messageArea.getText().toString() + "\n" +"OnError" + "\n";
                messageArea.setText(messageError);
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                String messageProgressBar = messageArea.getText().toString() + "\n" + "Hiding Progressbar" +"\n" ;
                messageArea.setText(messageProgressBar);
            }

            @Override
            public void onNext(String message) {
                String messageOnNext = messageArea.getText().toString() + "\n" +"onNext" + "\n" +message;
                messageArea.setText(messageOnNext);
            }
        };
    }
}