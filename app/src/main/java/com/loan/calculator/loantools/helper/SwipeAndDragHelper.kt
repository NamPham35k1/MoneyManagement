package com.loan.calculator.loantools.helper

import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.loan.calculator.loantools.R

class SwipeAndDragHelper(
    private val onMoveAction: (Int, Int) -> Unit,
    private val onSwipeAction: (Int) -> Unit,
    private val onDragFinished: () -> Unit
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.LEFT
) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        onDragFinished()
    }

    //    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
//        if (viewHolder.bindingAdapterPosition == 0) return makeMovementFlags(0, 0)
//        return super.getMovementFlags(recyclerView, viewHolder)
//    }
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPos = viewHolder.bindingAdapterPosition
        val toPos = target.bindingAdapterPosition
        if (toPos == 0) return false
        onMoveAction(fromPos, toPos)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipeAction(viewHolder.bindingAdapterPosition)
    }

    // KHÔNG sử dụng Float.MAX_VALUE ở đây nếu muốn onSwiped chạy
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue
    }

    // Giảm ngưỡng xuống để dễ kích hoạt onSwiped
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.5f
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val context = recyclerView.context

        val maxWidth = context.resources.getDimension(com.intuit.sdp.R.dimen._80sdp)

        val limitedDX = if (dX < -maxWidth) -maxWidth else dX

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = ContextCompat.getColor(context, R.color.red)
            }

            val cornerRadius = context.resources.getDimension(com.intuit.sdp.R.dimen._10sdp)
            val marginTopBottom = 0
            val marginRight = context.resources.getDimension(com.intuit.sdp.R.dimen._10sdp)
            val marginLeft = context.resources.getDimension(com.intuit.sdp.R.dimen._4sdp)

            val background = RectF(
                itemView.right.toFloat() + limitedDX + marginLeft,
                itemView.top.toFloat() + marginTopBottom,
                itemView.right.toFloat() - marginRight,
                itemView.bottom.toFloat() - marginTopBottom
            )
            c.drawRoundRect(background, cornerRadius, cornerRadius, paint)

            val icon = ContextCompat.getDrawable(context, R.drawable.ic_trash)
            icon?.let {
                val iconSize =
                    context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._20sdp)
                val iconTop = background.top + (background.height() - iconSize) / 2
                val iconLeft = background.left + (background.width() - iconSize) / 2
                it.setBounds(
                    iconLeft.toInt(),
                    iconTop.toInt(),
                    (iconLeft + iconSize).toInt(),
                    (iconTop + iconSize).toInt()
                )
                it.setTint(Color.WHITE)
                it.draw(c)
            }
        }
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            limitedDX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}