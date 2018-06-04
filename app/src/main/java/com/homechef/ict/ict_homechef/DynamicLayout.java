package com.homechef.ict.ict_homechef;

import android.widget.LinearLayout;

public class DynamicLayout {

    private LinearLayout ll[];
    private int currentTemp[];
    private int layoutNum;
    private int layoutSize;

    DynamicLayout(LinearLayout[] l, int x)
    {
        ll = l;
        layoutSize = x;
        layoutNum = l.length;
        currentTemp = new int[layoutNum];
    }

    public int selectLayout(int addSize)
    {
        for (int i = 0; i < layoutNum; i++)
        {
            if (currentTemp[i] + addSize <= layoutSize)
            {
                currentTemp[i] += addSize;
                return i;
            }
        }
        return layoutNum - 1;
    }

    public LinearLayout[] getLayout()
    {
        return ll;
    }

    public void calLayoutSize(int viewSize, int layoutNum)
    {
        currentTemp[layoutNum] += viewSize;
    }
}
