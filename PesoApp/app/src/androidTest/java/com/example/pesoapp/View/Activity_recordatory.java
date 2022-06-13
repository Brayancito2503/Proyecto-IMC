package com.example.pesoapp.View;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.WorkManager;

import com.example.pesoapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class RecordatoryActivity extends AppCompatActivity{


    Button selefecha, selehora;
    TextView tvFecha, tvHora;
    Button guardar, btn_eliminar;

    Calendar actual= Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();

    private int minutos, horas, dia, mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatory);
        selefecha = findViewById(R.id.btn_selefecha);
        selehora = findViewById(R.id.btn_selehora);
        tvFecha = findViewById(R.id.tv_fecha);
        tvHora = findViewById(R.id.tv_hora);
        guardar = findViewById(R.id.btn_guardar);
        btn_eliminar = findViewById(R.id.btn_eliminar);

        selefecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                anio=actual.get(Calendar.YEAR);

                mes=actual.get(Calendar.MONTH);

                dia=actual.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                        calendar.set(calendar.DAY_OF_MONTH, d);
                        calendar.set(calendar.MONTH, m);
                        calendar.set(calendar.YEAR, y);

                        SimpleDateFormat format= new SimpleDateFormat("dd//mm//yyyy");
                        String strDate= format.format(calendar.getTime());
                        tvFecha.setText(strDate);
                    }
                },anio, mes, dia);
                datePickerDialog.show();

            }
        });

        selehora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                horas=actual.get(Calendar.HOUR_OF_DAY);
                minutos=actual.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {

                        calendar.set(Calendar.HOUR_OF_DAY, h);
                        calendar.set(Calendar.HOUR_OF_DAY, m);

                        tvHora.setText(String.format("%02d:%02d", h, m));

                    }
                },horas, minutos, true);
                timePickerDialog.show();

            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tag= generateKey();
                Long alerttime= calendar.getTimeInMillis() - System.currentTimeMillis();
                int random = (int )(Math.random()* 50 + 1);
                Data data= guardarData("Peso IMC", "Es hora de revisar algo que hace esta app!", random);
                Workermanagernoti.guardarnoti(alerttime, data, "tag1");




                Toast.makeText(RecordatoryActivity.this, "Alarma Guardada", Toast.LENGTH_SHORT).show();
            }
        });
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EliminarNoti("tag1");
            }
        });

    }//fin del mundo

    private void EliminarNoti(String tag){
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Toast.makeText(RecordatoryActivity.this, "Alarma Eliminada", Toast.LENGTH_SHORT).show();
    }

    private String generateKey(){
        return UUID.randomUUID().toString();
    }

    private Data guardarData(String titulo, String detalle, int id_noti){
        return new Data.Builder()
                .putString("titulo", titulo)
                .putString("detalle", detalle)
                .putInt("Id_noti", id_noti).build();
    }
}