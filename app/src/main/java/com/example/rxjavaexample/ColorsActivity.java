package com.example.rxjavaexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class ColorsActivity extends AppCompatActivity {

    RecyclerView colorListView;
    SimpleStringAdapter simpleStringAdapter;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        createObservable();
    }

    private void configureLayout() {
        setContentView(R.layout.activity_colors);
        colorListView = findViewById(R.id.color_list);
        colorListView.setLayoutManager(new LinearLayoutManager(this));
        simpleStringAdapter = new SimpleStringAdapter(this);
        colorListView.setAdapter(simpleStringAdapter);
    }

    private void createObservable() {
        Observable<List<String>> listObservable = Observable.just(getColorList());
        disposable = listObservable.subscribe(colors -> simpleStringAdapter.setStrings(colors));
    }

    private static List<String> getColorList() {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("red");
        colors.add("blue");
        colors.add("green");
        colors.add("yellow");
        return colors;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(disposable != null && !disposable.isDisposed()) disposable.dispose();
    }
}