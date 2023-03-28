package com.cookandroid.subdietapp.CalendarDecor;

import android.content.Context;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class SatDecor  implements DayViewDecorator {
    //달력 참조
    private final Calendar calendar = Calendar.getInstance();

    public SatDecor(Context context){

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
//        day.copyTo(calendar);
//        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
//        return weekDay == Calendar.SATURDAY;

        //이번달토요일에만 색칠
        int weekDay = day.getCalendar().get(Calendar.DAY_OF_WEEK);
        int month = day.getMonth();
        return weekDay == Calendar.SATURDAY && month == Calendar.getInstance().get(Calendar.MONTH);


    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }
}
