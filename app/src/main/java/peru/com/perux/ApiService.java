package peru.com.perux;

import java.util.List;
import java.util.Map;

import modelo.Reporte;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService {


    @Multipart
    @POST("index.php")
    Call<E_Response> uploadFileWithPartMap(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);



    @POST("obtener_reportes.php")
    Call<List<Reporte>> get_Messages();


    @FormUrlEncoded
    @POST("levantar_alerta.php")
    Call<E_Response> levantarReporte(
            @Field("id_reporte") int id_reporte,
            @Field("id_usuario") int id_usuario,
               @Field("comentario_levantamiento") String comentario_levantamiento
    );


    @FormUrlEncoded
    @POST("obtener_reporte_unidades.php")
    Call<List<Reporte>> obtener_unidades(
            @Field("id_unidad") int id_unidad
    );



    @FormUrlEncoded
    @POST("insertar_usuario.php")
    Call<List<Reporte>> insertar_usuario(
            @Field("nombres") String nombres
    );


    @FormUrlEncoded
    @POST("obtener_equipos.php")
    Call<List<Reporte>> buscarEquipo(
            @Field("nombreEquipo") String nombreEquipo);
}
