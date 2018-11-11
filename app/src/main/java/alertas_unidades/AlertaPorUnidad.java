package alertas_unidades;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import modelo.Reporte;
import peru.com.perux.ApiService;
import peru.com.perux.R;
import peru.com.perux.RetroClient;
import peru.com.perux.SubirImagen;
import reporte.AdapterReportes;
import reporte.DescripcionReporte;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertaPorUnidad extends Fragment {

    AdapterReportes adapterUsuario;
    public AlertaPorUnidad() {
        // Required empty public constructor
    }

    public static AlertaPorUnidad entrar ()
    {
        return new AlertaPorUnidad ();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_alerta_por_unidad, container, false);

        final RecyclerView recycler = (RecyclerView)v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));



        ApiService service1 = RetroClient.getApiService();
        Call<List<Reporte>> call = service1.obtener_unidades(1);

        call.enqueue(new Callback<List<Reporte>>() {
            @Override
            public void onResponse(Call<List<Reporte>> call, Response<List<Reporte>> response) {
                Log.e("Reporte dosn","Download"+response.body());
                Log.e("Reporte dosn","Download"+ response.body().get(0).getRuta());



                final ArrayList<Reporte> wa = (ArrayList<Reporte>) response.body();



                adapterUsuario  = new AdapterReportes(wa,getActivity());
                recycler.setAdapter(adapterUsuario);

                adapterUsuario.setOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Reporte usuario = wa.get(recycler.getChildAdapterPosition(v));
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, DescripcionReporte.entrar(usuario,false)).commit();
                    }
                });



            }
            @Override
            public void onFailure(Call<List<Reporte>> call, Throwable t) {
                Log.e("onFailure Mensaje", t.toString());
                Log.e("onFailure Mensaje", call.toString());



            }
        });

        return v;

    }

}
