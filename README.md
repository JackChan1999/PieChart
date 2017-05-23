# 自定义控件之饼状图

<img src="art/piechart.jpg" width="400" />

## 定义基本信息载体，即javabean

```java
public class PieEntity {
	//对应数据占用的份额
	public float value;
	//对应数据的颜色
	public int color;

	public PieEntity(float value, int color) {
		this.value = value;
		this.color = color;
	}
}

private int[] colors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,0xFFE6B800, 0xFF7CFC00};
```

## 初始化三个画笔

```java
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
```

## 计算绘制饼状图的外接矩形的左上右下距离

```java
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
	//获得饼状图的半径:宽度和高度的较小值/2*0.7
	radius = (int) (min * 0.7f / 2);
	//获取饼状图的内接矩形
	rectF.left = -radius;
	rectF.top = -radius;
	rectF.right = radius;
	rectF.bottom = radius;
	touchRectF.left = -radius - 15;
	touchRectF.top = -radius - 15;
	touchRectF.right = radius + 15;
	touchRectF.bottom = radius + 15;
}
```

## 绘制饼状图

```java
if (mDataList == null)
		return;
//保存画布
canvas.save();
//屏幕画布到屏幕中间
canvas.translate(mTotalWidth / 2, mTotalHeight / 2);
//绘制饼图的每块区域
drawPie(canvas);
//还原画布
canvas.restore();
//5,绘制饼状图的步骤:
//起始的角度
float startAngle = 0;
//遍历数据绘制饼状图
for (int i = 0; i < mDataList.size(); i++) {
	/************************①绘制扇形****************************/
	//每个扇形的角度:数据值占总数据值的百分比
	float sweepAngle = mDataList.get(i).getValue() / mTotalValue * 360 - 1;
	//移动到初始点
	mPath.moveTo(0, 0);
	//绘制某一块扇形
	mPath.arcTo(mRectF, startAngle, sweepAngle);
	//设置颜色
	mPaint.setColor(mDataList.get(i).getColor());
	//绘制路径
	canvas.drawPath(mPath, mPaint);
	//重置路径:为了下次再绘制不会和上一次重叠
	mPath.reset();
	/***************************②绘制指示线*******************************/
	//绘制线和文本
	//确定直线的起始和结束的点的位置
	float startLineX = (float) (mRadius * Math.cos(Math.toRadians(startAngle + sweepAngle / 2)));
	float startLineY = (float) (mRadius * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));
	float endLineX = (float) ((mRadius + 30) * Math.cos(Math.toRadians(startAngle + sweepAngle / 2)));
	float endLineY = (float) ((mRadius + 30) * Math.sin(Math.toRadians(startAngle + sweepAngle / 2)));
	canvas.drawLine(startLineX, startLineY, endLineX, endLineY, mLinePaint);
	//记录下一次的起始角度为之前所有绘制的扇形弧度+1
	startAngle += sweepAngle + 1;
	/***************************③绘制文本*******************************/
	float res = mDataList.get(i).getValue() / mTotalValue * 100;
	//格式化数据,小数点后保留1位有效数字
	String percent = String.format("%.1f", entities.get(i).value / totalValue * 100);
	//将弧度转化为角度
	float v = startAngle % 360;
	//判断如果起始角度>90度并且小于270度
	if (startAngle % 360.0 >= 90.0 && startAngle % 360.0 <= 270.0) {
		Log.i("test", "resToRound:" + resToRound);
		//将文本绘制到文本连接线终点的左边
		canvas.drawText(resToRound + "%", endLineX - mTextPaint.measureText(resToRound + "%"), endLineY, mTextPaint);
	} else {
		//将文本绘制到文本连接线终点所在位置
		canvas.drawText(resToRound + "%", endLineX, endLineY, mTextPaint);
	}
	/****************************************************************/
}
```