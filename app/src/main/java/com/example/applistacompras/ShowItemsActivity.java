package com.example.applistacompras;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShowItemsActivity extends Activity {
    private List<String> itemList;
    private ArrayAdapter<String> adapter;
    private ListView itemListView;
    private Button addButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_items);

        sharedPreferences = getSharedPreferences("MyItemList", MODE_PRIVATE);

        itemList = loadItemList(); // Carrega a lista de itens
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, itemList);
        itemListView = findViewById(R.id.itemListView);
        addButton = findViewById(R.id.addButton);

        itemListView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addItemIntent = new Intent(ShowItemsActivity.this, AddItemActivity.class);
                startActivityForResult(addItemIntent, 1);
            }
        });

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                itemListView.setItemChecked(position, !itemListView.isItemChecked(position));
            }
        });

        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int itemPosition = position;
                final String selectedItem = itemList.get(itemPosition);

                // Crie um AlertDialog para escolher entre excluir ou editar
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowItemsActivity.this);
                builder.setTitle("Escolha uma ação");
                builder.setItems(new CharSequence[]{"Editar", "Excluir"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0: // Editar
                                Intent editItemIntent = new Intent(ShowItemsActivity.this, AddItemActivity.class);
                                editItemIntent.putExtra("editItem", selectedItem); // Passa o item selecionado para edição
                                startActivityForResult(editItemIntent, 2); // Usamos um requestCode diferente para a edição
                                break;

                            case 1: // Excluir
                                itemList.remove(itemPosition);
                                adapter.notifyDataSetChanged();
                                saveItemList(itemList); // Salve a lista atualizada
                                break;
                        }
                    }
                });
                builder.show();

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newItem = data.getStringExtra("newItem");
            if (newItem != null) {
                itemList.add(newItem);
                adapter.notifyDataSetChanged();
                saveItemList(itemList); // Salve a lista atualizada
            }
        }
    }

    private List<String> loadItemList() {
        String itemListJson = sharedPreferences.getString("itemList", "[]");
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(itemListJson, type);
    }

    private void saveItemList(List<String> list) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String itemListJson = new Gson().toJson(list);
        editor.putString("itemList", itemListJson);
        editor.apply();
    }
}
