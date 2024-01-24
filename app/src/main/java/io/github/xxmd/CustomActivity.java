package io.github.xxmd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.xxmd.databinding.ActivityCustomBinding;


public class CustomActivity extends AppCompatActivity {
    private ActivityCustomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        bindEvent();
    }

    private void bindEvent() {
        binding.turnTable.setOnClickListener(v -> binding.turnTable.rotate());
        binding.turnTable.setListener(new TurntableListener() {
            @Override
            public void onRotateStart(float preRotateAngle) {
                int width = binding.ivPoint.getWidth();
                int height = binding.ivPoint.getHeight();
                binding.ivPoint.setPivotX(width / 2);
                binding.ivPoint.setPivotY(height / 5);
            }

            @Override
            public void onRotating(float rotateAngle, float slope) {
                float percent = slope / binding.turnTable.getMaxSlope();
                binding.ivPoint.setRotation(-percent * 45);
            }

            @Override
            public void onRotateEnd(float hasRotateAngle) {
                TableOption result = binding.turnTable.getResult();
                Toast.makeText(CustomActivity.this, result.label, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlainActivity.class);
            startActivity(intent);
        });
    }

    private void initView() {
        List<TableOption> tableOptionList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            TableOption tableOption = new TableOption();
            tableOption.label = String.format("Label %d", i);
            tableOption.weight = 1;
            tableOption.backgroundColor = i % 2 == 0 ? Color.parseColor("#3cbdfe") : Color.WHITE;
            tableOption.labelColor = i % 2 != 0 ? Color.parseColor("#50A7FE") : Color.WHITE;
            tableOptionList.add(tableOption);
        }
        binding.turnTable.setTableOptionList(tableOptionList);
    }
}