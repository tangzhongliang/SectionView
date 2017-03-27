package com.leonard.android.views.sectionsample;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leonard.android.views.sectionsample.databinding.ListFragmentBinding;

import java.util.ArrayList;


/**
 * Copyright (C) 2013-2016 RICOH Co.,LTD
 * All rights reserved
 */

public class TestListFragment extends Fragment {

    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ListFragmentBinding mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_test_list, container, false);
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            if (i % 4 == 0) {
                arrayList.add("start" + i);
            }
            arrayList.add(String.format("第%d项", i));
        }
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DefaultItemAnimator animator = new DefaultItemAnimator() {
            @Override
            public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
                return super.animateMove(holder, fromX, fromY, toX, toY);
            }
        };
        mBinding.recycleView.setItemAnimator(animator);
        mBinding.recycleView.addItemDecoration(new DividerItemDecoration(getContext()));
        mBinding.recycleView.setAdapter(new MyAdapter(getContext(), arrayList));
        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefresh.setRefreshing(true);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(2000);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBinding.swipeRefresh.setRefreshing(false);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        return mBinding.getRoot();
    }

    public static TestListFragment newInstance() {
        return new TestListFragment();
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        private Drawable mDivider;
        private Context mContext;

        public DividerItemDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            mContext = context;
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            // item padding value.
            int itemMarginHorizontal = (int) mContext.getResources().getDimension(R.dimen.sound_item_horizontal_margin);
            // left padding value of divider.
            int left = parent.getPaddingLeft() + itemMarginHorizontal;
            // right padding value of divider.
            int right = parent.getWidth() - parent.getPaddingRight() - itemMarginHorizontal;

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}
