package com.skycopyhot.aweather.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skycopyhot.aweather.R;
import com.skycopyhot.aweather.model.Weather;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yongqiang
 * 15/5/16
 */
public class WeatherFragment extends BaseFragment {

    private static final String REQUEST_KEY_WEATHER = "REQUEST_KEY_WEATHER";
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_temp_ave)
    TextView tvTempAve;
    @BindView(R.id.iv_weather)
    ImageView ivWeather;
    @BindView(R.id.tv_temp_max)
    TextView tvTempMax;
    @BindView(R.id.tv_temp_min)
    TextView tvTempMin;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private Weather mWeather;

    public static WeatherFragment newInstance(Weather weather) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(REQUEST_KEY_WEATHER, weather);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeather = getArguments().getParcelable(REQUEST_KEY_WEATHER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);

        tvCity.setText(mWeather.getCity());
        tvDate.setText(mWeather.getDate());
        tvDes.setText(mWeather.getDescription());
        tvTempAve.setText(mWeather.getTemp_ave());
        tvTempMax.setText(mWeather.getTemp_max());
        tvTempMin.setText(mWeather.getTemp_min());

        Picasso.with(getContext()).load(mWeather.getIcon()).into(ivWeather);

        return view;
    }
}
