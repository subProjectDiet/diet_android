package com.cookandroid.subdietapp.CalendarDecor;//package com.example.sibalapplication;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.text.style.ForegroundColorSpan;
//
//import com.prolificinteractive.materialcalendarview.CalendarDay;
//import com.prolificinteractive.materialcalendarview.DayViewDecorator;
//import com.prolificinteractive.materialcalendarview.DayViewFacade;
//
//import java.util.Calendar;
//import java.util.HashSet;
//
//public class ExerciseDecor implements DayViewDecorator {
//
//    public final Drawable drawable;
//
//
//
//    public ExerciseDecor(Context context){
//
//        drawable = context.getResources().getDrawable(R.drawable.custom_border);
//
//    }
//
//    @Override
//    public boolean shouldDecorate(CalendarDay day) {
////        day.copyTo(calendar);
////        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
//        return weekDay == Calendar.SUNDAY;
////    }
//
//    @Override
//    public void decorate(DayViewFacade view) {
//        view.addSpan(new ForegroundColorSpan(Color.RED));
//    }
//
////         view.setSelectionDrawable(drawable);
//
//}