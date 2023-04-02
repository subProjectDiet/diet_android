package com.cookandroid.subdietapp.CalendarDecor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class BoldDecor  implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    private final int currentMonth;

    public BoldDecor(Context context){
        currentMonth = Calendar.getInstance().get(Calendar.MONTH);

    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int dayOfWeek = day.getCalendar().get(Calendar.DAY_OF_WEEK);
        return dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY;

    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLACK));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));


    }
}
