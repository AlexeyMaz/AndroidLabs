package com.example.androidlabs;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendar;
    private TextView dateView;
    private ImageView calendarIcon;
    private LinearLayout calendarContainer;
    private TextView selectedDateView;
    private Button btnDone;
    private View dimBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация компонентов
        calendar = findViewById(R.id.calendar);
        dateView = findViewById(R.id.date_view);
        calendarIcon = findViewById(R.id.calendar_open_icon);
        calendarContainer = findViewById(R.id.calendarContainer);
        selectedDateView = findViewById(R.id.selected_date_view);
        btnDone = findViewById(R.id.btnDone);
        dimBackground = findViewById(R.id.dimBackground); // Добавлено

        // Установка текущей даты в TextView
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        dateView.setText("Сегодня, " + currentDate);

        // Установка слушателя для изменения выбранной даты в календаре
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                selectedDateView.setText("Выбранная дата: " + selectedDate);
                if (!selectedDateView.getText().toString().isEmpty()) {
                    dateView.setText("Выбранная дата: " + selectedDateView.getText().toString().
                            replace("Выбранная дата: ", ""));
                }
            }
        });

        // Установка слушателя для отображения/скрытия календаря по нажатию на иконку
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarPopup();
            }
        });

        // Установка слушателя для кнопки "Готово"
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCalendarPopup();
                updateDateText(); // Обновление текста с учетом выбранной даты
            }
        });
    }

    // Метод для отображения календарного окна
    private void showCalendarPopup() {
        // Отображение темного фона (затемнение)
        dimBackground.setVisibility(View.VISIBLE);

        // Центрирование календарного окна
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) calendarContainer.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        calendarContainer.setLayoutParams(layoutParams);

        // Отображение календарного окна
        calendarContainer.setVisibility(View.VISIBLE);
    }

    // Метод для скрытия календарного окна
    private void hideCalendarPopup() {
        // Скрытие темного фона (затемнение)
        dimBackground.setVisibility(View.GONE);

        // Скрытие календарного окна
        calendarContainer.setVisibility(View.GONE);
    }

    // Метод для обработки нажатия на кнопку меню
    public void onMenuButtonClick(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    // Метод для обновления текста в dateView
    private void updateDateText() {
        if (!selectedDateView.getText().toString().isEmpty()) {
            dateView.setText("Выбранная дата: " + selectedDateView.getText().toString().replace("Выбранная дата: ", ""));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            dateView.setText("Сегодня, " + currentDate);
        }
    }
}
