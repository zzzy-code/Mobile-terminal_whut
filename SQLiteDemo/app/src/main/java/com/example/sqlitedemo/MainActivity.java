package com.example.sqlitedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerUsers;
    private EditText etName, etPassword, etPhone, etEmail;
    private RadioGroup radioGroupGender;
    private RadioButton radioMale, radioFemale;
    private Button btnAdd, btnSave, btnDelete, btnClear;

    private UserDBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 UI 控件
        initViews();

        // 初始化数据库
        dbHelper = new UserDBHelper(this);
        db = dbHelper.getWritableDatabase();

        // 加载 Spinner 数据
        loadSpinnerData();

        // 设置 Spinner 选择监听
        setupSpinnerListener();

        // 设置按钮点击事件
        setupButtonListeners();
    }

    private void initViews() {
        spinnerUsers = findViewById(R.id.spinnerUsers);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        btnAdd = findViewById(R.id.btnAdd);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnClear = findViewById(R.id.btnClear);
    }

    private void setupSpinnerListener() {
        spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = parent.getItemAtPosition(position).toString();
                loadUserData(selectedName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 无操作
            }
        });
    }

    private void setupButtonListeners() {
        btnAdd.setOnClickListener(v -> addUser());
        btnSave.setOnClickListener(v -> saveUser());
        btnDelete.setOnClickListener(v -> deleteUser());
        btnClear.setOnClickListener(v -> clearFields());
    }

    // 加载 Spinner 数据
    private void loadSpinnerData() {
        Cursor cursor = db.query(
                UserDBHelper.TABLE_USERS,
                new String[]{UserDBHelper.COLUMN_NAME},
                null, null, null, null, null
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        while (cursor.moveToNext()) {
            adapter.add(cursor.getString(0));
        }
        cursor.close();

        spinnerUsers.setAdapter(adapter);
    }

    // 加载用户数据到界面
    private void loadUserData(String name) {
        Cursor cursor = db.query(
                UserDBHelper.TABLE_USERS,
                null,
                UserDBHelper.COLUMN_NAME + "=?",
                new String[]{name},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            // 检查列名是否存在，避免返回 -1
            int nameIndex = cursor.getColumnIndex(UserDBHelper.COLUMN_NAME);
            int passwordIndex = cursor.getColumnIndex(UserDBHelper.COLUMN_PASSWORD);
            int phoneIndex = cursor.getColumnIndex(UserDBHelper.COLUMN_PHONE);
            int emailIndex = cursor.getColumnIndex(UserDBHelper.COLUMN_EMAIL);
            int genderIndex = cursor.getColumnIndex(UserDBHelper.COLUMN_GENDER);

            if (nameIndex == -1 || passwordIndex == -1 || phoneIndex == -1 || emailIndex == -1 || genderIndex == -1) {
                Toast.makeText(this, "数据库字段缺失，请检查表结构", Toast.LENGTH_SHORT).show();
                return;
            }

            // 安全获取数据
            etName.setText(cursor.getString(nameIndex));
            etPassword.setText(cursor.getString(passwordIndex));
            etPhone.setText(cursor.getString(phoneIndex));
            etEmail.setText(cursor.getString(emailIndex));
            String gender = cursor.getString(genderIndex);
            radioMale.setChecked(gender.equals("男"));
            radioFemale.setChecked(gender.equals("女"));
        }
        cursor.close();
    }

    // 添加用户
    private void addUser() {
        ContentValues values = new ContentValues();
        values.put(UserDBHelper.COLUMN_NAME, etName.getText().toString().trim());
        values.put(UserDBHelper.COLUMN_PASSWORD, etPassword.getText().toString().trim());
        values.put(UserDBHelper.COLUMN_PHONE, etPhone.getText().toString().trim());
        values.put(UserDBHelper.COLUMN_EMAIL, etEmail.getText().toString().trim());
        String gender = radioMale.isChecked() ? "男" : "女";
        values.put(UserDBHelper.COLUMN_GENDER, gender);

        long result = db.insert(UserDBHelper.TABLE_USERS, null, values);
        if (result != -1) {
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            loadSpinnerData();
            clearFields();
        } else {
            Toast.makeText(this, "添加失败（用户名已存在）", Toast.LENGTH_SHORT).show();
        }
    }

    // 保存用户修改
    private void saveUser() {
        String oldName = spinnerUsers.getSelectedItem().toString();
        ContentValues values = new ContentValues();
        values.put(UserDBHelper.COLUMN_NAME, etName.getText().toString().trim());
        values.put(UserDBHelper.COLUMN_PASSWORD, etPassword.getText().toString().trim());
        values.put(UserDBHelper.COLUMN_PHONE, etPhone.getText().toString().trim());
        values.put(UserDBHelper.COLUMN_EMAIL, etEmail.getText().toString().trim());
        String gender = radioMale.isChecked() ? "男" : "女";
        values.put(UserDBHelper.COLUMN_GENDER, gender);

        int rowsAffected = db.update(
                UserDBHelper.TABLE_USERS,
                values,
                UserDBHelper.COLUMN_NAME + "=?",
                new String[]{oldName}
        );

        if (rowsAffected > 0) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            loadSpinnerData();
        } else {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    // 删除用户
    private void deleteUser() {
        String name = spinnerUsers.getSelectedItem().toString();
        int rowsDeleted = db.delete(
                UserDBHelper.TABLE_USERS,
                UserDBHelper.COLUMN_NAME + "=?",
                new String[]{name}
        );

        if (rowsDeleted > 0) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            loadSpinnerData();
            clearFields();
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    // 清空输入框
    private void clearFields() {
        etName.setText("");
        etPassword.setText("");
        etPhone.setText("");
        etEmail.setText("");
        radioGroupGender.clearCheck();
        radioMale.setChecked(true); // 默认选中“男”
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}