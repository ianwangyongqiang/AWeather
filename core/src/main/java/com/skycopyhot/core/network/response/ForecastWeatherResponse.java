package com.skycopyhot.core.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.skycopyhot.core.model.City;
import com.skycopyhot.core.model.Forecast;
import com.skycopyhot.core.network.response.base.BaseResponse;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yongqiang
 * 14/5/16
 */
public class ForecastWeatherResponse extends BaseResponse {

    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("message")
    @Expose
    private Double message;
    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private List<Forecast> list = new ArrayList<>();

    /**
     *
     * @return
     * The city
     */
    public City getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The city
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     *
     * @return
     * The message
     */
    public Double getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(Double message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The cnt
     */
    public Integer getCnt() {
        return cnt;
    }

    /**
     *
     * @param cnt
     * The cnt
     */
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    /**
     *
     * @return
     * The list
     */
    public List<Forecast> getList() {
        return list;
    }

    /**
     *
     * @param list
     * The list
     */
    public void setList(List<Forecast> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
