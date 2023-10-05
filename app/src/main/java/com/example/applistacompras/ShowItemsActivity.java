package com.example.applistacompras;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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

    //cria lista que sera usada para mostrar a lista de compras, ou seja,
    // cada string adicionada
    private List<String> itemList;

    //cria um adaptador para vincular a lista com uma Listview e exibila na tela
    private ArrayAdapter<String> adapter;

    //referencia a Listview que sera usada no layout para mostrar as strings
    private ListView itemListView;

    //recupera botão de add
    private Button addButton;

    //armazenamento local do android, persiste e mantem dados entre diferentes sessoes
    private SharedPreferences sharedPreferences;


    //cria a activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //chama o layout responsavel
        setContentView(R.layout.show_items);

        //inicia o sharedpreferences, criando um arquivo para armazenar os dados "MyItemList"
        //e define esse arquivo como privado, apenas o app tem acesso
        sharedPreferences = getSharedPreferences("MyItemList", MODE_PRIVATE);

        itemList = loadItemList(); // Carrega a lista de itens
        //inicializa o adapter e vincula um layout pronto  do android para cada item para mostrar a lista,
        // e a propria lista de string
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, itemList);
        //ecupera a listView pelo id
        itemListView = findViewById(R.id.itemListView);
        //recupera botao de add
        addButton = findViewById(R.id.addButton);

        //define o adapter na list view, para ser vinculada a lista e exibir todos os itens ao usuario
        itemListView.setAdapter(adapter);

        //add um ouvinte ao botão, agora ele escuta a novas interações
        addButton.setOnClickListener(new View.OnClickListener() {
            //implementa o metodo quando o botão for clicado
            @Override
            public void onClick(View view) {
                //cria um Intent para ir da activity atual até a activity de add itens
                Intent addItemIntent = new Intent(ShowItemsActivity.this, AddItemActivity.class);
                //inicia a activity de destino
                //usa 'startActivityForResult' pois queremos armazenar o resultado
                //quando a activity for concluida
                startActivityForResult(addItemIntent, 1);
            }
        });

        //add um ouvinte a cada item da lista, agora ele escuta a novas interações
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //quando um item for clicado...
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //faz o efeito de marcação de um item quando clicado
                itemListView.setItemChecked(position, !itemListView.isItemChecked(position));


            }
        });

        //add um ouvinte ao item da lista, agora ele escuta interações
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            //implementa o metodo quando um item é pressionado
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int itemPosition = position;
                final String selectedItem = itemList.get(itemPosition);

                // Cria um AlertDialog para  excluir
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowItemsActivity.this);
                //seta o titulo do alert dialogue
                builder.setTitle(getString(R.string.deseja_excluir));

                //define um botao positivo para a pergunta do alert
                builder.setPositiveButton(getString(R.string.excluir), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //remove o item da lista de strings usando a posicao
                        itemList.remove(itemPosition);
                        //notifica o adaptador que a lista sera atualizada
                        adapter.notifyDataSetChanged();
                        saveItemList(itemList); // Salve a lista atualizada
                        //mostra toast de sucesso
                        showToast(getString(R.string.item_excluido_com_sucesso));

                    }
                });
                //define um botao negativo para cancelar e nao executa nenhuma acao
                builder.setNegativeButton(getString(R.string.cancelar), null);
                //mostra o alert na tela
                builder.show();

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //se o codigo e o resultado da activity forem esses
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //captura o novo item add
            String newItem = data.getStringExtra("newItem");
            //se ele nao for vazio
            if (newItem != null) {
                //add novo item na lista
                itemList.add(newItem);
                //notifica as alteraçoes ao adapter
                adapter.notifyDataSetChanged();
                saveItemList(itemList); // Salve a lista atualizada
            }
        }
    }

    //funçaõ que recupera a lista de itens salvos
    private List<String> loadItemList() {
        //recupera cada item em uma variavel
        //fazendo um get em cada string da lista usando uma chave e recuperando a lista toda[]
        String itemListJson = sharedPreferences.getString("itemList", "[]");

        //o type armazena o tipo que sera convertido, no casi lista de strings
        Type type = new TypeToken<List<String>>() {}.getType();
        //retorna uma conversao do json em uma lista de strings
        //usando a biblioteca Gson
        return new Gson().fromJson(itemListJson, type);
    }

    //função para salvar item na lista
    private void saveItemList(List<String> list) {
        //cria um objeto do SharedPreferences editor
        //onde sera possivel alterar a SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //converte a lista recebida nos parametros e, json
        //com a biblioteca Gson
        String itemListJson = new Gson().toJson(list);
        //coloca a nova string usando o editor
        // e a chave "itemList" para armazenar a nova string em 'itemListJson'
        editor.putString("itemList", itemListJson);
        //aplica as alteraçoes feitas com o editor
        editor.apply();
    }

    //função que recebe uma string e gera um Toast para ser mostrado ao usuário
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}




