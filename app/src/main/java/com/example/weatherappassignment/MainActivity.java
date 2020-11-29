package com.example.weatherappassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // we"ll make HTTP request to this URL to retrieve weather conditions


    // Textview to show temperature and description
    TextView temperature, descript, feature, humidity, wheather;
    ImageView weatherBackground;
    Spinner citychoice;
    ArrayAdapter<CharSequence> adapter;
    // JSON object that contains weather information
    //JSONObject jsonObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temperature = (TextView) findViewById(R.id.temperature);
        descript = (TextView) findViewById(R.id.description);
        feature = (TextView) findViewById(R.id.feature);
        humidity = (TextView) findViewById(R.id.humidity);
        wheather = (TextView) findViewById(R.id.wheather);
        citychoice = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.Cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citychoice.setAdapter(adapter);
        weatherBackground = (ImageView) findViewById(R.id.weatherbackground);
        citychoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city = parent.getItemAtPosition(position).toString();
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=872e44253e1f135d8fdb2c2176a76c42";
                weather(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void weather(String url){
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //after receiving information
                Log.d("Mohammad", "Response Recieved");
                Log.d("Mohammad-JSON",response.toString());
                try {
                    String town = response.getString("name");
                    Log.d("Mohammad-town", town);
                    //nested object
                    JSONObject jsonMain = response.getJSONObject("main");
                    JSONObject jsonWind = response.getJSONObject("wind");
                    JSONObject jsonSys = response.getJSONObject("sys");
                    JSONArray jsonWeatherArray = response.getJSONArray("weather");
                    Log.d("Mohammad-weather",jsonWeatherArray.toString());
                    String timerise = jsonSys.getString("sunrise");
                    String timeset = jsonSys.getString("sunset");
                    Date ssunrise = new Date(Long.parseLong(timerise) * 1000);
                    Date ssunset = new Date(Long.parseLong(timeset) * 1000);
                    Log.d("Mohammad-sunrise", String.valueOf(ssunrise));
                    Log.d("Mohammad-sunset", String.valueOf(ssunset));
                    double temp = jsonMain.getDouble("temp");
                    double speed = jsonWind.getDouble("speed");
                    Log.d("Mohammad-speed", String.valueOf(speed));
                    Log.d("Mohammad-temp", String.valueOf(temp));
                    double humid = jsonMain.getDouble("humidity");
                    Log.d("Mohammad-humidity", String.valueOf(humidity));
                    temperature.setText(String.valueOf(temp));
                    descript.setText(town);
                    humidity.setText(String.valueOf(humid));
                    feature.setText(String.valueOf(speed));
                    chooseBackground(jsonWeatherArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Recieve Error", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                // in case of error
                Log.e("Mohammad-Error", "Something went wrong with your URL");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObj);
    }

    public void chooseBackground(JSONArray jsonWeatherArray) {
        for (int i=0; i < jsonWeatherArray.length(); i++)
        {
            try {
                JSONObject oneObject = jsonWeatherArray.getJSONObject(i);
                // Pulling items from the array
                String weather = oneObject.getString("main");
                Log.d("Mohammad-Weather", weather);
                wheather.setText(weather);
                weatherBackground = (ImageView)findViewById(R.id.weatherbackground);
                if(weather.equals("Clouds")){
                    Glide.with(this)
                            .load("https://static.wixstatic.com/media/bf8ac8_456b9f3e267e46ccb13d145950aaae2d~mv2_d_7360_3226_s_4_2.jpg/v1/fill/w_640,h_438,al_c,q_80,usm_0.66_1.00_0.01/bf8ac8_456b9f3e267e46ccb13d145950aaae2d~mv2_d_7360_3226_s_4_2.webp")
                            .into(weatherBackground);
                }
                if(weather.equals("Clear")) {
                    Glide.with(this)
                            .load("https://image.freepik.com/free-photo/crystal-clear-sky-with-small-clouds_23-2148282521.jpg")
                            .into(weatherBackground);
                }
                if(weather.equals("Rain")) {
                    Glide.with(this)
                            .load("https://i1.wp.com/www.cleantechloops.com/wp-content/uploads/2019/02/artificial-rains-hadley-cells.jpg?ssl=1")
                            .into(weatherBackground);
                }
            } catch (JSONException e) {
                // Oops
            }
        }

    }
}