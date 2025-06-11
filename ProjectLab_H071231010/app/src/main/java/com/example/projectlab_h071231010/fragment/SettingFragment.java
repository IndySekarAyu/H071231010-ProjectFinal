package com.example.projectlab_h071231010.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.projectlab_h071231010.R;

public class SettingFragment extends Fragment {
    private static final String PREFS_NAME = "theme_pref";
    private static final String KEY_DARK_MODE = "is_dark_theme";

    private SharedPreferences sharedPreferences;
    private Switch swTema;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences =
                requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        swTema = view.findViewById(R.id.switchTheme);
        boolean isDarkTheme = sharedPreferences.getBoolean(KEY_DARK_MODE, false);
        swTema.setChecked(isDarkTheme);

        swTema.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit()
                    .putBoolean(KEY_DARK_MODE, isChecked)
                    .commit();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );

            // recreate host activity
            requireActivity().recreate();
        });
    }
}
