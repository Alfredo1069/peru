package peru.com.perux;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import agregar_usuario.AgregarUsuario;
import alertas_equipo.AlertaEquipo;
import alertas_unidades.AlertaPorUnidad;
import reporte.ListadoReportes;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewById(R.id.btnListaReportes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, ListadoReportes.entrar(true)).commit();
            }
        });


        findViewById(R.id.btnReporte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, SubirImagen.entrar()).commit();
            }
        });


        findViewById(R.id.btnAlertaUnidades).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, AlertaPorUnidad.entrar()).commit();
            }
        });

        findViewById(R.id.btnAgregarUsuario).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, AgregarUsuario.entrar()).commit();
            }
        });

        findViewById(R.id.btnAlertaGeneral).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, AlertaEquipo.entrar(true)).commit();
            }
        });

    }
}
