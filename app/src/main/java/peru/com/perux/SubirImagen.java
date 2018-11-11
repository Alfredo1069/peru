package peru.com.perux;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubirImagen extends Fragment {

    MagicalCamera magicalCamera;
    MagicalPermissions magicalPermissions;
    ImageView imgFoto;
    EditText txtComentario,txtNoEquipo;
    File   ruta;

    public SubirImagen() {
        // Required empty public constructor
    }

    public static SubirImagen entrar ()
    {
        return new SubirImagen ();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
View v = inflater.inflate(R.layout.fragment_subir_imagen, container, false);

        String [] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };


        magicalPermissions = new MagicalPermissions(this,permissions);
        magicalCamera = new MagicalCamera(getActivity(),100,magicalPermissions);
        imgFoto = (ImageView)v.findViewById(R.id.imgFoto) ;
        txtComentario = (EditText)v.findViewById(R.id.txtComentario);
        txtNoEquipo= (EditText)v.findViewById(R.id.txtNoEquipo);

        v.findViewById(R.id.btnTomarFoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicalCamera.takeFragmentPhoto(SubirImagen.this);
            }
        });

        v.findViewById(R.id.btnGaleria).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                magicalCamera.selectFragmentPicture(SubirImagen.this, "Selecciona Imagen");
            }
        });

        v.findViewById(R.id.btnSubirImagen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ruta!=null && txtNoEquipo.getText().toString().length()>0)
                {
                    subir();
                }
                else
                {
                    Toast.makeText(getActivity(),"Revisa la información",Toast.LENGTH_SHORT).show();
                }


            }
        });



        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("RequestCode",""+requestCode);

      /*  if (requestCode==100)
        {
            try {
                Log.e("DATA",""+data.getData().toString());
                //ruta = data.getData();
                Uri uri = data.getData();
                ruta  = new File(uri.getPath());
                Log.e("DATA2",""+ruta.toString());
                imgFoto.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {*/
            magicalCamera.resultPhoto(requestCode, resultCode, data);
            imgFoto.setImageBitmap(magicalCamera.getPhoto());
            String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),"myPhotoName","myDirectoryName", MagicalCamera.JPEG, true);



       Bitmap     imagen = magicalCamera.getPhoto();
            try {
                ContextWrapper cw = new ContextWrapper(getActivity());
                File dir = cw.getDir("Folder", Context.MODE_PRIVATE);
                ruta = new File(dir,getDateTime()+".jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(ruta);
                imagen.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                fileOutputStream.flush();
                Log.e("Ruta",""+ruta.toString());


            }
            catch (Exception e)
            {

            }

            if(path != null){
                Toast.makeText(getActivity(), "The photo is save in device, please check this path: " + path, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Sorry your photo dont write in devide, please contact with fabian7593@gmail and say this error", Toast.LENGTH_SHORT).show();
            }
      //  }





    }

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public void subir()
    {
        if (ruta!=null) {
            final AlertDialog dialog = new SpotsDialog(getActivity());
            dialog.show();
            ApiService service = RetroClient.getApiService();

            Log.e("La ruta es ", "" + ruta.toString());

            MultipartBody.Part body = prepareFilePart("fotoUp0", ruta);

            RequestBody description = createPartFromString(txtComentario.getText().toString());
            RequestBody noEquipo = createPartFromString(txtNoEquipo.getText().toString());
            RequestBody idUsuario = createPartFromString(String.valueOf(12345678));
            RequestBody unidad = createPartFromString(String.valueOf(1));
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("comentario", description);
            map.put("noEquipo", noEquipo);
            map.put("idUsuario", idUsuario);
            map.put("unidad", unidad);

            Call<E_Response> call = service.uploadFileWithPartMap(map, body);


            call.enqueue(new Callback<E_Response>() {
                @Override
                public void onResponse(Call<E_Response> call, Response<E_Response> response) {
                    Log.e("Ok", "ok" + response.toString());
                    dialog.dismiss();

                    Picasso.get().load("hhttp:").into(imgFoto);
                    txtComentario.setText("");
                    txtNoEquipo.setText("");
                    mostrarAlerta();
                }

                @Override
                public void onFailure(Call<E_Response> call, Throwable t) {

                    dialog.dismiss();
                    Log.e("fail", "fail" + t.toString());


                }
            });

        }
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, File fileUri) {
        File file = fileUri;
        RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);


    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    public String getDateTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return  dateFormat.format(date);

    }




    Dialog dialogSubirFoto;
    private void mostrarAlerta() {
        dialogSubirFoto = new Dialog(getActivity(), android.R.style.Theme_Dialog);
        dialogSubirFoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSubirFoto.setContentView(R.layout.layout_alerta);


        Button btnAceptar = (Button)dialogSubirFoto.findViewById(R.id.btnAceptar);


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSubirFoto.dismiss();
            }
        });





        dialogSubirFoto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialogSubirFoto.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialogSubirFoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSubirFoto.show();

    }

}
