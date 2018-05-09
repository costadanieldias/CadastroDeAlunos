package br.com.caelum.cadastro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.cadastro.vo.Aluno;

/**
 * Created by android5497 on 03/09/15.
 */
public class AlunoDAO extends SQLiteOpenHelper {

    private static final int VERSAO = 2;
    private static final String DATABASE = "Cadastro";
    private static final String TABELA = "Aluno";

    public AlunoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE " + TABELA +
                       "(id INTEGER PRIMARY KEY," +
                       "nome TEXT NOT NULL," +
                       "endereco TEXT," +
                       "telefone TEXT," +
                       "site TEXT," +
                       "nota REAL," +
                       "caminhoFoto TEXT);";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String query = "ALTER TABLE " + TABELA + " ADD COLUMN caminhoFoto TEXT;";
        database.execSQL(query);
    }

    public void inserir(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("endereco", aluno.getEndereco());
        values.put("telefone", aluno.getTelefone());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("caminhoFoto", aluno.getCaminhoFoto());

        super.getWritableDatabase().insert(TABELA, null, values);
    }

    public void alterar(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("endereco", aluno.getEndereco());
        values.put("telefone", aluno.getTelefone());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("caminhoFoto", aluno.getCaminhoFoto());

        super.getWritableDatabase().update(TABELA, values, "id=?", new String[] { aluno.getId().toString() });
    }

    public List<Aluno> getLista() {
        List<Aluno> alunos = new ArrayList<>();

        Cursor c = super.getReadableDatabase().rawQuery("SELECT * FROM " + TABELA + ";", null);

        while (c.moveToNext()) {
            Aluno aluno = new Aluno();
            aluno.setId(c.getLong(c.getColumnIndex("id")));
            aluno.setNome(c.getString(c.getColumnIndex("nome")));
            aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
            aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
            aluno.setSite(c.getString(c.getColumnIndex("site")));
            aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
            aluno.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);
        }

        c.close(); // sempre fechar o cursor, pois o android nao tem destrutor

        return alunos;
    }

    public void deletar(Aluno aluno) {
        String[] args = { aluno.getId().toString() };
        super.getWritableDatabase().delete(TABELA, "id=?", args);
    }

    public boolean isAluno(String telefone) {
        String[] parametros = {telefone};
        Cursor rawQuery = getReadableDatabase()
                .rawQuery("SELECT telefone FROM " + TABELA
                        + " WHERE telefone = ?", parametros);

        int total = rawQuery.getCount();
        rawQuery.close();
        return total > 0;
    }
}
