package com.cookandroid.subdietapp.CalendarDecor;

import android.content.Context;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class SundayDecor implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();

    public SundayDecor(Context context){

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
//        day.copyTo(calendar);
//        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
//        return weekDay == Calendar.SUNDAY;

        int weekDay = day.getCalendar().get(Calendar.DAY_OF_WEEK);
        int month = day.getMonth();
        return weekDay == Calendar.SUNDAY && month == Calendar.getInstance().get(Calendar.MONTH);

    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));
    }
}
