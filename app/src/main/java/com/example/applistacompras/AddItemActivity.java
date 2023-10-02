package com.example.applistacompras;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddItemActivity extends Activity {


        private EditText itemEditText;
        private Button addButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_item);

            itemEditText = findViewById(R.id.itemEditText);
            addButton = findViewById(R.id.addButton);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newItem = itemEditText.getText().toString().trim();
                    if (!newItem.isEmpty()) {
                        Intent intent = new Intent();
                        intent.putExtra("newItem", newItem);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });
        }
    }
