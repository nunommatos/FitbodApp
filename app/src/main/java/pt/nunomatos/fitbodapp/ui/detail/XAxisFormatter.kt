package pt.nunomatos.fitbodapp.ui.detail

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import pt.nunomatos.fitbodapp.utils.convertToMMMDDYYYFormat
import kotlin.math.roundToInt

class XAxisFormatter(private val arrayOfDates: List<String>) : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {

        val position = value.roundToInt()

        return if (position >= 0 && position < arrayOfDates.size && position % 2 == 0) {
            arrayOfDates[position].convertToMMMDDYYYFormat()
        } else {
            ""
        }
    }
}