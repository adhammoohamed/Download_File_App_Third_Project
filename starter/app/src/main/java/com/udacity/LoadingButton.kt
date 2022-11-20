package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

@Suppress("DEPRECATION")
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val buttonColor = resources.getColor(R.color.cyan)
    val textColor = resources.getColor(R.color.white)
    val buttonText = context.getString(R.string.download_text)
    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }


    init {
        // make the view clickable
        isClickable = true
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL
        paint.color = buttonColor
        // drawing the view
        canvas!!.drawRect(0.0f , 0.0f , width.toFloat() , height.toFloat() , paint)

        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 70.0f
        paint.color = textColor
        paint.typeface = Typeface.create("" , Typeface.BOLD)
        // drawing the text
        canvas.drawText(buttonText , (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}