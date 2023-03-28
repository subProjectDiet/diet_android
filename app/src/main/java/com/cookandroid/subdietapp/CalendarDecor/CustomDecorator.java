package com.cookandroid.subdietapp.CalendarDecor;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.cookandroid.subdietapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CustomDecorator  implements DayViewDecorator {

    public final Drawable drawable;

    public CustomDecorator(Context context) {
        drawable = context.getResources().getDrawable(R.drawable.custom_border);
        //내가 만든 drawble을 적용
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        // 모든 날짜에 대해 데코레이션을 적용합니다.
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);

    }
}