package com.example.rxjavaexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class FlowableExampleActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable_example);
        Button btn = findViewById(R.id.btnExample);
        textView = findViewById(R.id.tvExample);

        btn.setOnClickListener(v -> doSomeWork());

    }

    private void doSomeWork() {
        Flowable<Integer> observable = Flowable.just(1,2,3,4);
        observable.reduce(100, Integer::sum).subscribe(getObserver());
    }

    private SingleObserver<Integer> getObserver() {
        return new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Timber.d("onSubscribe: %s", d.isDisposed());
            }

            @Override
            public void onSuccess(Integer integer) {
                textView.append("onSuccess: value = " + integer);
                Timber.d("onSuccess: value = %s", integer);
            }

            @Override
            public void onError(Throwable e) {
                textView.append("onError: " + e.getMessage());
                Timber.e("onError: %s", e.getMessage());
            }
        };
    }
}