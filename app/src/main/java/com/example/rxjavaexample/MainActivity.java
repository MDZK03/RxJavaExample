package com.example.rxjavaexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFirst = findViewById(R.id.rxSimple);
        btnFirst.setOnClickListener(v -> startActivity(new Intent(this, RxJavaSimpleActivity.class)));

        Button btnSecond = findViewById(R.id.color);
        btnSecond.setOnClickListener(v -> startActivity(new Intent(this, ColorsActivity.class)));

        Button btnThird = findViewById(R.id.book);
        btnThird.setOnClickListener(v -> startActivity(new Intent(this, BooksActivity.class)));

        Button btnScheduler = findViewById(R.id.scheduler);
        btnScheduler.setOnClickListener(v -> startActivity(new Intent(this, SchedulerActivity.class)));
    }
}