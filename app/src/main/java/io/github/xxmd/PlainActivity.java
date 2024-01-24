package io.github.xxmd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.xxmd.databinding.ActivityPlainBinding;


public class PlainActivity extends AppCompatActivity {
    private ActivityPlainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlainBinding.inflate(getLayoutInflater());
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
            public void onRotating(float rotateAngle, float slope) {

            }

            @Override
            public void onRotateEnd(float hasRotateAngle) {
                TableOption result = binding.turnTable.getResult();
                Toast.makeText(PlainActivity.this, result.label, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnBack.setOnClickListener(v -> finish());
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