package com.example.keepingscore.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.keepingscore.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private TextView textView;

    private TextView high_score;

    private Button button;

    private Button buttonCount;

    private CountDownTimer timer;

    private int time = 60;

    private int newTimesPressed = 0;

    private int timesPressed = 0;

    private int highscore = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        high_score = root.findViewById(R.id.high_score);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                time--;
            }

            @Override
            public void onFinish() {
                button.setEnabled(false);
                buttonCount.setEnabled(true);

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                int defaultValue = getResources().getInteger(R.integer.saved_times_pressed_default_key);
                timesPressed = sharedPref.getInt(getString(R.string.saved_button_press_count_key), defaultValue);

                if (timesPressed < newTimesPressed) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(getString(R.string.saved_button_press_count_key), newTimesPressed);
                    editor.commit();

                    timesPressed = newTimesPressed;

                }

                newTimesPressed = 0;

                high_score.setText("The high score is " + timesPressed + " clicks!");

            }
        };

        button = root.findViewById(R.id.mash_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newTimesPressed = newTimesPressed + 1;

                // be amazing, do something
                textView.setText("Button has been pressed " + Integer.toString(newTimesPressed)+ " times!");
            }
        });

        buttonCount = root.findViewById(R.id.time_button);
        buttonCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button.setEnabled(true);
                buttonCount.setEnabled(false);
                time = 60;
                timer.start();
            }
        });
        return root;
    }
}