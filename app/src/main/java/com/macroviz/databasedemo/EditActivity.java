package com.macroviz.databasedemo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtTitle;
    EditText edt_name, edt_phone, edt_mail, edt_birth;
    Button btn_ok, btn_back;
    String new_name, new_phone, new_mail, new_birth, new_mytype;
    String type_radio;
    Bundle bundle;
    //jun+Radio
    RadioGroup rg;
    int myType;
    int index;
    private int mYear, mMonth, mDay;
    private DbAdapter dbAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();
        dbAdapter = new DbAdapter(this);
        bundle = this.getIntent().getExtras();
        //判斷目前是否為編輯狀態
        if(bundle.getString("type").equals("edit")){
            txtTitle.setText("編輯記事");
            index = bundle.getInt("item_id");
            Cursor cursor = dbAdapter.queryById(index);



//            switch(cursor.getString(1)){
//                case "1":
//                    type_radio = "收入";
//                    break;
//                case "2":
//                    type_radio = "支出";
//                    break;
//                case "3":
//                    type_radio = "備忘";
//                    break;
//            }
//
//            edt_name.setText(type_radio);
            edt_name.setText(cursor.getString(1));


            edt_phone.setText(cursor.getString(2));
            edt_mail.setText(cursor.getString(3));
            edt_birth.setText(cursor.getString(4));
        }
    }
    private void initView(){
        txtTitle = findViewById(R.id.txtTitle);
        edt_name = findViewById(R.id.edtName);
        edt_phone = findViewById(R.id.edtPhone);
        edt_mail = findViewById(R.id.edtEmail);
        edt_birth = findViewById(R.id.edtBirth);
        edt_name.setOnClickListener(this);
        edt_phone.setOnClickListener(this);
        edt_mail.setOnClickListener(this);
        edt_birth.setOnClickListener(this);
        btn_ok = findViewById(R.id.btn_ok);
        btn_back = findViewById(R.id.btn_back);
        btn_ok.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        //jun +Radio
        rg = findViewById(R.id.radioGroup1);
        //rg.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edtName:
                if(bundle.getString("type").equals("add")) edt_name.setText("");
                break;
            case R.id.edtPhone:
                if(bundle.getString("type").equals("add")) edt_phone.setText("");
                break;
            case R.id.edtEmail:
                if(bundle.getString("type").equals("add")) edt_mail.setText("");
                break;
            case R.id.edtBirth:
                if(bundle.getString("type").equals("add")) edt_birth.setText("");
                //取得今天日期
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //將選定日期設定至edt_birth
                        edt_birth.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
                break;

            case R.id.btn_ok:

                //jun+
                switch(rg.getCheckedRadioButtonId()){
                    case R.id.rgIncome:
                        myType =1;
                        break;
                    case R.id.rgOutput:
                        myType =2;
                        break;
                    case R.id.rgPs:
                        myType =3;
                        break;
                }
                new_mytype = String.valueOf(myType);
                Toast.makeText(EditActivity.this, new_mytype, Toast.LENGTH_LONG).show();

                //取得edit資料
                //new_name = edt_name.getText().toString();
                //jun+TYPE
                new_name = new_mytype;
                new_phone = edt_phone.getText().toString();
                new_mail = edt_mail.getText().toString();
                new_birth = edt_birth.getText().toString();

                if(bundle.getString("type").equals("edit")){
                    try{
                        //更新資料庫中的資料
                        dbAdapter.updateContacts(index, new_name, new_phone, new_mail, new_birth);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        //回到ShowActivity<-jun改回到Main
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }
                }else {
                    //測試是否有被log出來
                    Log.i("new_name=", new_name);
                    Log.i("new_phone=", new_phone);
                    Log.i("new_mail=", new_mail);
                    try {
                        //呼叫adapter的方法處理新增
                        dbAdapter.createContact(new_name, new_phone, new_mail, new_birth);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //回到列表
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    }
                }
                break;

                //jun
            case R.id.btn_back:
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);

                //jun+radio OK
//                switch(rg.getCheckedRadioButtonId()){
//                    case R.id.rgIncome:
//                        myType =1;
//                        break;
//                    case R.id.rgOutput:
//                        myType =2;
//                        break;
//                    case R.id.rgPs:
//                        myType =3;
//                        break;
//                }
//                Toast.makeText(EditActivity.this, String.valueOf(myType), Toast.LENGTH_LONG).show();
                break;
        }
    }
}
