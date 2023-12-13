package id.fishku.consumer.search

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import id.fishku.consumer.R

class FilterButtonView : AppCompatButton {

    private lateinit var clickedBackground: Drawable
    private lateinit var unclickedBackground: Drawable
    private var clickedTextColor: Int = 0
    private var unclickedTextColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        background = if (isActivated == true) clickedBackground else unclickedBackground
        setTextColor(if (isActivated == true) clickedTextColor else unclickedTextColor)

        setPadding(8, 0, 8, 0)
        width = 100
        textSize = 13f
        isAllCaps = false
        gravity = Gravity.CENTER
    }

    private fun init() {
        isActivated = false

        clickedBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_selected) as Drawable
        unclickedBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_unselected) as Drawable
        clickedTextColor = ContextCompat.getColor(context, R.color.white)
        unclickedTextColor = ContextCompat.getColor(context, R.color.blue)

        setOnClickListener {
            isActivated = !isActivated
            invalidate()
        }
    }

    fun resetState() {
        isActivated = false
        invalidate()
    }
}