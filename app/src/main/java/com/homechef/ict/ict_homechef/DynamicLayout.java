package com.homechef.ict.ict_homechef;

import android.widget.LinearLayout;

public class DynamicLayout {

    private LinearLayout ll[];
    private int currentTemp[];
    private int layoutNum;
    private int layoutSize;
    private int maxNum = 0;
    private int nowNum = 0;


    DynamicLayout(LinearLayout[] linearLayouts, int maxLayoutSize, int maxViewNum)
    {
        ll = linearLayouts;
        layoutSize = maxLayoutSize;
        layoutNum = linearLayouts.length;
        currentTemp = new int[layoutNum];
        maxNum = maxViewNum;
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
    public int getMaxNum()
    {
        return maxNum;
    }
    public int getNowNum()
    {
        return nowNum;
    }
    public void setNowNum(int num)
    {
        nowNum = num;
    }
}
