package com.devdelhi.weather.UI;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devdelhi.weather.MyLocation;
import com.devdelhi.weather.R;
import com.devdelhi.weather.weather.Current;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "DEEJAY";
    private Typeface black,bold,regular;
    private Current mCurrent = new Current();
    private RelativeLayout mainRelativeLayout;
    private LinearLayout progressBar;

    @BindView(R.id.timeLabel)
    TextView timeLabel;
    @BindView(R.id.loadingText)
    TextView loadingText;
    @BindView(R.id.locationLabel)
    TextView locationLabel;
    @BindView(R.id.degreeTextView)
    TextView degreeTextView;
    @BindView(R.id.humidityValue)
    TextView HumidityValue;
    @BindView(R.id.humidityLabel)
    TextView HumidityLabel;
    @BindView(R.id.precipitationLabel)
    TextView PrecipitationLabel;
    @BindView(R.id.precipitationValue)
    TextView PrecipitationValue;
    @BindView(R.id.summaryTextView)
    TextView SummaryTextView;
    @BindView(R.id.iconImageView)
    ImageView IconImageView;

    Double mLatitude;
    Double mLongitude;
    String mUserLocationName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainRelativeLayout = (RelativeLayout) findViewById(R.id.theLayout);
        progressBar = (LinearLayout) findViewById(R.id.progressBar2);

        black = Typeface.createFromAsset(getAssets(), "fonts/black.ttf");
        bold = Typeface.createFromAsset(getAssets(), "fonts/bold.ttf");
        regular = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");

        degreeTextView.setTypeface(black);
        locationLabel.setTypeface(bold);
        loadingText.setTypeface(bold);
        HumidityValue.setTypeface(bold);
        PrecipitationValue.setTypeface(bold);
        SummaryTextView.setTypeface(bold);
        timeLabel.setTypeface(regular);
        HumidityLabel.setTypeface(regular);
        PrecipitationLabel.setTypeface(regular);
        locationLabel.setText(mUserLocationName);

        mainRelativeLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        checkRunTimePermissions();
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            loadingText.setText("Sorry, But We Require Your Location Information To Get The Appropriate Weather Data For You. Please Grant The Permissions & Try Again.");
            Log.d(TAG, "Permissions Not Granted, Quitting!");
            return;
        }
        else {
            if (!isNetworkAvailable()) {
                alertUserAboutError();
                loadingText.setText("Please Check Your Internet Connectivity And Try Again");
            }
            else {
                checkRunTimePermissions();

                //Code Below To Get Users Location Co-ordinates and Location Name

                if (mLatitude == null || mLongitude == null || mUserLocationName == null) {
                    Log.d(TAG, "Dont have the data");
                    MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                        @Override
                        public void gotLocation(Location location){
                            //Got the location!
                            mLatitude = location.getLatitude();
                            mLongitude = location.getLongitude();

                            Log.d(TAG, "Got The Location " + mLatitude + " " + mLongitude);
                            try {
                                mUserLocationName = getLocationName(mLatitude, mLongitude);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    MyLocation myLocation = new MyLocation();
                    myLocation.getLocation(this, locationResult);
                }

                IconImageView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        getForecast(mLatitude, mLongitude);
                    }
                });
            }
        }


    }

    private String getLocationName(final double latitude, final double longitude) throws IOException {
        String tempLocation;
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
        if (addresses.size() > 0) {
            tempLocation = addresses.get(0).getLocality();
            Log.d(TAG, "Location Name " + tempLocation);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    getForecast(latitude,longitude);;
                }
            });

        }
        else {
            Log.d(TAG, "Couldn't Get User Location ");
            tempLocation = "Your Location";
        }
        return tempLocation;
    }

    private void getForecast(double latitude, double longitude) {
        Log.d(TAG, "Getting Forecast For " + latitude + " " + longitude);

        String apiKey = "fbda269f4d7041f1b148c53734096b16";
        String forecastURL = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude;
        Log.d(TAG, "Forecast URL " + forecastURL);
        if( isNetworkAvailable()){
            ToggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    alertUserAboutError();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToggleRefresh();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrent = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                        }
                        else
                            alertUserAboutError();
                    } catch (IOException e) {
                        Log.v(TAG, "IO Exception Caught : " + e);
                    } catch (JSONException e) {
                        Log.v(TAG, " This JSON Exception Caught : " + e);
                    }
                }
            });
        }
        else{
            Toast.makeText(this, R.string.NetworkNotAvailable, Toast.LENGTH_LONG).show();
            alertUserAboutError();
        }
    }

    private void ToggleRefresh() {
        if (progressBar.getVisibility() == View.INVISIBLE){
            progressBar.setVisibility(View.VISIBLE);
            mainRelativeLayout.setVisibility(View.INVISIBLE);
        }
        else if (progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.INVISIBLE);
            mainRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        mainRelativeLayout.setVisibility(View.VISIBLE);
        degreeTextView.setText(mCurrent.getmTemperature() + "");
        timeLabel.setText("Last Updated At " + mCurrent.getFormattedTime());
        HumidityValue.setText(mCurrent.getmHumidity()+"");
        PrecipitationValue.setText(mCurrent.getmPrecipitation() + "%");
        SummaryTextView.setText(mCurrent.getmSummary());
        locationLabel.setText(mUserLocationName);

        Drawable drawable = getResources().getDrawable(mCurrent.getIconId());

        IconImageView.setImageDrawable(drawable);

    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.d(TAG, " DJ FROM JSON TIMEZONE : " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();

        current.setmHumidity(currently.getDouble("humidity"));
        current.setmTime(currently.getLong("time"));
        current.setmIcon(currently.getString("icon"));
        current.setmPrecipitation(currently.getDouble("precipProbability"));
        current.setmSummary(currently.getString("summary"));
        current.setmTemperature(currently.getDouble("temperature"));
        current.setmTimeZone(timezone);

        return current;
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (( networkInfo != null ) && (networkInfo.isConnected()))
            isAvailable = true;
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "Error Dialog");
    }

    private boolean checkRunTimePermissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
            return  true;
        }
        return false; // We don't need to ask for Permissions
    }

}