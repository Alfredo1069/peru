package reporte;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import com.squareup.picasso.Picasso;



import modelo.Reporte;
import peru.com.perux.R;
import utilerias.Constantes;


public class AdapterReportes extends RecyclerView.Adapter<AdapterReportes.ViewHolder> implements View.OnClickListener {


    ArrayList<Reporte> usuarios;
    Context context;
    View.OnClickListener listener;

    public AdapterReportes(ArrayList<Reporte> arrayListUsuarios, Context applicationContext) {
        this.usuarios=arrayListUsuarios;
        context = applicationContext;
    }

    @NonNull
    @Override
    public AdapterReportes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte,null,false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReportes.ViewHolder holder, int position) {
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
        TextView lblNombre, lblCorreo, lblEquipo,lblFecha,lblUsuario,lblUnidad,lblEstatus;
        ImageView imgFotoUsuario;

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
        }
    }
}
