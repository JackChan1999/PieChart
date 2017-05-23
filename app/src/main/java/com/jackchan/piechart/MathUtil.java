package com.jackchan.piechart;

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
 * des ：数学工具类
 * gitVersion：2.12.0.windows.1
 * updateAuthor：AllenIverson
 * updateDate：2017/5/23 14:29
 * updateDes：${TODO}
 * ============================================================
 */

public class MathUtil {
	public static float getTouchAngle(float x, float y) {
		float touchAngle = 0;
		if (x < 0 && y < 0) {  //2 象限
			touchAngle += 180;
		} else if (y < 0 && x > 0) {  //1象限
			touchAngle += 360;
		} else if (y > 0 && x < 0) {  //3象限
			touchAngle += 180;
		}
		//Math.atan(y/x) 返回正数值表示相对于 x 轴的逆时针转角，返回负数值则表示顺时针转角。
		//返回值乘以 180/π，将弧度转换为角度。
		touchAngle += Math.toDegrees(Math.atan(y / x));
		if (touchAngle < 0) {
			touchAngle = touchAngle + 360;
		}
		return touchAngle;
	}
}
