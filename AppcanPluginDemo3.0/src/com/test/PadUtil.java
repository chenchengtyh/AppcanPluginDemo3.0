package com.test;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class PadUtil {

	/**
	 * �ж��Ƿ�Ϊƽ��
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public static boolean isPad(Context context) {

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		// ��Ļ���?
		@SuppressWarnings("deprecation")
		float screenWidth = display.getWidth();
		// ��Ļ�߶�
		@SuppressWarnings("deprecation")
		float screenHeight = display.getHeight();
		
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		// ��Ļ�ߴ�
		double screenInches = Math.sqrt(x + y);
		// ����6�ߴ���ΪPad
		if (screenInches >= 6.0) {
			return true;
		}
		return false;
	}
}
