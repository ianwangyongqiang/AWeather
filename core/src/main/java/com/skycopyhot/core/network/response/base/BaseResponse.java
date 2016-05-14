package com.skycopyhot.core.network.response.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yongqiang
 * 14/5/16
 */
public class BaseResponse {

    @SerializedName("cod")
    @Expose
    private Integer cod;

    /**
     * @return The cod
     */
    public Integer getCod() {
        return cod;
    }

    /**
     * @param cod The cod
     */
    public void setCod(Integer cod) {
        this.cod = cod;
    }
}
