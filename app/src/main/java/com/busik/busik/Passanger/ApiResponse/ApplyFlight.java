package com.busik.busik.Passanger.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ApplyFlight {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private String error;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    @Override
    public String toString() {
        return "ApplyFlight{" +
                "status=" + status +
                ", error='" + error + '\'' +
                '}';
    }
}