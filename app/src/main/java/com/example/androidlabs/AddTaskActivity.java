package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTaskActivity extends Activity {

    private EditText descriptionEditText;
    private CheckBox doneCheckBox;
    private Button setDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Инициализация компонентов
        descriptionEditText = findViewById(R.id.descriptionEditText);
        doneCheckBox = findViewById(R.id.doneCheckBox);
        setDateButton = findViewById(R.id.setDateButton);

        // Получение выбранной даты из предыдущей активности
        Intent intent = getIntent();
        if (intent != null) {
            String selectedDate = intent.getStringExtra("selectedDate");

        }

        // Обработка кнопки установки даты
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button saveTaskButton = findViewById(R.id.saveTaskButton);
        saveTaskButton.setOnClickListener(view -> onSaveTaskClick());
    }

    // Обработка кнопки сохранения задачи
    private void onSaveTaskClick() {
        // Получение данных о задаче
        String description = descriptionEditText.getText().toString();
        boolean isDone = doneCheckBox.isChecked();

        // Получение текущей даты
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String selectedDate = sdf.format(new Date());

        // Передача данных о задаче обратно в MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("description", description);
        resultIntent.putExtra("isDone", isDone);
        resultIntent.putExtra("selectedDate", selectedDate);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
