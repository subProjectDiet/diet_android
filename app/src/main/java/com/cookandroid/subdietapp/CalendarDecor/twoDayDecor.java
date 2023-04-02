package com.cookandroid.subdietapp.CalendarDecor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.cookandroid.subdietapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class twoDayDecor implements DayViewDecorator {
    private final CalendarDay date;
    private final Drawable drawable;

    public twoDayDecor(Context context,CalendarDay date) {
        drawable =  context.getResources().getDrawable(R.drawable.pink_circle);
        this.date = date;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        if (drawable != null) {
            view.setSelectionDrawable(drawable);
        }
        else {
            Log.d("event_decorator", "is null");
        };
    }


}
