/*
 * Copyright (C) 2015-2018 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.evermemo.TESTEDITOR.toolbar.spinner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.evermemo.R;

import java.util.List;

/**
 * This class implements an ArrayAdapter using the parameters from the
 * SpinnerItems object. Use the setter method in SpinnerItems to change the
 * layout of the spinner item or the spinner entry items. Overwrite the
 * getDropDownView() method if you need a more complex layout than a simple
 * TextView for the spinner entry items.
 */
public class SpinnerItemAdapter<T extends SpinnerItem> extends BaseAdapter implements SpinnerItem.OnChangedListener {

    final private int mSpinnerId;
    final private int mSpinnerItemId;
    private final List<T> mItems;
    private final LayoutInflater mInflater;
    @NonNull
    private final Handler mHandler;
    final private SparseArray<View> mViewCache = new SparseArray<>();
    private final int mSelectedBackgroundId;
    private int mSelectedItem;
    // we need this to update the spinner text, for some reason the layout view
    private ViewGroup mParent;
    private String mSpinnerTitle;

    public SpinnerItemAdapter(@NonNull Context context, @NonNull SpinnerItems<T> spinnerItems, int spinnerId, int spinnerItemId) {
        mSelectedItem = spinnerItems.getSelectedItem();
        mItems = spinnerItems.getItems();
        mInflater = LayoutInflater.from(context);
        mHandler = new Handler();
        mSpinnerId = spinnerId;
        mSpinnerItemId = spinnerItemId;

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.rte_ToolbarSpinnerSelectedColor, typedValue, true);
        mSelectedBackgroundId = typedValue.resourceId;
    }

    /**
     * This method returns the spinner view
     */
    @SuppressLint("ViewHolder")
    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        View spinnerView = mInflater.inflate(mSpinnerId, parent, false);

        mParent = parent;
        TextView spinnerTitleView = spinnerView.findViewById(R.id.title);
        updateSpinnerTitle(spinnerTitleView);
        return spinnerView;
    }

    /**
     * Returns the spinner entry view
     */
    @SuppressLint("InlinedApi")
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        SpinnerItem spinnerItem = mItems.get(position);
        spinnerItem.setOnChangedListener(this, position);

        // we can't use the convertView because it keeps handing us spinner layouts (mSpinnerId)
        View spinnerItemView = mInflater.inflate(mSpinnerItemId, parent, false);
        int key = (position << 16) + getItemViewType(position);
        mViewCache.put(key, spinnerItemView);

        bindView(position, spinnerItemView, spinnerItem);

        return spinnerItemView;
    }

    @Override
    public void onSpinnerItemChanged(final Object tag) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int position = (Integer) tag;
                int key = (position << 16) + getItemViewType(position);
                SpinnerItem spinnerItem = mItems.get(position);
                View spinnerItemView = mViewCache.get(key);
                if (spinnerItemView != null) {
                    bindView(position, spinnerItemView, spinnerItem);
                }
            }
        });
    }

    private void bindView(int position, @NonNull View spinnerItemView, @NonNull SpinnerItem spinnerItem) {
        // configure spinner name
        //  TextView nameView = spinnerItemView.findViewById(R.id.spinner_name);
        //  spinnerItem.formatNameView(nameView);

        // configure spinner color
        //   View colorView = spinnerItemView.findViewById(R.id.spinner_color);
        //  spinnerItem.formatColorView(colorView);

        // set background for selected item
        //   View textContainer = spinnerItemView.findViewById(R.id.chip_pacemaker);
        //   textContainer = textContainer == null ? nameView : textContainer;
        //   textContainer.setBackgroundResource(position == mSelectedItem ? mSelectedBackgroundId : android.R.color.transparent);
    }

    /**
     * @return position of the selected item (0..n-1) or -1 if no item is selected
     */
    public int getSelectedItem() {
        return mSelectedItem;
    }

    /**
     * @param position position of the selected item (0..n-1) or -1 if no item is selected
     */
    public void setSelectedItem(int position) {
        mSelectedItem = position;
    }

    public void updateSpinnerTitle(String title) {
        mSpinnerTitle = title;
        if (mParent != null) {
            try {
                TextView spinnerTitleView = mParent.getChildAt(0).findViewById(R.id.title);
                updateSpinnerTitle(spinnerTitleView);
            } catch (Exception ignore) { /* this is a hack but better safe than sorry */ }
        }
    }

    private void updateSpinnerTitle(@Nullable TextView titleView) {
        if (titleView != null) {
            titleView.setText(mSpinnerTitle);
            titleView.setVisibility((mSpinnerTitle == null) ? View.GONE : View.VISIBLE);
            titleView.setHorizontallyScrolling(true);
        }
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}