package com.udacity

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

@Suppress("DEPRECATION")
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val textRect = Rect()
    private var textWidthSize = 0
    private var widthSize = 0
    private var heightSize = 0
    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val buttonColor = resources.getColor(R.color.cyan)
    val textColor = resources.getColor(R.color.white)
    val downloadText = context.getString(R.string.download_text)
    val loadingText = context.getString(R.string.btn_loading)
    val loadingBtnColor = resources.getColor(R.color.animatedBtn)
    private lateinit var btnText: String
    private var progressValue = 0f
    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
    }

    val valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {

        duration = 2000

        addUpdateListener {
            progressValue = this.animatedValue as Float
            invalidate()
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                this@LoadingButton.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                buttonState = ButtonState.Completed
                invalidate()
                this@LoadingButton.isEnabled = true
            }
        })
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
        canvas!!.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), paint)

        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 70.0f
        paint.color = textColor

        btnText = if (buttonState == ButtonState.Loading) loadingText
        else downloadText

        // drawing the text first time before animating
        textWidthSize = paint.measureText(btnText).toInt()
        paint.color = textColor
        canvas.drawText(
            btnText,
            ((widthSize - paint.textSize) / 2),
            ((height + 33) / 2).toFloat(),
            paint
        )

        // drawing all animating things
        if (buttonState == ButtonState.Loading) {
            // draw the animated button
            paint.color = loadingBtnColor
            canvas.drawRect(0.0f, 0.0f, width * progressValue, height.toFloat(), paint)

            // drawing the loading text
            textWidthSize = paint.measureText(btnText).toInt()

            paint.color = textColor
            canvas.drawText(
                btnText,
                ((widthSize - paint.textSize) / 2),
                ((height + 33) / 2).toFloat(),
                paint
            )

            // locate the circle to draw it behind the text
            paint.getTextBounds(btnText , 0 , btnText.length , textRect)
            val textRadius = textRect.height().toFloat()

            canvas.translate(
                (widthSize + textWidthSize + textRadius) /2f ,
                heightSize /2f - textRadius / 2
            )

            // draw the animated circle
            paint.color = resources.getColor(R.color.colorAccent)
            canvas.drawArc(
                0f,
                0f,
                textRadius,
                textRadius,
                0f,
                360f * progressValue,
                true,
                paint
            )
        }


    }

    // using this method to perform this block when user click
    override fun performClick(): Boolean {
        super.performClick()
        if (buttonState == ButtonState.Completed) {
            buttonState = ButtonState.Loading
            valueAnimator.start()
        }
        invalidate()
        return true
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