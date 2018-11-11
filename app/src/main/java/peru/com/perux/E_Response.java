package peru.com.perux;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class E_Response {

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @SerializedName("error")
    @Expose
    String error;

    public String getSuccesfull() {
        return succesfull;
    }

    public void setSuccesfull(String succesfull) {
        this.succesfull = succesfull;
    }

    @SerializedName("succesfull")
    @Expose
    String succesfull;
}
