package com.example.threedapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Cube3DView(this))
    }
}

class Cube3DView(context: Context) : View(context) {
    private val paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 6f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private var angleX = 0.0
    private var angleY = 0.0
    private var previousX = 0f
    private var previousY = 0f

    private val vertices = arrayOf(
        doubleArrayOf(-1.0, -1.0, -1.0), doubleArrayOf(1.0, -1.0, -1.0),
        doubleArrayOf(1.0, 1.0, -1.0), doubleArrayOf(-1.0, 1.0, -1.0),
        doubleArrayOf(-1.0, -1.0, 1.0), doubleArrayOf(1.0, -1.0, 1.0),
        doubleArrayOf(1.0, 1.0, 1.0), doubleArrayOf(-1.0, 1.0, 1.0)
    )

    private val edges = arrayOf(
        intArrayOf(0, 1), intArrayOf(1, 2), intArrayOf(2, 3), intArrayOf(3, 0),
        intArrayOf(4, 5), intArrayOf(5, 6), intArrayOf(6, 7), intArrayOf(7, 4),
        intArrayOf(0, 4), intArrayOf(1, 5), intArrayOf(2, 6), intArrayOf(3, 7)
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)

        val width = width.toDouble()
        val height = height.toDouble()
        val scale = width / 3.5

        val projected = Array(8) { DoubleArray(2) }

        for (i in vertices.indices) {
            val v = vertices[i]
            var y = v[1] * cos(angleX) - v[2] * sin(angleX)
            var z = v[1] * sin(angleX) + v[2] * cos(angleX)
            var x = v[0]

            val x2 = x * cos(angleY) + z * sin(angleY)
            z = -x * sin(angleY) + z * cos(angleY)
            x = x2

            val distance = 3.0
            val fov = scale / (z + distance)

            projected[i][0] = x * fov + width / 2
            projected[i][1] = y * fov + height / 2
        }

        for (edge in edges) {
            val start = projected[edge[0]]
            val end = projected[edge[1]]
            canvas.drawLine(start[0].toFloat(), start[1].toFloat(), end[0].toFloat(), end[1].toFloat(), paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        if (event.action == MotionEvent.ACTION_MOVE) {
            angleY += (x - previousX) * 0.01
            angleX -= (y - previousY) * 0.01
            invalidate()
        }
        previousX = x
        previousY = y
        return true
    }
}
