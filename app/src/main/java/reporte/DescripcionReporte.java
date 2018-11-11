package reporte;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;

import dmax.dialog.SpotsDialog;
import modelo.Reporte;
import peru.com.perux.ApiService;
import peru.com.perux.E_Response;
import peru.com.perux.MainActivity;
import peru.com.perux.R;
import peru.com.perux.RetroClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utilerias.Constantes;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescripcionReporte extends Fragment {


    public DescripcionReporte() {
        // Required empty public constructor
    }

    public  static  Reporte reporte;
    public  static Boolean ocultar;
    public static DescripcionReporte entrar (Reporte reporte_, boolean ocultar_)
    {
        ocultar = ocultar_;
        reporte = reporte_;
        return new DescripcionReporte ();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

View v = inflater.inflate(R.layout.fragment_descripcion_reporte, container, false);
        Log.e("Descripcion","2"+reporte.getRuta());


        ImageView imgFotoDescripcion = (ImageView)v.findViewById(R.id.imgFotoDescripcion);
        TextView  lblComentarioDescripcion = (TextView)v.findViewById(R.id.lblComentarioDescripcion);
        TextView lblId = (TextView)v.findViewById(R.id.lblId);


        TextView lblUsuario = (TextView)v.findViewById(R.id.lblUsuario);
        TextView lblEquipo = (TextView)v.findViewById(R.id.lblEquipo);
        TextView lblUnidad = (TextView)v.findViewById(R.id.lblUnidad);
        TextView lblFecha = (TextView)v.findViewById(R.id.lblFecha);

        lblUsuario.setText(reporte.getNombre_usuario());
                lblEquipo.setText(reporte.getEquipo());
        lblUnidad.setText(reporte.getNombre_unidad());
                lblFecha.setText(""+reporte.getFecha());



        lblComentarioDescripcion.setText(reporte.getComentario());
         Picasso.get().load(Constantes.SERVIDOR+reporte.getRuta()).placeholder(R.drawable.load).into(imgFotoDescripcion);
        lblId.setText(""+reporte.getId());


        Button btnLevantarAlerta = (Button)v.findViewById(R.id.btnLevantarAlerta);

if (ocultar)
    btnLevantarAlerta.setVisibility(View.GONE);
        if (reporte.getStatus().equals("1"))
            btnLevantarAlerta.setEnabled(false);


        btnLevantarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mostrarAlerta();

            }
        });

        return v;
    }



    Dialog dialogSubirFoto;
    private void mostrarAlerta() {
        dialogSubirFoto = new Dialog(getActivity(), android.R.style.Theme_Dialog);
        dialogSubirFoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSubirFoto.setContentView(R.layout.layout_levantar);
//sdfasdfsdfasdfasdfasdfdsa

        Button btnAceptar = (Button)dialogSubirFoto.findViewById(R.id.btnAceptar);
        final EditText txtComentarioLevantar =(EditText)dialogSubirFoto.findViewById(R.id.txtComentarioLevantar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog dialog = new SpotsDialog(getActivity());
                dialog.show();
                ApiService service = RetroClient.getApiService();
                Call<E_Response> call = service.levantarReporte(reporte.getId(), 1 , txtComentarioLevantar.getText().toString());
                call.enqueue(new Callback<E_Response>() {
                    @Override
                    public void onResponse(Call<E_Response> call, Response<E_Response> response) {
                        Log.e("Ok", "ok" + response.toString());
                        mostrarNotificacion();
                        dialog.dismiss();
                        dialogSubirFoto.dismiss();
                    }

                    @Override
                    public void onFailure(Call<E_Response> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("fail", "fail" + t.toString());
                    }
                });
            }
        });











        dialogSubirFoto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialogSubirFoto.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialogSubirFoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSubirFoto.show();

    }



    public void mostrarNotificacion()
    {
        Intent intent = new Intent(getActivity(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notification = new Notification.Builder(getActivity()).setSmallIcon(R.drawable.feliz)
                .setContentTitle("Alerta")
                .setContentText("Se Registro una alerta")
                .setAutoCancel(true)
                .setSound(uri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel("id","nombre",NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager!=null)
            {
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notification.setChannelId("id");
        }

        if (notificationManager!=null)
        {
            notificationManager.notify("id",0,notification.build());
        }

    }


}
