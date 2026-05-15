package com.example.miaplicacion

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.Locale

class LineChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintLinea = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#0055CC")
        strokeWidth = 4f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val paintPunto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#0055CC")
        style = Paint.Style.FILL
    }

    private val paintTexto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#3A3A3C")
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }

    private val paintEje = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#C6C6C8")
        strokeWidth = 2f
    }

    var puntos: List<Pair<String, Float>> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    var colorLinea: Int = Color.parseColor("#0055CC")
        set(value) {
            field = value
            paintLinea.color = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (puntos.isEmpty()) {
            paintTexto.color = Color.parseColor("#636366")
            paintTexto.textSize = 40f
            canvas.drawText("Sin datos", width / 2f, height / 2f, paintTexto)
            return
        }

        val paddingLeft = 80f
        val paddingRight = 40f
        val paddingTop = 40f
        val paddingBottom = 80f
        val chartW = width - paddingLeft - paddingRight
        val chartH = height - paddingTop - paddingBottom

        val valores = puntos.map { it.second }
        val min = (valores.minOrNull() ?: 0f) * 0.9f
        val max = (valores.maxOrNull() ?: 100f) * 1.1f
        val rango = (max - min).coerceAtLeast(1f)

        val stepX = if (puntos.size > 1) chartW / (puntos.size - 1).toFloat() else chartW

        val path = Path()
        val puntosDibujo = puntos.mapIndexed { i, p ->
            val x = paddingLeft + i * stepX
            val y = paddingTop + chartH - ((p.second - min) / rango * chartH)
            Pair(x, y)
        }

        puntosDibujo.forEachIndexed { i, (x, y) ->
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            canvas.drawCircle(x, y, 8f, paintPunto)
        }
        canvas.drawPath(path, paintLinea)

        val stepY = chartH / 4f
        for (i in 0..4) {
            val y = paddingTop + i * stepY
            val valor = max - (i * rango / 4f)
            canvas.drawLine(paddingLeft, y, width - paddingRight, y, paintEje)
            paintTexto.textSize = 28f
            canvas.drawText(String.format(Locale.US, "%.0f", valor), paddingLeft - 10f, y + 10f, paintTexto)
        }

        puntos.forEachIndexed { i, (label, _) ->
            paintTexto.textSize = 24f
            val x = paddingLeft + i * stepX
            canvas.drawText(label.takeLast(5), x, height - 10f, paintTexto)
        }
    }
}
