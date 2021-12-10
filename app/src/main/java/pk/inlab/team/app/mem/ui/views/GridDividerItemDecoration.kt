package pk.inlab.team.app.mem.ui.views

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Adds interior dividers to a RecyclerView with a GridLayoutManager.
 *
 * Sole constructor. Takes in Drawable objects to be used as horizontal and vertical dividers.
 * Params:
 * horizontalDivider – A divider Drawable to be drawn on the rows of the grid of the RecyclerView
 * verticalDivider – A divider Drawable to be drawn on the columns of the grid of the RecyclerView
 * numColumns – The number of columns in the grid of the RecyclerView
 */
class GridDividerItemDecoration (
    private val mHorizontalDivider: Drawable?,
    private val mVerticalDivider: Drawable?,
    private val mNumColumns: Int?,
): RecyclerView.ItemDecoration() {

    /**
     * Draws horizontal and/or vertical dividers onto the parent RecyclerView.
     * Params:
     * canvas – The Canvas onto which dividers will be drawn
     * parent – The RecyclerView onto which dividers are being added
     * state – The current RecyclerView.State of the RecyclerView
     */
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        drawHorizontalDividers(canvas, parent)
        drawVerticalDividers(canvas, parent)
    }

    /**
     * Adds vertical dividers to a RecyclerView with a GridLayoutManager or its subclass.
     * Params:
     * canvas – The Canvas onto which dividers will be drawn
     * parent – The RecyclerView onto which dividers are being added
     */
    private fun drawVerticalDividers(canvas: Canvas, parent: RecyclerView) {
        val parentLeft = parent.paddingLeft
        val parentRight = parent.width - parent.paddingRight

        val childCount = parent.childCount
        val numChildrenOnLastRow = childCount % mNumColumns!!
        var numRows = childCount / mNumColumns
        if (numChildrenOnLastRow == 0) {
            numRows--
        }
        for (i in 0 until numRows) {
            val child = parent.getChildAt(i * mNumColumns)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val parentTop = child.bottom + params.bottomMargin
            val parentBottom = parentTop + mVerticalDivider?.intrinsicHeight!!
            mVerticalDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom)
            mVerticalDivider.draw(canvas)
        }
    }

    /**
     * Adds horizontal dividers to a RecyclerView with a GridLayoutManager or its subclass.
     * Params:
     * canvas – The Canvas onto which dividers will be drawn
     * parent – The RecyclerView onto which dividers are being added
     */
    private fun drawHorizontalDividers(canvas: Canvas, parent: RecyclerView) {
        val parentTop = parent.paddingTop
        val parentBottom = parent.height - parent.paddingBottom
        // In favor of avoiding NullPointer Exception ;-)
        try {
            for (i in 0 until mNumColumns!!) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val parentLeft = child.right + params.rightMargin
                val parentRight = parentLeft + mHorizontalDivider?.intrinsicWidth!!
                mHorizontalDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom)
                mHorizontalDivider.draw(canvas)
            }
        } catch (e: NullPointerException) {
            // Just placeHolder for catch block
        }

    }

    /**
     * Determines the size and location of offsets between items in the parent RecyclerView.
     * Params:
     * outRect – The Rect of offsets to be added around the child view
     * view – The child view to be decorated with an offset
     * parent – The RecyclerView onto which dividers are being added
     * state – The current RecyclerView.State of the RecyclerView
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val childIsInLeftmostColumn = (parent.getChildAdapterPosition(view) % mNumColumns!!) == 0

        if (!childIsInLeftmostColumn) {
            outRect.left = mHorizontalDivider?.intrinsicWidth!!
        }

        val childIsInFirstRow = (parent.getChildAdapterPosition(view)) < mNumColumns
        if (!childIsInFirstRow) {
            outRect.top = mVerticalDivider?.intrinsicHeight!!
        }
    }
}