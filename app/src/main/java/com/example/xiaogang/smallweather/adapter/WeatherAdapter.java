package com.example.xiaogang.smallweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiaogang.smallweather.R;
import com.example.xiaogang.smallweather.model.Weather;
import com.example.xiaogang.smallweather.util.Util;

/**
 * Created by xiaogang on 16/11/13.
 */

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mcontext;
    private Weather mWeather;

    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;
    private static final int TYPE_THREE = 2;
    private static final int TYPE_FORE = 3;

    public WeatherAdapter(Context mcontext, Weather mWeather) {
        this.mcontext = mcontext;
        this.mWeather = mWeather;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == WeatherAdapter.TYPE_ONE) {
            return WeatherAdapter.TYPE_ONE;
        }
        if (position == WeatherAdapter.TYPE_TWO) {
            return WeatherAdapter.TYPE_TWO;
        }
        if (position == WeatherAdapter.TYPE_THREE) {
            return WeatherAdapter.TYPE_THREE;
        }
        if (position == WeatherAdapter.TYPE_FORE) {
            return WeatherAdapter.TYPE_FORE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ONE:
                return new NowWeatherViewHolder(
                    LayoutInflater.from(mcontext).inflate(R.layout.item_nowweather, parent, false));
            case TYPE_THREE:
                return new SuggestionViewHolder(
                        LayoutInflater.from(mcontext).inflate(R.layout.item_suggestion, parent, false));
            case TYPE_TWO:
                return new DailyForecastViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.item_dailyforecast,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NowWeatherViewHolder){
            ((NowWeatherViewHolder) holder).weathertxt.setText(mWeather.now.cond.txt);
            Glide.with(mcontext).load("http://files.heweather.com/cond_icon/"+mWeather.now.cond.code+".png").into(((NowWeatherViewHolder) holder).weathericon);
            ((NowWeatherViewHolder) holder).tempFlu.setText(String.format("%s℃",mWeather.now.tmp));
            ((NowWeatherViewHolder) holder).tempMax.setText(String.format(
                    mWeather.dailyForecast.get(0).tmp.max + " / " +mWeather.dailyForecast.get(0).tmp.min+"℃"));

//          ((NowWeatherViewHolder) holder).tempPm.setText(String.format("PM2.5: %s μg/m³", mWeather.aqi.city.pm25));
//          ((NowWeatherViewHolder) holder).tempQuality.setText(String.format("空气质量： %s", mWeather.aqi.city.qlty));
            //有的地点返回的数据中没有这两项
        }else if (holder instanceof SuggestionViewHolder){
            ((SuggestionViewHolder)holder).clothBrief.setText(String.format("穿衣建议---%s", mWeather.suggestion.drsg.brf));
            ((SuggestionViewHolder)holder).clothTxt.setText(mWeather.suggestion.drsg.txt);
            ((SuggestionViewHolder)holder).sportBrief.setText(String.format("运动指数---%s", mWeather.suggestion.sport.brf));
            ((SuggestionViewHolder)holder).sportTxt.setText(mWeather.suggestion.sport.txt);
            ((SuggestionViewHolder)holder).travelBrief.setText(String.format("旅游建议---%s", mWeather.suggestion.trav.brf));
            ((SuggestionViewHolder)holder).travelTxt.setText(mWeather.suggestion.trav.txt);
            ((SuggestionViewHolder)holder).fluBrief.setText(String.format("感冒指数---%s", mWeather.suggestion.flu.brf));
            ((SuggestionViewHolder)holder).fluTxt.setText(mWeather.suggestion.flu.txt);
        }else if (holder instanceof DailyForecastViewHolder){
            ((DailyForecastViewHolder) holder).forecastDate[0].setText("今天");
            for (int i = 0; i < mWeather.dailyForecast.size(); i++) {
                if (i > 0) {
                    try {
                        ((DailyForecastViewHolder) holder).forecastDate[i].setText(
                                Util.dayForWeek(mWeather.dailyForecast.get(i).date));
                    } catch (Exception e) {

                    }
                }
                Glide.with(mcontext).load("http://files.heweather.com/cond_icon/"+mWeather.dailyForecast.get(i).cond.codeD+".png").
                        into(((DailyForecastViewHolder) holder).forecastIcon[i]);
                ((DailyForecastViewHolder) holder).forecastTemp[i].setText(
                        String.format("%s / %s℃",
                                mWeather.dailyForecast.get(i).tmp.min,
                                mWeather.dailyForecast.get(i).tmp.max));
                ((DailyForecastViewHolder) holder).forecastTxt[i].setText(
                        String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
                                mWeather.dailyForecast.get(i).cond.txtD,
                                mWeather.dailyForecast.get(i).wind.sc,
                                mWeather.dailyForecast.get(i).wind.dir,
                                mWeather.dailyForecast.get(i).wind.spd,
                                mWeather.dailyForecast.get(i).pop));
            }
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }

   public class NowWeatherViewHolder extends RecyclerView.ViewHolder{
        ImageView weathericon;
        TextView weathertxt;
        TextView tempFlu;
        TextView tempMax;

        public NowWeatherViewHolder(View itemView) {
            super(itemView);
            weathericon = (ImageView) itemView.findViewById(R.id.weather_icon);
            weathertxt = (TextView) itemView.findViewById(R.id.weather_txt);
            tempFlu = (TextView) itemView.findViewById(R.id.temp_flu);
            tempMax = (TextView) itemView.findViewById(R.id.temp_max);
        }
    }
   public class SuggestionViewHolder extends RecyclerView.ViewHolder{
       TextView clothBrief;
       TextView clothTxt;
       TextView sportBrief;
       TextView sportTxt;
       TextView travelBrief;
       TextView travelTxt;
       TextView fluBrief;
       TextView fluTxt;

       public SuggestionViewHolder(View itemView) {
           super(itemView);
           clothBrief = (TextView) itemView.findViewById(R.id.cloth_brief);
           clothTxt = (TextView) itemView.findViewById(R.id.cloth_txt);
           sportBrief = (TextView) itemView.findViewById(R.id.sport_brief);
           sportTxt = (TextView) itemView.findViewById(R.id.sport_txt);
           travelBrief = (TextView) itemView.findViewById(R.id.travel_brief);
           travelTxt = (TextView) itemView.findViewById(R.id.travel_txt);
           fluBrief = (TextView) itemView.findViewById(R.id.flu_brief);
           fluTxt = (TextView) itemView.findViewById(R.id.flu_txt);
       }
   }
   public class DailyForecastViewHolder extends RecyclerView.ViewHolder{
       private LinearLayout dailyforecastLinear;
       private TextView[] forecastDate = new TextView[mWeather.dailyForecast.size()];
       private TextView[] forecastTemp = new TextView[mWeather.dailyForecast.size()];
       private TextView[] forecastTxt = new TextView[mWeather.dailyForecast.size()];
       private ImageView[] forecastIcon = new ImageView[mWeather.dailyForecast.size()];
       public DailyForecastViewHolder(View itemView) {
           super(itemView);
           dailyforecastLinear = (LinearLayout) itemView.findViewById(R.id.dailyforecast_linear);
           for (int i = 0; i < mWeather.dailyForecast.size(); i++) {
               View view = View.inflate(mcontext, R.layout.item_dailyforeline, null);
               forecastDate[i] = (TextView) view.findViewById(R.id.forecast_date);
               forecastTemp[i] = (TextView) view.findViewById(R.id.forecast_temp);
               forecastTxt[i] = (TextView) view.findViewById(R.id.forecast_txt);
               forecastIcon[i] = (ImageView) view.findViewById(R.id.forecast_icon);
               dailyforecastLinear.addView(view);
           }
       }
   }

}
