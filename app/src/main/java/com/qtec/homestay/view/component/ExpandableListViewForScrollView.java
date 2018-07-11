package com.qtec.homestay.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/31
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class ExpandableListViewForScrollView extends ExpandableListView {
  public ExpandableListViewForScrollView(Context context) {
    super(context);
  }

  public ExpandableListViewForScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ExpandableListViewForScrollView(Context context, AttributeSet attrs,
                                         int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }
}
