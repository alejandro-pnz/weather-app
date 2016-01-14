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
import android.widget.EditText;

import com.akozhevnikov.weatherapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.akozhevnikov.weatherapp.network.NetworkUtils.CITY_KEY;

public class ChooseCityFragment extends Fragment {
	@Bind(R.id.city_search_text_view)
	EditText citySearchTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_choose_city, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@SuppressWarnings("unused")
	@OnClick(R.id.get_weather_button)
	void searchCity() {
		String city = citySearchTextView.getText().toString();
		if (!TextUtils.isEmpty(city)) {
			city = city.toLowerCase();
			city = Character.toString(city.charAt(0)).toUpperCase() + city.substring(1);

			Bundle bundle = new Bundle();
			bundle.putString(CITY_KEY, city);

			FragmentManager manager = ChooseCityFragment.this.getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();

			CityWeatherFragment cityWeatherFragment = new CityWeatherFragment();
			cityWeatherFragment.setArguments(bundle);

			manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

			transaction.replace(R.id.content_frame, cityWeatherFragment);
			transaction.commit();

			Context context = ChooseCityFragment.this.getActivity();
		}
	}
}
