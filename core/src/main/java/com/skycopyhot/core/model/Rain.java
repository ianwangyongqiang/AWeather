package com.skycopyhot.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by yongqiang
 * 14/5/16
 */
public class Rain {

    @SerializedName("3h")
    @Expose
    private Integer _3h;

    /**
     *
     * @return
     * The _3h
     */
    public Integer get3h() {
        return _3h;
    }

    /**
     *
     * @param _3h
     * The 3h
     */
    public void set3h(Integer _3h) {
        this._3h = _3h;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}