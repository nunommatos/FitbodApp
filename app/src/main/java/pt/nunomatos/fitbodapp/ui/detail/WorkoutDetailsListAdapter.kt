package pt.nunomatos.fitbodapp.ui.detail

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import pt.nunomatos.fitbodapp.R
import pt.nunomatos.fitbodapp.data.model.RepMaxWeightModel
import pt.nunomatos.fitbodapp.utils.inflateLayoutOnParent

class WorkoutDetailsListAdapter(
    private val repMaxGraphList: List<RepMaxWeightModel>,
    private val repMaxList: List<RepMaxWeightModel>,
    private val historyRepMax: RepMaxWeightModel?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_GRAPH = 0
        private const val VIEW_TYPE_TITLE_HEADER = 1
        private const val VIEW_TYPE_HISTORY = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_GRAPH
            1 -> VIEW_TYPE_TITLE_HEADER
            else -> VIEW_TYPE_HISTORY
        }
    }

    override fun getItemCount(): Int {
        // we add 2 to include the graph and the history list header
        return repMaxList.size + 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GRAPH -> {
                WorkoutDetailsGraphViewHolder(
                    parent.inflateLayoutOnParent(
                        R.layout.item_workout_details_graph
                    )
                )
            }
            VIEW_TYPE_TITLE_HEADER -> {
                WorkoutDetailsTitleHeaderViewHolder(
                    parent.inflateLayoutOnParent(
                        R.layout.item_workout_details_title_header
                    )
                )
            }
            else -> {
                WorkoutDetailsHistoryViewHolder(
                    parent.inflateLayoutOnParent(
                        R.layout.item_workout_details_history
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? WorkoutDetailsGraphViewHolder)?.bind()
        (holder as? WorkoutDetailsHistoryViewHolder)?.bind(repMaxList[position - 2])
    }

    inner class WorkoutDetailsGraphViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val lineChart = itemView as LineChart

        private fun populateGraph() {
            val typedValue = TypedValue()
            val a = itemView.context.obtainStyledAttributes(
                typedValue.data,
                intArrayOf(R.attr.colorPrimary)
            )
            val colorPrimary = a.getColor(0, 0)
            a.recycle()

            val values = ArrayList<Entry>()
            for (i in 1 until repMaxGraphList.size) {
                values.add(Entry(i.toFloat(), repMaxGraphList[i - 1].maxWeight.toFloat()))
            }

            val dataSet = LineDataSet(values, "").apply {
                setDrawCircles(true)
                setDrawValues(false)
                color = colorPrimary
                setCircleColor(colorPrimary)
                lineWidth = 2f
                circleRadius = 5f
                setDrawCircleHole(false)
                setDrawFilled(false)
            }

            lineChart.apply {
                description.isEnabled = false
                axisRight.isEnabled = false
                legend.isEnabled = false
                isHighlightPerTapEnabled = false
                isDoubleTapToZoomEnabled = false
                extraBottomOffset = 20f
                setTouchEnabled(false)
                setPinchZoom(false)
                data = LineData(
                    arrayListOf<ILineDataSet>().apply {
                        add(dataSet)
                    }
                )
            }
        }

        fun bind() {
            lineChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                axisMinimum = 0f
                axisMaximum = repMaxGraphList.size.toFloat()
                labelRotationAngle = 335f
                textColor = ContextCompat.getColor(itemView.context, R.color.lightGray)
                valueFormatter = XAxisFormatter(repMaxGraphList.map { it.date })
                setCenterAxisLabels(true)
                isGranularityEnabled = true
            }

            lineChart.axisLeft.apply {
                disableGridDashedLine()
                axisMinimum = repMaxGraphList.minOf { it.maxWeight }.toFloat() * 0.75f
                axisMaximum = repMaxGraphList.maxOf { it.maxWeight }.toFloat() * 1.5f
                setDrawZeroLine(false)
                setDrawLimitLinesBehindData(false)
                textColor = ContextCompat.getColor(itemView.context, R.color.lightGray)
            }

            populateGraph()
        }

    }

    inner class WorkoutDetailsTitleHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class WorkoutDetailsHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val bestRepMaxIcon = itemView.findViewById<ImageView>(R.id.bestRepMaxIcon)
        private val historyDate = itemView.findViewById<TextView>(R.id.historyDate)
        private val repMaxValue = itemView.findViewById<TextView>(R.id.repMaxValue)

        fun bind(repMax: RepMaxWeightModel) {
            bestRepMaxIcon.isVisible = repMax == historyRepMax
            repMaxValue.text = String.format("%.2f", repMax.maxWeight)
            historyDate.text = repMax.date
        }
    }
}