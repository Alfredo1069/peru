package reporte;


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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListadoReportes extends Fragment {

    AdapterHistorialAlertas adapterUsuario;

    public ListadoReportes() {
        // Required empty public constructor
    }
static  boolean ocultar;
    public static ListadoReportes entrar (boolean ocultar_)
    {
        ocultar = ocultar_;
        return new ListadoReportes ();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_listado_reportes, container, false);


        final RecyclerView recycler = (RecyclerView)v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));



        ApiService service1 = RetroClient.getApiService();
        Call<List<Reporte>> call = service1.get_Messages();

        call.enqueue(new Callback<List<Reporte>>() {
            @Override
            public void onResponse(Call<List<Reporte>> call, Response<List<Reporte>> response) {
                Log.e("Reporte dosn","Download"+response.body());
                Log.e("Reporte dosn","Download"+ response.body().get(0).getRuta());



                final ArrayList<Reporte> wa = (ArrayList<Reporte>) response.body();
                ArrayList<Reporte> lista = new ArrayList<>();


                String status="";
                for (int x = 0; x< wa.size();x++)
                {

                    if (status.equals(""))
                    {
                        lista.add(new Reporte("Reportado"));
                        status = wa.get(x).getStatus();
                    }
                    else if (!status.equals(wa.get(x).getStatus()))
                    {
                        status = wa.get(x).getStatus();
                        lista.add(new Reporte("Levantado"));
                    }






                    lista.add(wa.get(x));


                    Log.e("Rutas",""+wa.get(x).getRuta());


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



            }
        });

        return v;
    }

}
