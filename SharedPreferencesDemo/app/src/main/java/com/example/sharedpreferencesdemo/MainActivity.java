package com.example.sharedpreferencesdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etPassword, etPhone, etEmail;
    private Spinner spGender;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化视图
        etName = findViewById(R.id.et_name);
        etPassword = findViewById(R.id.et_password);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        spGender = findViewById(R.id.sp_gender);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnRead = findViewById(R.id.btn_read);
        Button btnClear = findViewById(R.id.btn_clear);

        // 初始化 SharedPreferences
        pref = getSharedPreferences("user_info", MODE_PRIVATE);

        // 设置 Spinner 数据（性别列表）
        String[] genderList = {"男", "女"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                genderList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);

        // 按钮事件绑定
        btnSave.setOnClickListener(v -> saveData());
        btnRead.setOnClickListener(v -> readData());
        btnClear.setOnClickListener(v -> clearFields());
    }

    // 保存数据到 SharedPreferences
    private void saveData() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", etName.getText().toString());
        editor.putString("password", etPassword.getText().toString());
        editor.putString("phone", etPhone.getText().toString());
        editor.putString("email", etEmail.getText().toString());
        editor.putString("gender", spGender.getSelectedItem().toString()); // 从 Spinner 获取选中项
        editor.apply();
    }

    // 从 SharedPreferences 读取数据
    private void readData() {
        etName.setText(pref.getString("name", ""));
        etPassword.setText(pref.getString("password", ""));
        etPhone.setText(pref.getString("phone", ""));
        etEmail.setText(pref.getString("email", ""));

        // 设置 Spinner 选中项
        String savedGender = pref.getString("gender", "男");
        ArrayAdapter adapter = (ArrayAdapter) spGender.getAdapter();
        spGender.setSelection(adapter.getPosition(savedGender)); // 恢复保存的性别
    }

    // 清空输入框
    private void clearFields() {
        etName.setText("");
        etPassword.setText("");
        etPhone.setText("");
        etEmail.setText("");
        spGender.setSelection(0); // 默认选中第一个选项（男）
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData(); // 退出时自动保存
    }
}