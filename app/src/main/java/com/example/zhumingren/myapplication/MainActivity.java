package com.example.zhumingren.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.facebookphotopicker.FacebookSignInActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btnSkip = (Button) findViewById(R.id.btn_skipto_signin);
        btnSkip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_skipto_signin) {
            startActivity(new Intent(this, FacebookSignInActivity.class));
        }
    }
}
