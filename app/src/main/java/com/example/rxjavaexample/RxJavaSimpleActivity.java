package com.example.rxjavaexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaSimpleActivity extends AppCompatActivity {

    CompositeDisposable disposable = new CompositeDisposable();

    final Observable<String> serverDownloadObservable = Observable.create(emitter -> {
        SystemClock.sleep(5000);
        emitter.onNext("This is a sentence.");
        emitter.onComplete();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_simple);

        Button btnServer = findViewById(R.id.serverBtn);
        btnServer.setOnClickListener(v -> {
            Toast.makeText(this, "Please wait a few seconds", Toast.LENGTH_SHORT).show();
            v.setEnabled(false);
            Disposable subscribe = serverDownloadObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnError(throwable -> {
                        throw new Exception("Unexpected error.");
                    })
                    .subscribe(string -> {
                        updateUserInterface(string);
                        btnServer.setEnabled(true);
                    });
            disposable.add(subscribe);
        });
    }

    private void updateUserInterface(String string) {
        TextView tv = findViewById(R.id.resultView);
        tv.setText(string);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(disposable != null && !disposable.isDisposed()) disposable.dispose();
    }
}