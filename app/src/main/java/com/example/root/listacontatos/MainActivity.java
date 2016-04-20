package com.example.root.listacontatos;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btnSalvar;
    EditText txtNome, txtTelefone;

    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirW();
            }
        });

        CarregarLista(this);

    }

    public void AbrirW() {
        txtNome = (EditText) findViewById(R.id.txtName);
        txtTelefone = (EditText) findViewById(R.id.txtPhone);

        LayoutInflater li = getLayoutInflater();
        final View view = li.inflate(R.layout.alerta, null);


        view.findViewById(R.id.btnSalvar).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                SalvaContato(view);
                Toast.makeText(MainActivity.this, "Contato salvo com sucesso", Toast.LENGTH_SHORT).show();
                alerta.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Novo Contato");
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void SalvaContato(View view) {
        txtNome = (EditText) view.findViewById(R.id.txtName);
        txtTelefone = (EditText) view.findViewById(R.id.txtPhone);

        ContextoDados db = new ContextoDados(this);
        db.InserirContato(txtNome.getText().toString(), txtTelefone.getText().toString());
        CarregarLista(this);
    }

    public void CarregarLista(Context c) {
        ArrayList<String> items = new ArrayList<String>();

        ContextoDados db = new ContextoDados(c);
        ContextoDados.ContatosCursor cursor = db.RetornarContatos(ContextoDados.ContatosCursor.OrdenarPor.NomeCrescente);

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            String contato = cursor.getString(1)+ ":"+ cursor.getString(2);
            items.add(contato);
            MostraLista(cursor, items);
        }
        cursor.close();
    }

    public void MostraLista(Cursor cursor, ArrayList items) {
        ListView ls = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> itemsAdapter = null;

        ArrayList<String> item = new ArrayList<String>();
        String nome;

        if (itemsAdapter == null) {
            itemsAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    items);
            ls.setAdapter(itemsAdapter);
        } else {
            itemsAdapter.clear();
            itemsAdapter.addAll(items);
            itemsAdapter.notifyDataSetChanged();
        }

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();

                String[] pieces = name.split(":");

                Toast.makeText(MainActivity.this, pieces[1], Toast.LENGTH_SHORT).show();
            }
        });
    }
}

