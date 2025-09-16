package com.example.travelapp2.presentation.map.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.travelapp2.R


fun createCustomMarkerDrawable(context: Context, index: Int): Drawable {
    val size = 70
    val padding = 20
    val bitmapWithCircle = Bitmap.createBitmap(size + padding * 2, size + padding * 2, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmapWithCircle)
    canvas.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

    val selectedColor = ContextCompat.getColor(context, R.color.main)

    val paint = Paint().apply {
        isAntiAlias = true
    }

    val radius = size / 2f
    val center = radius + padding

    paint.style = Paint.Style.FILL
    paint.color = android.graphics.Color.WHITE
    canvas.drawCircle(center, center, radius, paint)

    paint.style = Paint.Style.STROKE
    paint.color = selectedColor
    paint.strokeWidth = 6f
    canvas.drawCircle(center, center, radius - paint.strokeWidth / 2, paint)

    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    paint.style = Paint.Style.FILL
    paint.color = selectedColor
    paint.textSize = 30f
    paint.textAlign = Paint.Align.CENTER

    val text = index.toString()
    val textBounds = Rect()
    paint.getTextBounds(text, 0, text.length, textBounds)

    canvas.drawText(text, center, center + textBounds.height() / 2f, paint)

    return BitmapDrawable(context.resources, bitmapWithCircle)
}
