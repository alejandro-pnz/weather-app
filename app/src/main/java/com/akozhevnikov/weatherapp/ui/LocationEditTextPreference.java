package com.akozhevnikov.weatherapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.akozhevnikov.weatherapp.R;

/**
 * Created by alejandro on 14.01.16.
 */
public class LocationEditTextPreference extends EditTextPreference {
	private final int minLength;

	public LocationEditTextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.LocationEditTextPreference,
				0, 0);

		try {
			minLength = a.getInteger(R.styleable.LocationEditTextPreference_minLength, 0);
			Log.d(LocationEditTextPreference.class.getSimpleName(), String.valueOf(minLength));
		} finally {
			a.recycle();
		}
	}

	@Override
	protected void showDialog(Bundle state) {
		super.onClick();

		EditText et = getEditText();
		et.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				Dialog d = getDialog();
				if( d instanceof AlertDialog){
					AlertDialog alertDialog = (AlertDialog)d;
					Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
					if(s.length() < minLength){
						positiveButton.setEnabled(false);
					} else {
						positiveButton.setEnabled(true);
					}
				}
			}
		});
	}
}