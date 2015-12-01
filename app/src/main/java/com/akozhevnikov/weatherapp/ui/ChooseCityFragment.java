package com.akozhevnikov.weatherapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akozhevnikov.weatherapp.R;
import com.akozhevnikov.weatherapp.network.NetworkUtils;

public class ChooseCityFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_city, container, false);

        final EditText citySearchTextView
                = (EditText) view.findViewById(R.id.city_search_text_view);

        Button getWeatherButton = (Button) view.findViewById(R.id.get_weather_button);
        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = citySearchTextView.getText().toString();
                if (!TextUtils.isEmpty(city)) {
                    city = city.toLowerCase();
                    city = Character.toString(city.charAt(0)).toUpperCase() + city.substring(1);

                    Bundle bundle = new Bundle();
                    bundle.putString(NetworkUtils.CITY_KEY, city);

                    FragmentManager manager = ChooseCityFragment.this.getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    CityWeatherFragment cityWeatherFragment = new CityWeatherFragment();
                    cityWeatherFragment.setArguments(bundle);

                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    transaction.replace(R.id.content_frame, cityWeatherFragment);
                    transaction.commit();


                    Context context = ChooseCityFragment.this.getActivity();
                    if (!NetworkUtils.isNetworkAvailable(context)) {
                        Toast.makeText(context, getString(R.string.check_network),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return view;
    }
}
