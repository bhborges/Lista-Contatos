package com.example.root.listacontatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by root on 19/04/16.
 */
public class ContextoDados extends SQLiteOpenHelper {

    private static final String NOME_BD = "Agenda";
    private static final int VERSAO_BD = 2;
    private static final String LOG_TAG = "Agenda";
    private final Context contexto;
    public ContextoDados(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.ContextoDados_onCreate).split("\n");
        db.beginTransaction();

        try
        {
            ExecutarComandosSQL(db, sql);
            db.setTransactionSuccessful();
        }
        catch (SQLException e)
        {
            Log.e("Erro ao criar tabela", e.toString());
        }
        finally
        {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(LOG_TAG, "Atualizando a base de dados da versão " + oldVersion + " para " + newVersion + ", que destruirá todos os dados antigos");
        String[] sql = contexto.getString(R.string.ContextoDados_onUpdate).split("\n");
        db.beginTransaction();
        try
        {
            ExecutarComandosSQL(db, sql);
            db.setTransactionSuccessful();
        }
        catch (SQLException e)
        {
            Log.e("Erro ao atualiz tabela", e.toString());
            throw e;
        }
        finally
        {
            db.endTransaction();
        }

        onCreate(db);
    }


    private void ExecutarComandosSQL(SQLiteDatabase db, String[] sql)
    {
        for( String s : sql )
        if (s.trim().length()>0)
        db.execSQL(s);
    }



    public ContatosCursor RetornarContatos(ContatosCursor.OrdenarPor ordenarPor)
    {
        String sql = ContatosCursor.CONSULTA + (ordenarPor == ContatosCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        ContatosCursor cc = (ContatosCursor) bd.rawQueryWithFactory(new ContatosCursor.Factory(), sql, null, null);
        String nome;
        if (cc != null && !cc.isClosed()) {
            cc.moveToFirst();
        }
        return cc;
    }

    public long InserirContato(String nome, String telefone)
    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Nome", nome);
            initialValues.put("Telefone", telefone);
            return db.insert("Contatos", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public static class ContatosCursor extends SQLiteCursor
    {
        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM Contatos ORDER BY Nome ";

        private ContatosCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new ContatosCursor(db, driver, editTable, query);
            }
        }

        public long getID()
        {
            return getLong(getColumnIndexOrThrow("ID"));
        }

        public String getNome()
        {
            return getString(getColumnIndexOrThrow("Nome"));
        }

        public String getTelefone()
        {
            return getString(getColumnIndexOrThrow("Telefone"));
        }
    }
}


