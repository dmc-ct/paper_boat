package com.paper.boat.zrdx.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.paper.boat.dream.R;


@SuppressLint("DrawAllocation")
public class ProgressView extends View {

    private Context mContext = null;
    private float pvHeight = 5;//进度条高度 宽度默认100%
    private float pvFontSize = 12;//进度文字大小
    private int pvVSpace = 5;//纵向间距
    private int pvValue = 0;//进度0-100

    //
    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super( context, attrs, defStyle );
    }

    @SuppressLint("Recycle")
    public ProgressView(Context context, AttributeSet attrs) {
        super( context, attrs );
        mContext = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes( attrs,
                    R.styleable.ProgressView );
            pvHeight = a.getFloat( R.styleable.ProgressView_pvHeight,
                    pvHeight );
            pvFontSize = a.getFloat( R.styleable.ProgressView_pvFontSize,
                    pvFontSize );
            //间距
            pvVSpace = a.getInt( R.styleable.ProgressView_pvVSpace, pvVSpace );
            pvValue = a.getInt( R.styleable.ProgressView_pvValue, pvValue );
        }
    }

    public ProgressView(Context context) {
        super( context );
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );

        //进度条宽度 高度
        int height = dip2px( mContext, pvHeight );
        int width = super.getMeasuredWidth();
        pvValue = pvValue < 0 ? 0 : pvValue;
        pvValue = pvValue > 100 ? 100 : pvValue;

        //画进度条背景
        Paint p = new Paint();
        p.setColor( Color.parseColor( "#adadad" ) );
        p.setAntiAlias( true );
        p.setStrokeWidth( height );
        p.setStrokeCap( Paint.Cap.ROUND );
        p.setStyle( Paint.Style.STROKE );

        canvas.drawLine( height / 2, height / 2, width - height / 2, height / 2, p );

        //画进度
        int toX = height / 2;
        if (pvValue > 0 && pvValue <= 100) {
            p = new Paint();
            if (pvValue <= 30) {
                p.setColor( Color.parseColor( "#fa3030" ) );
            } else if (pvValue <= 70) {
                p.setColor( Color.parseColor( "#0FADFF" ) );
            } else {
                p.setColor( Color.parseColor( "#A3E338" ) );
            }
            p.setAntiAlias( true );
            p.setStrokeWidth( height );
            p.setStrokeCap( Paint.Cap.ROUND );
            p.setStyle( Paint.Style.STROKE );

            toX = Math.round( (width - height / 2 - height / 2) / 100.0f * pvValue ) + height / 2;

            canvas.drawLine( height / 2, height / 2, toX, height / 2, p );

            //画矩形(只画右边半截)
            if (toX <= width - height / 2 - height / 2) {
                Paint rectP = new Paint();
                rectP.setColor( p.getColor() );
                rectP.setStyle( Paint.Style.FILL );
                canvas.drawRect( toX, 0, toX + height / 2, height, rectP );
            }
        }

        //字
        Paint valuePaint = new Paint();
        valuePaint.setColor( p.getColor() );
        valuePaint.setAntiAlias( true );
        valuePaint.setTextSize( dip2px( mContext, pvFontSize ) );

        float valueRectWidth = valuePaint.measureText( pvValue + "%" );
        canvas.drawText( pvValue + "%", (toX + height / 2) > valueRectWidth ? (toX + height / 2) - valueRectWidth : 0, height +
                dip2px( mContext, pvVSpace + 10 ), valuePaint );
    }

    // 设置值
    public void setValue(int newValue) {
        pvValue = newValue;
        invalidate();
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
