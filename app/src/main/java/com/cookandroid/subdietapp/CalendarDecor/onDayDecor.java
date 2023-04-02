package com.cookandroid.subdietapp.CalendarDecor;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.cookandroid.subdietapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class onDayDecor implements DayViewDecorator {
    private final CalendarDay date;
    private final Drawable drawable;

    public onDayDecor(Context context) {
        drawable =  context.getResources().getDrawable(R.drawable.pink_circle);
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }


}
