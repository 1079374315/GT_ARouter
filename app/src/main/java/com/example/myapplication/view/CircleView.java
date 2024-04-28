package com.example.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.baselibrary.ARouter;
import com.example.baselibrary.Config;
import com.example.myapplication.R;
import com.gsls.gt.GT;
import com.gsls.gt_databinding.route.annotation.GT_Autowired;
import com.gsls.gt_databinding.route.annotation.GT_Route;


/**
 * @author：King
 * @time：2020/12/10-10:33
 * @moduleName：
 * @businessIntroduction：
 * @loadLibrary：GT库
 */
@GT_Route(value = Config.AppConfig.VIEW, extras = "View 模块")
public class CircleView extends View {

    //默认颜色
    private int mColor = Color.RED;

    //默认大小
    private int mDefaultWidth = 250;
    private int mDefaultHeight = 250;

    //创建画笔
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @GT_Autowired
    private String name;

    public CircleView(Context context) {
        super(context);
        mPaint.setColor(mColor);
    }

    //接受传递过来的 Bundle 值
    public CircleView(Context context, Bundle bundle) {
        super(context);
        ARouter.getInstance().inject(this); //与Autowired配合使用
        GT.logt("name:" + name);
        String extra = bundle.getString("extra");
        GT.logt("extra:" + extra);
        mPaint.setColor(mColor);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 获取设置的颜色
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor = typedArray.getColor(R.styleable.CircleView_color, Color.RED);
        typedArray.recycle();
        mPaint.setColor(mColor);
    }


    /**
     * 处理 wrap_content 会填充满屏的问题
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);//宽的测量大小，模式
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);//高的测量大小，模式
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int w = widthSpecSize;   //定义测量宽，高(不包含测量模式),并设置默认值，查看View#getDefaultSize可知
        int h = heightSpecSize;

        //处理wrap_content的几种特殊情况
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            w = mDefaultWidth;  //单位是px
            h = mDefaultHeight;
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            //只要宽度布局参数为wrap_content， 宽度给固定值200dp(处理方式不一，按照需求来)
            w = mDefaultWidth;
            //按照View处理的方法，查看View#getDefaultSize可知
            h = heightSpecSize;
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            w = widthSpecSize;
            h = mDefaultHeight;
        }
        //给两个字段设置值，完成最终测量
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //TODO 处理 Padding 无效的问题
        //获取设置的内间距
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //获取设置的宽高
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        //获取组件半径
        int radius = Math.min(width, height) / 2;


        //绘制组件
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2, radius, mPaint);
    }


    private void log(Object msg) {
        Log.i("GT_i", msg.toString());
    }

}
