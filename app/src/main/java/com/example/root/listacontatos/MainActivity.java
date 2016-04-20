package com.example.root.listacontatos;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnSalvar, btnCancelar, btnNovo;
    EditText txtNome, txtTelefone;

    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.alerta, null);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirW();
            }
        });

    }

    public void AbrirW(){
        LayoutInflater li = getLayoutInflater();
        View view = li.inflate(R.layout.alerta, null);
        Button btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "alerta.dismiss()", Toast.LENGTH_SHORT).show();
                alerta.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Novo Contato");
        builder.setView(view);
        alerta = builder.create();
        alerta.show();
        CarregaContato();

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

    public void CarregaContato(){
        txtNome = (EditText) findViewById(R.id.txtName);
        txtTelefone = (EditText) findViewById(R.id.txtPhone);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalvaContato();
            }
        });
    }

    public void SalvaContato(){
        BaseDAO db  = new BaseDAO(this);
        db.InserirContato(txtNome.getText().toString(), txtTelefone.getText().toString());
        setContentView(R.layout.content_main);
        CarregarLista(this);

    }

    public  void CarregarLista(Context c){
        BaseDAO db = new BaseDAO(c);
        BaseDAO.ContatosCursor cursor = db.RetornarContatos(BaseDAO.ContatosCursor.OrdenarPor.NomeCrescente);

        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            MostraLista(cursor);
        }
    }
 public void MostraLista(Cursor cursor){

     ListView ls = (ListView) findViewById(R.id.listView);


         ls.setAdapter((ListAdapter) cursor);


 }

}

