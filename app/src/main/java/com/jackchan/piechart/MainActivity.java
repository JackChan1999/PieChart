package com.jackchan.piechart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
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

public class MainActivity extends AppCompatActivity {
	private int[] colors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
			0xFFE6B800, 0xFF7CFC00};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Pie pie = (Pie) findViewById(R.id.pie);
		List<PieEntity> entities = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			entities.add(new PieEntity(i + 1, colors[i]));
		}
		pie.setDatas(entities);

	}
}
