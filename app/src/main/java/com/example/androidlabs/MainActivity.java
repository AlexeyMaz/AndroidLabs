package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Обработка результата
                    Intent data = result.getData();
                    if (data != null) {
                        // Извлеките данные, которые вернула вторая активность
                        // Например, задача, описание, выполнена ли и дата
                    }
                }
            }
    );

    private CalendarView calendar;
    private TextView dateView;
    private ImageView calendarIcon;
    private LinearLayout calendarContainer;
    private TextView selectedDateView;
    private Button btnDone;
    private View dimBackground;
    private ImageView noTasksIcon;
    private String selectedDate;
    private TextView noTasksMessage;
    private static final int ADD_TASK_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация компонентов
        calendar = findViewById(R.id.calendar);
        dateView = findViewById(R.id.date_view);
        calendarIcon = findViewById(R.id.calendar_open_icon2);
        calendarContainer = findViewById(R.id.calendarContainer);
        selectedDateView = findViewById(R.id.selected_date_view);
        btnDone = findViewById(R.id.btnDone);
        dimBackground = findViewById(R.id.dimBackground);
        noTasksIcon = findViewById(R.id.no_tasks_icon);
        noTasksMessage = findViewById(R.id.no_tasks_message);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        selectedDate = sdf.format(new Date());
        selectedDateView.setText("Выбранная дата: " + selectedDate);

        // Установка текущей даты в TextView
        String currentDate = sdf.format(new Date());
        dateView.setText("Сегодня, " + currentDate);

        // Проверка наличия задач на текущую дату
        if (!hasTasksForDate(currentDate)) {
            noTasksIcon.setVisibility(View.VISIBLE);
            noTasksMessage.setVisibility(View.VISIBLE);
        } else {
            noTasksIcon.setVisibility(View.GONE);
            noTasksMessage.setVisibility(View.GONE);
        }

        // Установка слушателя для изменения выбранной даты в календаре
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                selectedDateView.setText("Выбранная дата: " + selectedDate);
            }
        });

        // Установка слушателя для отображения/скрытия календаря по нажатию на иконку
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noTasksIcon.setVisibility(View.GONE);
                noTasksMessage.setVisibility(View.GONE);
                showCalendarPopup();
            }
        });

        // Установка слушателя для кнопки "Готово"
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCalendarPopup();
                updateDateText();
                updateNoTaskItems();
            }
        });

        // Установка слушателя для закрытия календаря при нажатии вне его области
        dimBackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Пользователь нажал вне области календаря, закрываем его
                    hideCalendarPopup();
                    updateNoTaskItems();
                }
                return true;
            }
        });

        ImageView addTaskButton = findViewById(R.id.add_task_icon);
        addTaskButton.setOnClickListener(view -> startAddTaskActivity());
    }

    private void startAddTaskActivity() {
        Intent intent = new Intent(this, AddTaskActivity.class);
        addTaskLauncher.launch(intent);
    }

    // Метод для отображения календарного окна
    private void showCalendarPopup() {
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
        dimBackground.setVisibility(View.GONE);

        // Скрытие календарного окна
        calendarContainer.setVisibility(View.GONE);
    }

    // Метод для обновления текста в dateView
    private void updateDateText() {
        if (selectedDateView.getText().toString() != null) {
            dateView.setText("Выбранная дата: " + selectedDateView.getText().toString().
                    replace("Выбранная дата: ", ""));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            dateView.setText("Сегодня, " + currentDate);
        }
    }

    private void updateNoTaskItems() {
        // Получаем списки задач для выбранной даты
        List<Task> completedTasks = getCompletedTasks(selectedDate);
        List<Task> incompleteTasks = getIncompleteTasks(selectedDate);

        // Проверяем наличие задач
        if (completedTasks.isEmpty() && incompleteTasks.isEmpty()) {
            // Нет задач, отображаем картинку no_tasks_icon
            noTasksIcon.setVisibility(View.VISIBLE);
            noTasksMessage.setVisibility(View.VISIBLE);
        } else {
            // Есть хотя бы одна задача, отображаем списки
            noTasksIcon.setVisibility(View.GONE);
            noTasksMessage.setVisibility(View.GONE);

            // Отображаем списки задач
            // Допустим, есть методы для получения описаний задач
            List<String> completedTasksDescriptions = getTaskDescriptions(completedTasks);
            List<String> incompleteTasksDescriptions = getTaskDescriptions(incompleteTasks);

            // Далее отобразите списки ваших задач в соответствующих TextView или RecyclerView
            // в зависимости от ваших потребностей
        }
    }

    // Получение списка выполненных задач для выбранной даты
    private List<Task> getCompletedTasks(String selectedDate) {
        // Реализуйте логику получения выполненных задач для выбранной даты
        // возвращайте список объектов Task или используйте ваш тип данных
        return new ArrayList<>(); // пример: пока возвращает пустой список
    }

    // Получение списка невыполненных задач для выбранной даты
    private List<Task> getIncompleteTasks(String selectedDate) {
        // Реализуйте логику получения невыполненных задач для выбранной даты
        // возвращайте список объектов Task или используйте ваш тип данных
        return new ArrayList<>(); // пример: пока возвращает пустой список
    }

    // Получение описаний задач
    private List<String> getTaskDescriptions(List<Task> tasks) {
        // Реализуйте логику получения описаний задач
        // возвращайте список строк с описаниями задач
        return new ArrayList<>(); // пример: пока возвращает пустой список
    }

    // Метод для проверки наличия задач на указанную дату (подставьте свою логику)
    private boolean hasTasksForDate(String date) {
        // Здесь должна быть логика проверки наличия задач на указанную дату
        // Верните true, если задачи есть, и false, если их нет
        return false;  // Подставьте свою логику
    }

    public void onAddTaskClick(View view) {
        // Создание интента для запуска новой активности (AddTaskActivity)
        Intent intent = new Intent(this, AddTaskActivity.class);

        // Передача текущей выбранной даты в новую активность
        intent.putExtra("selectedDate", selectedDate);

        // Запуск новой активности
        startActivityForResult(intent, ADD_TASK_REQUEST);
    }

    // Метод для обработки нажатия на кнопку меню
    public void onMenuButtonClick(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Получение данных о задаче из AddTaskActivity
            String description = data.getStringExtra("description");
            boolean isDone = data.getBooleanExtra("isDone", false);
            String selectedDate = data.getStringExtra("selectedDate");

            // Теперь у вас есть данные о новой задаче, которые вы можете использовать
            // для создания соответствующего объекта задачи и обновления интерфейса.
        }
    }
}
