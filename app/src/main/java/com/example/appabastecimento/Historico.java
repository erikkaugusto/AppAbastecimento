package com.example.appabastecimento;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Historico extends AppCompatActivity {

    private TextView campo;
    private Button botaoVoltar;
    private String texto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historico);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        campo = findViewById(R.id.campo);
        botaoVoltar = findViewById(R.id.botaoVoltar);

        botaoVoltar.setOnClickListener(v -> finish());

        try {
            SQLiteDatabase banco = openOrCreateDatabase("app", MODE_PRIVATE, null);
            Cursor lista = banco.rawQuery("SELECT * FROM abastecimentos", null);

            if (lista.moveToFirst()) {
                do {
                    String data = lista.getString(lista.getColumnIndexOrThrow("dataabastecimento"));
                    String quantidade = lista.getString(lista.getColumnIndexOrThrow("quantidadelitros"));
                    String quilometragem = lista.getString(lista.getColumnIndexOrThrow("quilomcarro"));
                    String valor = lista.getString(lista.getColumnIndexOrThrow("valortotal"));

                    texto += "\nDATA: " + data +
                            "\nQUANTIDADE ABASTECIDA: " + quantidade + " litros" +
                            "\nQUILOMETRAGEM: " + quilometragem + " km" +
                            "\nVALOR: R$ " + valor +
                            "\n----------------";
                } while (lista.moveToNext());
            } else {
                texto = "Nenhum registro encontrado.";
            }

            campo.setText(texto);

            banco.close();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Ocorreu um erro ao carregar o histórico!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}