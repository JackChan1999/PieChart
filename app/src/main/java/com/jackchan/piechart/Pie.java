package com.jackchan.piechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * ============================================================
 * Copyright：JackChan和他的朋友们有限公司版权所有 (c) 2017
 * Author：   JackChan
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChan1999
 * GitBook：  https://www.gitbook.com/@alleniverson
 * CSDN博客： http://blog.csdn.net/axi295309066
 * 个人博客： https://jackchan1999.github.io/
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：PieChart
 * Package_Name：com.jackchan.piechart
 * Version：1.0
 * time：2017/5/23 14:29
 * des ：自定义控件之饼状图
 * gitVersion：2.12.0.windows.1
 * updateAuthor：AllenIverson
 * updateDate：2017/5/23 14:29
 * updateDes：${TODO}
 * ============================================================
 */

public class Pie extends View {

	private List<PieEntity> entities;
	private int width;
	private int height;
	private int radius;
	private Path path;
	private Paint paint;
	private Paint linePaint;

	public Pie(Context context) {
		this(context, null);
	}

	public Pie(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Pie(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	//定义扇形的外接矩形
	private RectF rectF;
	private RectF touchRectF;

	private void init() {
		rectF = new RectF();
		//初始化path
		path = new Path();
		//初始化画笔
		paint = new Paint();
		paint.setAntiAlias(true);

		//创建绘制直线对应的画笔
		linePaint = new Paint();
		linePaint.setColor(Color.BLACK);
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(3);
		linePaint.setTextSize(20);

		touchRectF = new RectF();

	}

	//当自定义控件的尺寸已经决定好的时候回调
	//这个方法是在onMeasure方法执行后执行,最终的测量结果已经产生
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//获得当前控件的宽高
		this.width = w;
		this.height = h;
		//为了防止绘制后超出屏幕区域,获取屏幕的宽高的较小值
		int min = Math.min(w, h);
		radius = (int) (min * 0.7f / 2);

		rectF.left = -radius;
		rectF.top = -radius;
		rectF.right = radius;
		rectF.bottom = radius;

		touchRectF.left = -radius - 15;
		touchRectF.top = -radius - 15;
		touchRectF.right = radius + 15;
		touchRectF.bottom = radius + 15;

	}

	/**
	 *  步骤分解:
	 *  1. 最内侧的扇形组成的圆形区域的绘制
	 *  2. 中间的短线段的绘制
	 *  3. 最外侧的文本的绘制
	 *
	 * @param canvas
	 */

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		canvas.translate(width / 2, height / 2);
		//对扇形的绘制处理
		drawPie(canvas);
		canvas.restore();

	}

	private void drawPie(Canvas canvas) {
		float startAngle = 0;
		//绘制扇形
		for (int i = 0; i < entities.size(); i++) {
			//设置扇形区域的颜色
			paint.setColor(entities.get(i).color);
			path.moveTo(0, 0);
			PieEntity entity = entities.get(i);
			//获取每一个数据对应的弧度
			float sweepAngle = entity.value / totalValue * 360 - 1;
			if (position==i) {
				//绘制一个突出的扇形区域
				path.arcTo(touchRectF,startAngle,sweepAngle);
			} else {
				path.arcTo(rectF, startAngle, sweepAngle);

			}
			canvas.drawPath(path, paint);

			//绘制直线
			//Math.toRadians(参数):指的是将参数的弧度转换为角度
			float startX = (float) (radius * Math.cos(Math.toRadians(startAngle + sweepAngle / 2)));
			float startY = (float) (radius * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));
			float endX = (float) ((radius + 30) * Math.cos(Math.toRadians(startAngle + sweepAngle / 2)));
			float endY = (float) ((radius + 30) * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));
			canvas.drawLine(startX, startY, endX, endY, linePaint);

			startAngles[i] = startAngle;

			//每个扇形区域的起始点就是上一个扇形区域的终点
			startAngle += sweepAngle + 1;
			//在每次绘制扇形之后要对path进行重置操作
			//这样就可以清除上一次绘制path使用的画笔的相关记录
			path.reset();

			//绘制文本
			//1,文本内容

			String percent = String.format("%.1f", entities.get(i).value / totalValue * 100);
			percent = percent + "%";
			Log.i("test", "percent:" + percent + ", startAngle:" + startAngle % 360.0f);
			if (startAngle % 360.0f >= 90.0f && startAngle % 360.0f <= 270.0f) {
				//计算文本的宽度
				float textWidth = linePaint.measureText(percent);
				canvas.drawText(percent, endX - textWidth, endY, linePaint);
			} else {
				//2,文本的位置
				canvas.drawText(percent, endX, endY, linePaint);
			}

		}
	}

	private float totalValue;
	private float[] startAngles;

	public void setDatas(List<PieEntity> entities) {
		this.entities = entities;
		//计算总数据
		for (PieEntity entity : entities) {
			totalValue += entity.value;
		}
		startAngles = new float[entities.size()];
	}

	//当用户与手机屏幕进行交互的时候(触摸)
	//触摸事件处理
	//1,按下去
	//2,移动
	//3,抬起
	//参数:触摸事件,这个事件是由用户与屏幕交互产生的,这个事件包含上述三种情况
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//获取用户对屏幕的行为
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				//做点击范围的认定
				//获取用户点击的位置距当前视图的左边缘的距离
				float x = event.getX();
				float y = event.getY();
				//将点击的x和y坐标转换为以饼状图为圆心的坐标
				x = x - width / 2;
				y = y - height / 2;
				float touchAngle = MathUtil.getTouchAngle(x, y);
				float touchRadius = (float) Math.sqrt(x * x + y * y);
				//判断触摸的点距离饼状图圆心的距离<饼状图对应圆的圆心
				if (touchRadius < radius) {
					//说明是一个有效点击区域
					//查找触摸的角度是否位于起始角度集合中
					//binarySearch:参数2在参数1对应的集合中的索引
					//未找到,则返回 -(和搜索的值附近的大于搜索值的正确值对应的索引值+1)
					//{1,2,3}
					//搜索1:返回值1在集合中对应的索引0
					//1.2:返回值为 -(1+1) -2
					//1.8:返回值 -(1+1) -2
					int searchResult = Arrays.binarySearch(startAngles, touchAngle);
					if (searchResult < 0) {
						position = -searchResult - 1 - 1;
					} else {
						position = searchResult;
					}
					Log.i("test", "position:" + position);
					//让onDraw方法重新调用:
					//重绘
					invalidate();
				}
				break;
			case MotionEvent.ACTION_MOVE:

				break;
			case MotionEvent.ACTION_UP:

				break;
		}
		return super.onTouchEvent(event);
	}
	//被点击的扇形的位置
	private int position;
}
