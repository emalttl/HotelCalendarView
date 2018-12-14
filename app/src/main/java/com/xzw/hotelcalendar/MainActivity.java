package com.xzw.hotelcalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.addapp.hotelcalendar.CalendarPickerView;

import static com.xzw.hotelcalendar.Common.FORMATTER_MM_DD;

public class MainActivity extends AppCompatActivity {

    private List<Date> selectDates = new ArrayList<>();
    private TextView tvCalendar;
    private CalendarPickerView calendarView;

    private Context mContext;
    private TextView tvCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        tvCalendarView = findViewById(R.id.tv_calendar_view);
        tvCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CalendarActivity.class));
            }
        });


        tvCalendar = findViewById(R.id.tv_calendar);
        tvCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCalendar(new Date());
            }
        });
    }

    private void initCalendar(Date currentDate) {
        Utils.init(this);
        final CommonDialog dialog = new CommonDialog(this, R.layout.cal_dialog_calendar);
        dialog.setDialogHeight(ScreenUtils.getScreenHeight() / 4 * 3);
        dialog.setAnimation(R.style.AnimBottom);
        dialog.setGravity(Gravity.BOTTOM);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight() / 4 * 3);
//        dialog.rootView.layoutParams = params;
        dialog.getRootView().setLayoutParams(params);
        Calendar calendarMax = Calendar.getInstance();
        final Calendar calendarCurrent = Calendar.getInstance();
//        calendarCurrent.time = currentDate;
        calendarCurrent.setTime(currentDate);
        calendarCurrent.set(Calendar.HOUR, 0);//小时设置为0
        calendarCurrent.set(Calendar.MINUTE, 0);//分钟设置为0
        calendarCurrent.set(Calendar.SECOND, 0);//秒设置为0

//        calendarMax.time = currentDate;
        calendarMax.setTime(currentDate);
        calendarMax.set(Calendar.HOUR, 0);//小时设置为0
        calendarMax.set(Calendar.MINUTE, 0);//分钟设置为0
        calendarMax.set(Calendar.SECOND, 0);//秒设置为0
        calendarMax.add(Calendar.DAY_OF_MONTH, 180);//可选180天
        //        calendarMax.add(Calendar.MONTH, 6);//可选6个月
        //        lastYear.add(Calendar.MONTH, -1);

//        val  calendarView = dialog.rootView.findViewById(R.id.calendar_view) as CalendarPickerView
        calendarView = dialog.getRootView().findViewById(R.id.calendar_view);
        //        ArrayList<Integer> list = new ArrayList<>();
        //        list.add(3);
        //        list.add(4);
        ////        calendar.deactivateDates(list);//表格第三列不可选择
        //        ArrayList<Date> arrayList = new ArrayList<>();
        //        try {
        //            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        //            String strdate = "22-2-2018";
        //            String strdate2 = "26-2-2018";
        //            Date newdate = dateformat.parse(strdate);
        //            Date newdate2 = dateformat.parse(strdate2);
        //            arrayList.add(newdate);
        //            arrayList.add(newdate2);
        //        } catch (ParseException e) {
        //            e.printStackTrace();
        //        }

//        val  tvCancel =   dialog.rootView.findViewById(R.id.tv_cancel) as   TextView
        TextView tvCancel = dialog.getRootView().findViewById(R.id.tv_cancel);
//        val selectedDates = ArrayList<Date>()//上次选择的时间范围
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

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//        calendarView.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
//            @SuppressLint("SetTextI18n")
//            override fun onDateSelected(date: Date) {
//                val selDates = calendarView.getSelectedDates()
//                selectDates = calendarView.getSelectedDates() as ArrayList<Date>
//                if (selDates.size > 1) {
//                    new Handler().postDelayed({ dialog.dismiss() }, 600)
//                    tv_select_date.text = FORMATTER_MM_DD.format(selDates.get(0)) + " 到 " +
//                            FORMATTER_MM_DD.format(selDates[selDates.size - 1])
//                }
//            }
//            override fun onDateUnselected(date: Date) {}
//        });
        calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSelected(Date date) {
                List<Date> selDates = calendarView.getSelectedDates();
                selectDates = calendarView.getSelectedDates();
                if (selDates.size() > 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 600);
                    tvCalendar.setText(FORMATTER_MM_DD.format(selDates.get(0)) + " 到 " +
                            FORMATTER_MM_DD.format(selDates.get(selDates.size() - 1)));
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        if (startDate != null && endDate != null)
            tvCalendarView.setText(startDate + " 到 " + endDate);
    }
}
