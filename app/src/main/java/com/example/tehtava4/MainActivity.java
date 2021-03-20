package com.example.tehtava4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
//import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Spinner theaters;

    String url = "https://www.finnkino.fi/xml/TheatreAreas/";

    ArrayList<Theater> tList;
    ArrayList<Show> sList;

    TextInputEditText date, startTime, endTime;
    String sDate, sStartTime, sEndTime;

    LinearLayout movie;

    Date startDate, endDate;

    Button applyChanges;

    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TheaterList theaterList = TheaterList.getInstance();
        theaterList.readXML(url);

        date = (TextInputEditText) findViewById(R.id.date);
        startTime = (TextInputEditText) findViewById(R.id.startTime);
        endTime = (TextInputEditText) findViewById(R.id.endTime);

        movie = findViewById(R.id.movie);

        Button applyChanges = findViewById(R.id.applyChanges);

        theaters = (Spinner) findViewById(R.id.theaters);
        ArrayAdapter AA = new ArrayAdapter(this, android.R.layout.simple_spinner_item, theaterList.getNameList());
        theaters.setAdapter(AA);

        theaters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                setShowList(theaterList, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected = 0;
                setShowList(theaterList, 0);
            }
        });

        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sDate = s.toString();
            }
        });

        startTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sStartTime = s.toString();
            }
        });

        endTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sEndTime = s.toString();
            }
        });

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowList(theaterList, selected);
            }
        });
    }

    private void setShowList(TheaterList theaterList, int position) {
        Date date = new Date();

        sList = theaterList.getTheaterList().get(position).getShows(sDate, sStartTime, sEndTime);

        movie.removeAllViewsInLayout();

        if (sList.size() == 0) {
            sList.add(new Show("No shows", date, date, "No shows in chosen theater in chosen time frame or invalid date/time choices"));
        }

        for (Show show : sList) {
            TextView textView = new TextView(this);
            String HL = show.getHeadline();
            textView.setText(HL);
            textView.setTextColor(0xFF000000);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textView.setLayoutParams(params);
            movie.addView(textView);
        }
    }
}