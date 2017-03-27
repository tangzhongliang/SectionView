package com.leonard.android.views.section;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (C) 2013-2016 RICOH Co.,LTD
 * All rights reserved
 */

public class SectionRecyclerView extends RecyclerView {
    private PinnedSection mPinnedSection;
    public static int ITEM_PINNED_TYPE = 1;

    public SectionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        if (mPinnedSection != null) {
            int save = c.save();
//            c.translate(0, mPinnedSection.offset);
            drawChild(c, mPinnedSection.viewHolder.itemView, getDrawingTime());
//            c.restoreToCount(save);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int[] sectionPosition = findSectionPosition();
        createPinnedShadow(sectionPosition[0], sectionPosition[1]);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mPinnedSection = null;
    }

    int[] findSectionPosition() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        int itemViewType = getAdapter().getItemViewType(firstCompletelyVisibleItemPosition);
        View firstCompleteView = layoutManager.findViewByPosition(firstCompletelyVisibleItemPosition);
        int headPosition = -1;
        int headTopOffset = 0;
        if (firstCompleteView.getTop() > 0) {
            for (int i = firstCompletelyVisibleItemPosition - 1; i > -1; i--) {
                if (getAdapter().getItemViewType(i) == ITEM_PINNED_TYPE) {
                    headPosition = i;
                    break;
                }
            }
        } else {
            headPosition = firstCompletelyVisibleItemPosition;
        }
        if (firstCompletelyVisibleItemPosition - headPosition >= 1 && itemViewType == 1) {
            headTopOffset = firstCompleteView.getTop() - firstCompleteView.getHeight();
        }
        return new int[]{headPosition, headTopOffset};
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }

    /**
     * Wrapper class for pinned section view and its position in the list.
     */
    static class PinnedSection {
        public ViewHolder viewHolder;
        public int position;
        public long id;
        public int offset;
    }

    void createPinnedShadow(int position, int offset) {

        // try to recycle shadow
        PinnedSection pinnedShadow = null;

        // create new shadow, if needed
        if (pinnedShadow == null) {
            ViewHolder viewHolder = getAdapter().createViewHolder(this, 0);
            pinnedShadow = new PinnedSection();
            pinnedShadow.viewHolder = viewHolder;
            ViewGroup.LayoutParams layoutParams = pinnedShadow.viewHolder.itemView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = generateDefaultLayoutParams();
                pinnedShadow.viewHolder.itemView.setLayoutParams(layoutParams);
            }
            View pinnedView = pinnedShadow.viewHolder.itemView;
            int heightMode = View.MeasureSpec.getMode(layoutParams.height);
            int heightSize = View.MeasureSpec.getSize(layoutParams.height);

            if (heightMode == View.MeasureSpec.UNSPECIFIED) heightMode = View.MeasureSpec.EXACTLY;

            int maxHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            if (heightSize > maxHeight)
                heightSize = maxHeight;

            // measure & layout
            int ws = View.MeasureSpec.makeMeasureSpec(getWidth() - getPaddingLeft() - getPaddingRight(), View.MeasureSpec.EXACTLY);
            int hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);
            pinnedView.measure(ws, hs);
            // read layout parameters
            pinnedView.layout(0, offset, pinnedView.getMeasuredWidth(), pinnedView.getMeasuredHeight() + offset);
        }
        pinnedShadow.position = position;
        // request new view using recycled view, if such
        getAdapter().bindViewHolder(pinnedShadow.viewHolder, position);
        // initialize pinned shadow
        pinnedShadow.position = position;
        pinnedShadow.id = getAdapter().getItemId(position);

        // store pinned shadow
        pinnedShadow.offset = offset;
        mPinnedSection = pinnedShadow;
    }

    public static void sort(List list) {

        String sections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        list.addAll(0, Arrays.asList(sections.toCharArray()));
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });
        Iterator iterator = list.iterator();
        int i;
        while (iterator.hasNext()){
            Object current = iterator.next();

        }
    }
}
