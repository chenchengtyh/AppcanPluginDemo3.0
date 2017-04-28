package com.test;

import com.test.ScreenTool.Screen;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;


public class BussUtil {
	/** dialog ��Ⱥ͸߶����� */
	public static void setDialogParams(Context context, Dialog dialog,
			double pwidth, double mwidth) {
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		Screen screen = ScreenTool.getScreenPix(context);
		if (PadUtil.isPad(context)) {
			params.width = (int) (screen.getWidthPixels() * pwidth);
		} else {
			params.width = (int) (screen.getWidthPixels() * mwidth);
		}
		params.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
		dialog.show();
	}
	
	/** dialog ȫ����Ⱥ͸߶����� */
	public static void setDialogParamsFull(Context context, Dialog dialog) {
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
		params.height = android.view.WindowManager.LayoutParams.MATCH_PARENT;
		dialog.getWindow().setAttributes(params);
		dialog.show();
	}
}
