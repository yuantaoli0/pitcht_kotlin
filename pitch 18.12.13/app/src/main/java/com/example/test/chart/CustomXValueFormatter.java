package com.example.test.chart;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

public class CustomXValueFormatter implements IAxisValueFormatter {
    private List<String> labels;
    public static final String TAG = CustomXValueFormatter.class.getName();
    /**
     * @param labels 要显示的标签字符数组
     */
    public CustomXValueFormatter(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Log.i(TAG,value+"");
        if(value-(int)value>0.2){
            return "";
        }
        return labels.get(((int) value));
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
