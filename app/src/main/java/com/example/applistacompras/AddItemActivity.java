package com.example.applistacompras;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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
                        showToast(getString(R.string.item_adicionado_com_sucesso));
                    }
                }
            });

            Button backButton = findViewById(R.id.button);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Crie um Intent para iniciar a ActivityDestino
                    Intent intent = new Intent(AddItemActivity.this, ShowItemsActivity.class);

                    // Inicie a ActivityDestino
                    startActivity(intent);

                    // Opcional: Finalize a ActivityAtual se desejar que ela seja encerrada
                    finish();
                }
            });

        }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    }
