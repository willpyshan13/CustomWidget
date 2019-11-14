package com.will.myapplication

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Desc:上下滚动文字
 * <p>
 * Date: 2019/11/13
 * Copyright: Copyright (c) 2018-2019
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * @Author: pengyushan
 */
class ScrollTextWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textRect = Rect()

    private val animDuration: Long

    private val textSize: Float
    private val textColor: Int
    private val borderColor: Int
    private var textList: Array<String>? = null

    var oldNumber = 0
    var number = 0
    val borderWidth = 0F
    private var offset = 0f
    private var mMaxWidth: Int = 0
    private val rectF = RectF()
    private var drawText = true
    private var fillContent = false

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProductControlWidget)
        textColor = typedArray.getColor(
            R.styleable.ProductControlWidget_textColor,
            Color.parseColor("#000000")
        )
        borderColor = typedArray.getColor(
            R.styleable.ProductControlWidget_borderColor,
            Color.parseColor("#000000")
        )
        fillContent = typedArray.getBoolean(R.styleable.ProductControlWidget_fillContent, false);
        animDuration =
            typedArray.getInt(R.styleable.ProductControlWidget_animDuration, 300).toLong()
        textSize = typedArray.getDimension(R.styleable.ProductControlWidget_textSize, 30f)
        val i = typedArray.getResourceId(
            R.styleable.ProductControlWidget_textRes,
            0
        )
        if (i > 0) {
            textList = resources.getStringArray(i)
        }

        drawText = !textList.isNullOrEmpty()
        typedArray.recycle()
        initPaint()
    }

    private fun initPaint() {
        paint.isAntiAlias = true
        if (fillContent) {
            paint.style = Paint.Style.FILL
        } else {
            paint.style = Paint.Style.STROKE
        }
        paint.color = borderColor

        textPaint.color = textColor
        textPaint.textSize = textSize
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mMaxWidth = (MeasureSpec.getSize(widthMeasureSpec))
        val width = measureWidth(widthMeasureSpec)
        setMeasuredDimension(
            measureWidth(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
        rectF.set(
            borderWidth / 2,
            borderWidth / 2,
            width.toFloat() - borderWidth / 2,
            MeasureSpec.getSize(heightMeasureSpec).toFloat() - borderWidth / 2
        )
    }

    private fun measureWidth(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        var defaultWidth = specSize

        when (specMode) {
            MeasureSpec.AT_MOST -> {
                defaultWidth = mMaxWidth
            }
            MeasureSpec.EXACTLY -> {
                defaultWidth = specSize
            }
            MeasureSpec.UNSPECIFIED -> {
                defaultWidth = mMaxWidth
            }
        }
        return defaultWidth
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            if (drawText) {
                drawScrollText(it)
            } else if (number > 0) {
                drawBg(it)
                drawScrollNumText(it)
            }
        }
    }

    /**
     * Desc:圆形
     * <p>
     * Author: pengyushan
     * Date: 2019-11-14
     * @param canvas Canvas?
     */
    private fun drawBg(canvas: Canvas) {
        canvas.save()
        canvas.drawCircle(width / 2.toFloat(), height / 2.toFloat(), width / 2.toFloat(), paint)
        canvas.restore()
    }

    /**
     * Desc:绘制滚动数字
     * <p>
     * Author: zhuanghongzhan
     * Date: 2019-11-11
     * @param canvas Canvas
     */
    private fun drawScrollNumText(canvas: Canvas) {
        canvas.save()
        val textWidth = width / 2F
        if (number > oldNumber) {
            textPaint.getTextBounds(number.toString(), 0, number.toString().length, textRect)
            canvas.drawText(
                number.toString(),
                textWidth,
                offset + (height + textRect.height()) / 2,
                textPaint
            )

            textPaint.getTextBounds(oldNumber.toString(), 0, oldNumber.toString().length, textRect)
            canvas.drawText(
                oldNumber.toString(),
                textWidth,
                offset + (height + textRect.height()) / 2 - height,
                textPaint
            )
        } else {
            textPaint.getTextBounds(number.toString(), 0, number.toString().length, textRect)
            canvas.drawText(
                number.toString(),
                textWidth,
                offset + (height + textRect.height()) / 2 - height,
                textPaint
            )

            textPaint.getTextBounds(oldNumber.toString(), 0, oldNumber.toString().length, textRect)
            canvas.drawText(
                oldNumber.toString(),
                textWidth,
                offset + (height + textRect.height()) / 2,
                textPaint
            )
        }
        canvas.restore()
    }

    /**
     * Desc:绘制滚动文字
     * <p>
     * Author: zhuanghongzhan
     * Date: 2019-11-11
     * @param canvas Canvas
     */
    private fun drawScrollText(canvas: Canvas) {
        canvas.save()
        val textWidth = width / 2F
        textList?.let {
            if (number > oldNumber) {
                textPaint.getTextBounds(textList!![number], 0, textList!![number].length, textRect)
                canvas.drawText(
                    textList!![number],
                    textWidth,
                    offset + (height + textRect.height()) / 2,
                    textPaint
                )

                textPaint.getTextBounds(
                    textList!![oldNumber],
                    0,
                    textList!![oldNumber].length,
                    textRect
                )
                canvas.drawText(
                    textList!![oldNumber],
                    textWidth,
                    offset + (height + textRect.height()) / 2 - height,
                    textPaint
                )
            } else {
                textPaint.getTextBounds(textList!![number], 0, textList!![number].length, textRect)
                canvas.drawText(
                    textList!![number],
                    textWidth,
                    offset + (height + textRect.height()) / 2 - height,
                    textPaint
                )

                textPaint.getTextBounds(
                    textList!![oldNumber],
                    0,
                    textList!![oldNumber].length,
                    textRect
                )
                canvas.drawText(
                    textList!![oldNumber],
                    textWidth,
                    offset + (height + textRect.height()) / 2,
                    textPaint
                )
            }
        }
        canvas.restore()
    }

    fun showNext() {
        oldNumber = number
        if (drawText) {
            number = 1
        } else {
            number++
        }
        startScrollAnim()
        postInvalidate()
    }

    fun showPre() {
        oldNumber = number
        if (drawText) {
            number = 0
        } else {
            number--
        }
        startScrollAnim()
        postInvalidate()
    }

    /**
     * Desc:数字滚动动画
     * <p>
     * Author: pengyushan
     * Date: 2019-11-13
     */
    private fun startScrollAnim() {
        val scrollAnimator = ValueAnimator.ofFloat(1f, 0f)
        scrollAnimator.duration = animDuration
        scrollAnimator.addUpdateListener {
            offset = if (number >= oldNumber) {
                it.animatedValue as Float * height
            } else {
                (1 - it.animatedValue as Float) * height
            }
            postInvalidate()
        }
        scrollAnimator.start()
    }


}