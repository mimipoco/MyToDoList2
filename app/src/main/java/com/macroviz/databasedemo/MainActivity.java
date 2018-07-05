package com.macroviz.databasedemo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //color用
    Spinner spinner;
    ArrayAdapter<CharSequence> data;
    ArrayList<ColorItem> colorItems;
    SpinnerAdapter spinnerAdapter;
    ArrayList<String> color_tickets;
    String[] color_names, color_codes;
    LinearLayout itembg;
    TextView textView5;
    ImageView imageView;
    //

    private DbAdapter dbAdapter;
    Button btna;
    TextView no_contact;
    ListView list_contacts;
    private Intent intent;
    private SimpleCursorAdapter dataSimpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //color用
        spinner = findViewById(R.id.spinner);
        //ArrayAdapter<CharSequence> data = ArrayAdapter.createFromResource(
        //this, R.array.items, android.R.layout.simple_spinner_item );
        //spinner.setAdapter(data);

        colorItems = new ArrayList<ColorItem>();
        colorItems.add(new ColorItem("Red","#ff0000"));
        colorItems.add(new ColorItem("Green","#00c7a4"));
        colorItems.add(new ColorItem("Blue","#4b7bd8"));
       // colorItems.add(new ColorItem("Purple","#cf97ca"));
        spinnerAdapter = new SpinnerAdapter(this,colorItems);
        spinner.setAdapter(spinnerAdapter);
        //
        itembg = findViewById(R.id.itemBG);
        //itembg.setBackgroundColor(Color.RED);
        textView5=findViewById(R.id.textView5);
        //textView5.setBackgroundColor(Color.parseColor(  ));

        imageView =findViewById(R.id.imageView);


        //

        dbAdapter = new DbAdapter(this);
        Log.i("dbCount=",String.valueOf(dbAdapter.listContacts().getCount()));
        no_contact = findViewById(R.id.no_contact);
        list_contacts = findViewById(R.id.list_contacts);
        //判斷目前是否有聯絡人資料並設定顯示元件，如果是0，就顯示「目前無資料」
        if(dbAdapter.listContacts().getCount() == 0){
            list_contacts.setVisibility(View.INVISIBLE);
            no_contact.setVisibility(View.VISIBLE);

        }else{
            list_contacts.setVisibility(View.VISIBLE);
            no_contact.setVisibility(View.INVISIBLE);
        }
        displayList();
    }

    private void displayList(){
        Cursor cursor = dbAdapter.listContacts();
        String[] columns = new String[]{
                dbAdapter.KEY_BIRTH,//日期
                dbAdapter.KEY_NAME,//TYPE:記事,支出,收入
                dbAdapter.KEY_PHONE,//IF TYPE= 支出or收入,錢
                dbAdapter.KEY_EMAIL,//內容

        };
        int[] view = new int[]{
                R.id.txtBirth,
                R.id.txtName,
                R.id.txtPhone,
                R.id.txtMail,
        };
        dataSimpleAdapter = new SimpleCursorAdapter(this, R.layout.item_view, cursor, columns, view, 0);
        list_contacts.setAdapter(dataSimpleAdapter);
        list_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor current_cursor = (Cursor) list_contacts.getItemAtPosition(position);
                int item_id = current_cursor.getInt(current_cursor.getColumnIndexOrThrow("_id"));
                intent = new Intent();
                intent.putExtra("item_id", item_id);
                intent.setClass(MainActivity.this, ShowActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                Intent i = new Intent(this, EditActivity.class);
                i.putExtra("type","add");
                startActivity(i);

                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
