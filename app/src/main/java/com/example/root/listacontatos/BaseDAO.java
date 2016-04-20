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

/**
 * Created by root on 19/04/16.
 */
public class BaseDAO extends SQLiteOpenHelper {



    public static final String TBL_AGENDA = "agenda";
    public static final String AGENDA_ID = "_id";
    public static final String AGENDA_NOME = "nome";
    public static final String AGENDA_TELEFONE = "telefone";

    private static final String DATABASE_NAME = "agenda.db";
    private static final int DATABASE_VERSION = 2;

    //Estrutura da tabela Agenda (sql statement)
    private static final String CREATE_AGENDA = "create table " +
            TBL_AGENDA + "( " + AGENDA_ID       + " integer primary key autoincrement, " +
            AGENDA_NOME     + " text not null, " +
            AGENDA_TELEFONE + " text not null);";

    public BaseDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_AGENDA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_AGENDA);
        onCreate(db);
    }


    public ContatosCursor RetornarContatos(ContatosCursor.OrdenarPor ordenarPor)
    {
        String sql = ContatosCursor.CONSULTA + (ordenarPor == ContatosCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        ContatosCursor cc = (ContatosCursor) bd.rawQueryWithFactory(new ContatosCursor.Factory(), sql, null, null);
        cc.moveToFirst();
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


