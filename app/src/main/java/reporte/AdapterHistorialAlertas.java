package reporte;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import com.squareup.picasso.Picasso;



import modelo.Reporte;
import peru.com.perux.R;
import utilerias.Constantes;


public class AdapterHistorialAlertas extends RecyclerView.Adapter<AdapterHistorialAlertas.ViewHolder> implements View.OnClickListener {


    ArrayList<Reporte> usuarios;
    Context context;
    View.OnClickListener listener;

    public AdapterHistorialAlertas(ArrayList<Reporte> arrayListUsuarios, Context applicationContext) {
        this.usuarios=arrayListUsuarios;
        context = applicationContext;
    }

    @NonNull
    @Override
    public AdapterHistorialAlertas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial,null,false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistorialAlertas.ViewHolder holder, int position) {

        if (usuarios.get(position).getEstado()==null)
        {
            holder.relativeHeader.setVisibility(View.GONE);
            holder.relativeData.setVisibility(View.VISIBLE);
            holder.lblCorreo.setText(usuarios.get(position).getComentario());
            holder.lblNombre.setText(""+usuarios.get(position).getId());

            holder.lblFecha.setText(""+usuarios.get(position).getFecha());
            holder.lblEquipo.setText(""+usuarios.get(position).getEquipo());

            holder.lblUsuario.setText(""+usuarios.get(position).getNombre_usuario());
            holder.lblUnidad.setText(""+usuarios.get(position).getNombre_unidad());


            if (usuarios.get(position).getStatus().equals("1"))
            {
                holder.lblEstatus.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            else
            {
                holder.lblEstatus.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            }


            holder.lblEstatus.setText(""+usuarios.get(position).getStatus());
        }
        else
        {
            holder.lblHeader.setText(usuarios.get(position).getEstado());

            if (usuarios.get(position).getEstado().equals("Reportado"))
            {
                holder.lblHeader.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            }
            else
            {
                holder.lblHeader.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }

            holder.relativeData.setVisibility(View.GONE);
            holder.relativeHeader.setVisibility(View.VISIBLE);
        }




        Log.e("RUTA",""+Constantes.SERVIDOR+usuarios.get(position).getRuta());
        Picasso.get().load(Constantes.SERVIDOR+usuarios.get(position).getRuta()).placeholder(R.drawable.load).resize(200,200).into(holder.imgFotoUsuario);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null)
        {
            listener.onClick(v);
        }

    }

    public  void setOnClick(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView lblNombre, lblCorreo, lblEquipo,lblFecha,lblUsuario,lblUnidad,lblEstatus,lblHeader;
        ImageView imgFotoUsuario;
        RelativeLayout relativeHeader,relativeData;

        public ViewHolder(View itemView) {
            super(itemView);
            lblNombre = (TextView)itemView.findViewById(R.id.lblNombre);
            lblCorreo = (TextView)itemView.findViewById(R.id.lblCorreo);
            lblFecha= (TextView)itemView.findViewById(R.id.lblFecha);
            lblEquipo= (TextView)itemView.findViewById(R.id.lblEquipo);
            imgFotoUsuario = (ImageView)itemView.findViewById(R.id.imgFotoUsuario);

            lblUsuario= (TextView)itemView.findViewById(R.id.lblUsuario);
            lblUnidad= (TextView)itemView.findViewById(R.id.lblUnidad);
            lblEstatus= (TextView)itemView.findViewById(R.id.lblEstatus);

            relativeHeader= (RelativeLayout)itemView.findViewById(R.id.relativeHeader);
                    relativeData= (RelativeLayout)itemView.findViewById(R.id.relativeData);
            lblHeader = (TextView)itemView.findViewById(R.id.lblHeader);
        }
    }
}
