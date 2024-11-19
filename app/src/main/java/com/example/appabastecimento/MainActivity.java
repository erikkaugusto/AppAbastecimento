package com.example.appabastecimento;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText dataAbastecimento;
    private EditText quantLitros;
    private EditText quilomCarro;
    private EditText valorAbastecido;
    private Button botaoHistorico;
    private Button botaoSalvar;
    private Button botaoEstatisticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dataAbastecimento = findViewById(R.id.dataAbastecimento);
        quantLitros = findViewById(R.id.quantLitros);
        quilomCarro = findViewById(R.id.quilomCarro);
        valorAbastecido = findViewById(R.id.valorAbastecido);
        botaoHistorico = findViewById(R.id.botaoHistorico);
        botaoSalvar = findViewById(R.id.botaoSalvar);
        botaoEstatisticas = findViewById(R.id.botaoEstatisticas);

        try {
            SQLiteDatabase banco = openOrCreateDatabase("app1", MODE_PRIVATE, null);

            banco.execSQL("CREATE TABLE IF NOT EXISTS abastecimentos (" +
                    "dataabastecimento DATE NOT NULL, " +
                    "quantidadelitros DECIMAL NOT NULL," +
                    "quilomcarro INTEGER NOT NULL," +
                    "valortotal DECIMAL NOT NULL" +
                    ")");
            banco.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void salvar(View v) {

        if (dataAbastecimento.getText().toString().isEmpty()
                || quantLitros.getText().toString().isEmpty()
                || quilomCarro.getText().toString().isEmpty()
                || valorAbastecido.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Por favor, preencha todos os dados",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String data = dataAbastecimento.getText().toString();
            String quantidade = quantLitros.getText().toString();
            String km = quilomCarro.getText().toString();
            String valor = valorAbastecido.getText().toString();

            SQLiteDatabase banco = openOrCreateDatabase("app1", MODE_PRIVATE, null);

            String sql = "INSERT INTO abastecimentos " +
                    "(dataabastecimento, quantidadelitros, quilomcarro, valortotal) " +
                    "VALUES " +
                    "('" + data + "', '" + quantidade + "', '" + km + "', '" + valor + "')";
            banco.execSQL(sql);

            Toast.makeText(getApplicationContext(),
                    "Abastecimento cadastrado!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Verifique se todos os campos estão preenchidos!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void abrirHistorico (View v){
        Intent telaHistorico = new Intent(this, Historico.class);
        startActivity(telaHistorico);
    }
}