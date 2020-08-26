package com.kolastudios;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aquery.AQuery;

public class BaseActivity extends AppCompatActivity {
    APIInterface apiInterface;
    AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        aq = new AQuery(this);
    }
}
