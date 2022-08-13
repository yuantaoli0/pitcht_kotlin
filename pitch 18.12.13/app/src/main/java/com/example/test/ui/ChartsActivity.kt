package com.example.test.ui

import android.content.Context
import com.example.test.R
import com.example.test.viewmodel.EmptyVm
import com.rczs.gis.base.BaseActivity
import io.data2viz.charts.chart.Chart
import io.data2viz.charts.chart.chart
import io.data2viz.charts.chart.discrete
import io.data2viz.charts.chart.mark.area
import io.data2viz.charts.chart.quantitative
import io.data2viz.geom.Size
import io.data2viz.viz.VizContainerView
import java.util.*

class ChartsActivity : BaseActivity<EmptyVm>() {

    data class WeeklyData(val day: Long, val amount: Int)

    override fun setResLayout(): Int {
        return R.layout.activity_charts
    }

    override fun initView() {

    }

    override fun initData() {
        val data = getData()

        val chartView = MyChart(this, data)

        runOnUiThread {
            setContentView(chartView)
        }

    }

    fun getData(): List<WeeklyData> {
        val weeklyValues: MutableList<WeeklyData> = arrayListOf()
        var currCalendar = Calendar.getInstance()
        currCalendar.set(Calendar.HOUR, 0)
        currCalendar.set(Calendar.MINUTE, 0)
        currCalendar.set(Calendar.SECOND, 0)
        currCalendar.set(Calendar.MILLISECOND, 0)

        currCalendar.add(Calendar.DAY_OF_MONTH, -7)

        repeat(7) {
            val prevDay = currCalendar.clone() as Calendar
            currCalendar.add(Calendar.DAY_OF_MONTH, 1)
            weeklyValues.add(
                WeeklyData(
                    currCalendar.timeInMillis,
                    1
                )
            )
        }

        weeklyValues.sortBy { it.day }
        println("asdd")        
        return weeklyValues
    }
}


class MyChart(context: Context, weekData: List<ChartsActivity.WeeklyData>) : VizContainerView(context) {
    var vizSize = 500.0
    private val chart: Chart<ChartsActivity.WeeklyData> = chart(weekData) {
        size = Size(vizSize, vizSize)
        title = "Past week"

        val xAxis = discrete({ dayToString(domain.day) })

        val yAxis = quantitative({ domain.amount.toDouble() }) {
            name = "Number of occurrences"
        }

        area(xAxis, yAxis)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        chart.size = Size(vizSize, vizSize * h / w)
    }

    private fun dayToString(day: Long): String {
        val cal = Calendar.getInstance()
        cal.time = Date(day)
        return "${cal.get(Calendar.YEAR)}.${cal.get(Calendar.MONTH) + 1}.${cal.get(Calendar.DAY_OF_MONTH)}"
    }
}