package agregar_usuario;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import alertas_unidades.AlertaPorUnidad;
import modelo.Reporte;
import peru.com.perux.ApiService;
import peru.com.perux.R;
import peru.com.perux.RetroClient;
import reporte.AdapterReportes;
import reporte.DescripcionReporte;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarUsuario extends Fragment {


    public AgregarUsuario() {
        // Required empty public constructor
    }


    public static AgregarUsuario entrar ()
    {
        return new AgregarUsuario ();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_agregar_usuario, container, false);

        final EditText lblNombreUsuario = (EditText)v.findViewById(R.id.lblNombreUsuario);


        v.findViewById(R.id.btnGuardar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService service1 = RetroClient.getApiService();
                Call<List<Reporte>> call = service1.insertar_usuario(lblNombreUsuario.getText().toString());

                call.enqueue(new Callback<List<Reporte>>() {
                    @Override
                    public void onResponse(Call<List<Reporte>> call, Response<List<Reporte>> response) {
                        Log.e("Reporte dosn","Download"+response.body());
                        Log.e("Reporte dosn","Download"+ response.body().get(0).getRuta());











                    }
                    @Override
                    public void onFailure(Call<List<Reporte>> call, Throwable t) {
                        Log.e("onFailure Mensaje", t.toString());
                        Log.e("onFailure Mensaje", call.toString());



                    }
                });
            }
        });

        return v;
    }

}
