package com.xzw.hotelcalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.addapp.hotelcalendar.CalendarPickerView;

import static com.xzw.hotelcalendar.Common.FORMATTER_MM_DD;

public class CalendarActivity extends AppCompatActivity {

    private CalendarPickerView calendarView;
    private TextView tvCancel;

    private Context mContext;

    private List<Date> selectDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mContext = this;

        calendarView = findViewById(R.id.calendar_view);
        tvCancel = findViewById(R.id.tv_cancel);

        initCalendar(new Date());
    }

    private void initCalendar(Date currentDate) {
        Utils.init(this);
        Calendar calendarMax = Calendar.getInstance();
        final Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTime(currentDate);
        calendarCurrent.set(Calendar.HOUR, 0);//小时设置为0
        calendarCurrent.set(Calendar.MINUTE, 0);//分钟设置为0
        calendarCurrent.set(Calendar.SECOND, 0);//秒设置为0

        calendarMax.setTime(currentDate);
        calendarMax.set(Calendar.HOUR, 0);//小时设置为0
        calendarMax.set(Calendar.MINUTE, 0);//分钟设置为0
        calendarMax.set(Calendar.SECOND, 0);//秒设置为0
        calendarMax.add(Calendar.DAY_OF_MONTH, 180);//可选180天
        //        calendarMax.add(Calendar.MONTH, 6);//可选6个月
        //        lastYear.add(Calendar.MONTH, -1);

        List<Date> selectedDates = new ArrayList<>();
        if (selectDates.size() >= 2) {
            selectedDates.add(selectDates.get(0));
            selectedDates.add(selectDates.get(selectDates.size() - 1));
        }
        calendarView.init(calendarCurrent.getTime(), calendarMax.getTime(), Common.FORMATTER_yyyy_mm) //
                .inMode(CalendarPickerView.SelectionMode.RANGE) //
                //.withSelectedDate(new Date())//选中一天
                //.withDeactivateDates(list)////表格第三列不可选择
                .withSelectedDates(selectedDates);//选中范围

        calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSelected(Date date) {
                List<Date> selDates = calendarView.getSelectedDates();
                selectDates = calendarView.getSelectedDates();
                if (selDates.size() > 1) {
//                    tvCalendar.setText(FORMATTER_MM_DD.format(selDates.get(0)) + " 到 " +
//                            FORMATTER_MM_DD.format(selDates.get(selDates.size() - 1)));
                    String startDate = FORMATTER_MM_DD.format(selDates.get(0));
                    String endDate = FORMATTER_MM_DD.format(selDates.get(selDates.size() - 1));
                    Intent intent = new Intent();
                    intent.setClass(mContext, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("startDate", startDate);
                    bundle.putString("endDate", endDate);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }
}
