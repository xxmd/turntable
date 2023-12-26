package io.github.xxmd;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.xxmd.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        bindEvent();
    }

    private void bindEvent() {
        binding.turnTable.setOnClickListener(v -> binding.turnTable.rotate());
        binding.turnTable.setListener(new TurntableListener() {
            @Override
            public void onRotateStart(float preRotateAngle) {

            }

            @Override
            public void onRotating(float rotateAngle) {

            }

            @Override
            public void onRotateEnd(float hasRotateAngle) {
                TableOption result = binding.turnTable.getResult();
                Toast.makeText(MainActivity.this, result.label, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        List<TableOption> tableOptionList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            TableOption tableOption = new TableOption();
            tableOption.label = String.format("Label %d", i);
            tableOption.weight = 1;
            tableOption.backgroundColor = Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255));
            tableOption.labelColor = Color.BLACK;
            tableOptionList.add(tableOption);
        }
        binding.turnTable.setTableOptionList(tableOptionList);
    }
}