package alertas_equipo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import reporte.AdapterHistorialAlertas;
import reporte.AdapterReportes;
import reporte.DescripcionReporte;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertaEquipo extends Fragment {

    EditText txtNombreEquipo;
    AdapterHistorialAlertas adapterUsuario;
    RecyclerView recycler;
     ArrayList<Reporte> wa;
    public AlertaEquipo() {
        // Required empty public constructor
    }

    public  static  boolean ocultar;
    public static AlertaEquipo entrar (boolean ocultar_)
    {
        ocultar = ocultar_;
        return new AlertaEquipo ();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
       View v =  inflater.inflate(R.layout.fragment_alerta_equipo, container, false);

          recycler = (RecyclerView)v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        txtNombreEquipo = (EditText)v.findViewById(R.id.txtNombreEquipo);


        v.findViewById(R.id.btnBuscar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarEquipo();
            }
        });

        return v;

    }

    private void buscarEquipo() {

        ApiService service1 = RetroClient.getApiService();
        Call<List<Reporte>> call = service1.buscarEquipo(txtNombreEquipo.getText().toString());

        call.enqueue(new Callback<List<Reporte>>() {
            @Override
            public void onResponse(Call<List<Reporte>> call, Response<List<Reporte>> response) {
                Log.e("Reporte dosn","Download"+response.body());
                Log.e("Reporte dosn","Download"+ response.body().get(0).getRuta());



              wa = (ArrayList<Reporte>) response.body();



                ArrayList<Reporte> lista = new ArrayList<>();


                if (wa.size()>0)
                {

                    String status="";
                    for (int x = 0; x< wa.size();x++) {

                        if (status.equals("")) {
                            lista.add(new Reporte("Reportado"));
                            status = wa.get(x).getStatus();
                        } else if (!status.equals(wa.get(x).getStatus())) {
                            status = wa.get(x).getStatus();
                            lista.add(new Reporte("Levantado"));
                        }

                        lista.add(wa.get(x));
                    }

                }






                adapterUsuario  = new AdapterHistorialAlertas(lista,getActivity());
                recycler.setAdapter(adapterUsuario);

                adapterUsuario.setOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Reporte usuario = wa.get(recycler.getChildAdapterPosition(v));
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, DescripcionReporte.entrar(usuario,ocultar)).commit();
                    }
                });





            }
            @Override
            public void onFailure(Call<List<Reporte>> call, Throwable t) {
                Log.e("onFailure Mensaje", t.toString());
                Log.e("onFailure Mensaje", call.toString());

                if (wa!=null)
                {
                    if (wa.size()>0)
                    {
                        final int size = wa.size();
                        wa.clear();
                        adapterUsuario.notifyItemRangeRemoved(0, size);
                    }
                }

            }
        });
    }



}
