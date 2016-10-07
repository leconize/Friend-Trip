package com.example.impchanii.week7;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
    }

    public void confirmSignUp(View view){
        Intent i = new Intent();
        EditText edit = (EditText) findViewById(R.id.idsignupinput);
        Editable editable = edit.getText();
        String allthetext = editable.toString();
        i.putExtra("idsignup", allthetext);
        setResult(RESULT_OK, i);
        finish();
    }
}
