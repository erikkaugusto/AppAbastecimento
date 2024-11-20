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

public class Estatisticas extends AppCompatActivity {

    private TextView custoKm;
    private TextView totalKm;
    private TextView consumoMedio;
    private Button botaoVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_estatisticas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        custoKm = findViewById(R.id.custoKm);
        totalKm = findViewById(R.id.totalKm);
        consumoMedio = findViewById(R.id.consumoMedio);
        botaoVoltar = findViewById(R.id.botaoVoltar);

        botaoVoltar.setOnClickListener(v -> finish());

        calcularEstatisticas();
    }

    private void calcularEstatisticas() {
        try {
            SQLiteDatabase banco = openOrCreateDatabase("app", MODE_PRIVATE, null);
            Cursor cursor = banco.rawQuery("SELECT quilomcarro, quantidadelitros, valortotal FROM abastecimentos ORDER BY dataabastecimento ASC", null);

            double totalKmRodado = 0.0;
            double totalLitrosConsumidos = 0.0;
            double totalValorGasto = 0.0;
            double quilometragemAnterior = -1;

            while (cursor.moveToNext()) {
                double quilometragemAtual = cursor.getDouble(cursor.getColumnIndexOrThrow("quilomcarro"));
                double litros = cursor.getDouble(cursor.getColumnIndexOrThrow("quantidadelitros"));
                double valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valortotal"));

                if (quilometragemAnterior >= 0) {
                    totalKmRodado += (quilometragemAtual - quilometragemAnterior);
                }
                quilometragemAnterior = quilometragemAtual;

                totalLitrosConsumidos += litros;
                totalValorGasto += valor;
            }

            double custoPorKm = (totalKmRodado > 0) ? totalValorGasto / totalKmRodado : 0.0;
            double consumoMedioLitros = (totalKmRodado > 0) ? totalLitrosConsumidos / totalKmRodado : 0.0;

            custoKm.setText(String.format("R$ %.2f", custoPorKm));
            totalKm.setText(String.format("%.2f km", totalKmRodado));
            consumoMedio.setText(String.format("%.2f litros/km", consumoMedioLitros));

            banco.close();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Ocorreu um erro ao calcular as estatísticas.", Toast.LENGTH_SHORT).show();
        }
    }
}