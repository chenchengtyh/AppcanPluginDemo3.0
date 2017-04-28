package com.test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.platform.push.PushBroadCastReceiver;

import com.camera.FileUtilss;
import com.camera.LogcatHelper;

import Decoder.BASE64Encoder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import javax.crypto.spec.DHGenParameterSpec;

@SuppressLint("NewApi")
public class EUExDemo extends EUExBase {

	static final String func_activity_callback = "uexESign.cbStartActivityForResult";
	static final String func_dialog_callback = "uexESign.cbTest_showInputDialog";
	static final String func_sign_callback = "uexESign.cbSignResult";
	static final String func_signpic_callback = "uexESign.cbSignPicResult";

	static final String func_on_callback = "javascript:uexESign.onCallBack";

	static final int mMyActivityRequestCode = 10000;
	static final int eMyActivityRequestCode = 10001;
	static final int mMyActivityCameraRequestCode = 6000;
	private static final String TAG = "uexESign";
	private static final String CALLBACK_ON_VIEW_BUTTON_CLICK = "uexESign.onViewButtonClick";
	private static final String CALLBACK_ON_FRAGMENT_BUTTON_CLICK = "uexESign.onFragmentButtonClick";

	private Vibrator m_v;
	private View mAddView;
	private ViewDataVO mAddViewData;

	private DemoFragment mAddFragmentView;
	private ViewDataVO mAddFragmentData;

	private FileUtilss fu;
	String filebase64;
	String filenamepath;

	public EUExDemo(Context context, EBrowserView view) {
		super(context, view);
	}

	public void test_addView(String[] parm) {
		if (parm.length < 1) {
			return;
		}
		mAddViewData = DataHelper.gson.fromJson(parm[0], ViewDataVO.class);
		if (mAddViewData == null) {
			return;
		}
		if (mAddView != null) {
			test_removeView(null);
		}
		mAddView = new DemoView(mContext, "我是一个添加的view的text", "view button",
				onViewButtonClick);
		if (mAddViewData.isScrollWithWebView()) {
			android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(
					mAddViewData.getWidth(), mAddViewData.getHeight(),
					mAddViewData.getLeft(), mAddViewData.getTop());
			addViewToWebView(mAddView, lp, TAG);
		} else {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					mAddViewData.getWidth(), mAddViewData.getHeight());
			lp.leftMargin = mAddViewData.getLeft();
			lp.topMargin = mAddViewData.getTop();
			addViewToCurrentWindow(mAddView, lp);
		}
	}

	public void test_removeView(String[] params) {
		if (mAddViewData == null || mAddView == null) {
			return;
		}
		if (mAddViewData.isScrollWithWebView()) {
			removeViewFromWebView(TAG);
		} else {
			removeViewFromCurrentWindow(mAddView);
			mAddView = null;
		}
	}

	// this case start a Activity: HelloAppCanNativeActivity
	public void test_startActivityForResult(String[] parm) {
		Toast.makeText(mContext, "启动test!", Toast.LENGTH_LONG).show();
		Intent intent = new Intent();
		intent.setClass(mContext, HelloAppCanNativeActivity.class);
		try {
			startActivityForResult(intent, mMyActivityRequestCode);
		} catch (Exception e) {
			Toast.makeText(mContext, "找不到此Activity!!", Toast.LENGTH_LONG)
					.show();
		}
	}

	// this case to use Vibrator
	public void test_vibrator(String[] parm) {
		if (parm.length < 1) {
			return;
		}
		VibratorDataVO dataVO = DataHelper.gson.fromJson(parm[0],
				VibratorDataVO.class);
		double inMilliseconds = dataVO.getTime();
		try {
			if (null == m_v) {
				m_v = (Vibrator) mContext
						.getSystemService(Service.VIBRATOR_SERVICE);
			}
			m_v.vibrate((int) inMilliseconds);
		} catch (SecurityException e) {
			Toast.makeText(mContext, "未配置震动权限或参数错误!!", Toast.LENGTH_LONG)
					.show();
			return;
		}
		String jsCallBack = func_on_callback + "('" + "成功震动了" + inMilliseconds
				+ "毫秒" + "');";
		onCallback(jsCallBack);
	}

	// 拍照插件入口
	public void test_starActivityCameraView(String[] parm) {

		// LogcatHelper.getInstance(mContext).start();

		filenamepath = Environment.getExternalStorageDirectory()
				+ "/photopath/";

		fu = new FileUtilss(filenamepath);

		Intent intent = new Intent();
		intent.setClass(mContext, CameraViewMain.class);
		try {
			startActivityForResult(intent, mMyActivityCameraRequestCode);
		} catch (Exception e) {
			Toast.makeText(mContext, "找不到此Activity!!", Toast.LENGTH_LONG)
					.show();
		}

	}

	// this case show a input dialog
	public void test_showInputDialog(String[] parm) {
		/*
		 * if (parm.length < 1) { return; }
		 * 
		 * DialogDataVO dataVO = DataHelper.gson.fromJson(parm[0],
		 * DialogDataVO.class); String defaultValue = dataVO.getDefaultValue();
		 * new DialogUtil(mContext, this).show(defaultValue);
		 */
		Intent intent = new Intent();
		intent.setClass(mContext, MainActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		try {
			startActivityForResult(intent, eMyActivityRequestCode);
		} catch (Exception e) {
			Toast.makeText(mContext, "找不到此Activity!!", Toast.LENGTH_LONG)
					.show();
		}
		/*
		 * Intent intent = new Intent(); intent.setClass(mContext,
		 * MainActivity.class); startActivity(intent);
		 */
	}

	// this case show a custom view into window
	public void test_addFragment(String[] parm) {
		if (parm.length < 1) {
			return;
		}
		if (mAddFragmentView != null) {
			test_removeFragment(null);
		}
		mAddFragmentData = DataHelper.gson.fromJson(parm[0], ViewDataVO.class);
		if (mAddFragmentData == null)
			return;
		mAddFragmentView = new DemoFragment();
		mAddFragmentView.setFragmentText("我是一个添加的fragment的text");
		mAddFragmentView.setFragmentButtonText("fragment button");
		mAddFragmentView.setOnButtonClick(onFragmentButtonClick);
		if (mAddFragmentData.isScrollWithWebView()) {
			android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(
					mAddFragmentData.getWidth(), mAddFragmentData.getHeight(),
					mAddFragmentData.getLeft(), mAddFragmentData.getTop());
			addFragmentToWebView(mAddFragmentView, lp, TAG);
		} else {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					mAddFragmentData.getWidth(), mAddFragmentData.getHeight());
			lp.leftMargin = mAddFragmentData.getLeft();
			lp.topMargin = mAddFragmentData.getTop();
			addFragmentToCurrentWindow(mAddFragmentView, lp, TAG);
		}

	}

	// this case remove a custom view from window
	public void test_removeFragment(String[] parm) {
		if (mAddFragmentData == null || mAddFragmentView == null) {
			return;
		}
		if (mAddFragmentData.isScrollWithWebView()) {
			removeFragmentFromWebView(TAG);
		} else {
			removeFragmentFromWindow(mAddFragmentView);
			mAddFragmentView = null;
		}
	}

	private void callBackPluginJs(String methodName, String jsonData) {
		String js = SCRIPT_HEADER + "if(" + methodName + "){" + methodName
				+ "('" + jsonData + "');}";
		onCallback(js);
	}

	// clean something
	@Override
	protected boolean clean() {
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("jochen", "requestCode=" + requestCode);
		if (requestCode == mMyActivityRequestCode) {
			JSONObject jsonObject = new JSONObject();
			try {
				if (resultCode == Activity.RESULT_OK) {
					String ret = data.getStringExtra("result");
					jsonObject.put("result", ret);
				} else {
					jsonObject.put("result", "cancel");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			callBackPluginJs(func_activity_callback, jsonObject.toString());
		}
		if (requestCode == eMyActivityRequestCode) {
			if (resultCode == Activity.RESULT_OK) {
				JSONObject jsonObject = new JSONObject();
				String pathstr = data.getStringExtra("imgpath");
				String basestr = data.getStringExtra("basestr");
				try {
					// jsonObject.put("imgpath", pathstr);
					// Log.d("jochen", "2222222222222=" + basestr.length());
					jsonObject.put("", basestr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				callBackPluginJs(func_sign_callback, jsonObject.toString());
			}

		}
		if (requestCode == mMyActivityCameraRequestCode) {
			if (resultCode == Activity.RESULT_OK) {
				JSONObject jsonObject = new JSONObject();
				String imgpath = data.getStringExtra("imgpath");
				// String basestrpic = data.getStringExtra("basestrpic");
				// String uploadBuffer = null;

				// try {
				// FileInputStream fis = new FileInputStream(imgpath);
				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// byte[] buffer = new byte[1024];
				// int count = 0;
				// while ((count = fis.read(buffer)) >= 0) {
				// baos.write(buffer, 0, count);
				// }
				// BASE64Encoder encoder = new BASE64Encoder();
				// uploadBuffer = encoder.encode(baos.toByteArray());
				//
				// /*
				// * base64字符串保存到txt文件
				// fu.writeTxtToFile(uploadBuffer, filenamepath, "base64.txt");
				// filebase64 = filenamepath+"base64.txt";
				// */
				// fis.close();
				// } catch (IOException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }

				try {
					// jsonObject.put("imgpath", pathstr);

					jsonObject.put("result", imgpath);

					/*
					 * base64转回bitmap测试 AlertDialog.Builder newbuilder = new
					 * Builder(mContext); final ImageView vw = new
					 * ImageView(mContext); Bitmap bitmap = null; byte[]
					 * bitmapArray; bitmapArray = Base64.decode(uploadBuffer,
					 * Base64.DEFAULT); bitmap =
					 * BitmapFactory.decodeByteArray(bitmapArray, 0,
					 * bitmapArray.length);
					 * 
					 * vw.setImageBitmap(bitmap); newbuilder.setView(vw);
					 * newbuilder.show();
					 */
					// Log.d("jochen", "1111111111111=" +
					// uploadBuffer.length());
					// uploadBuffer = null;

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				callBackPluginJs(func_signpic_callback, jsonObject.toString());
			}

		}
	}

	public void callbackDialog(String ret) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("content", ret);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		callBackPluginJs(func_dialog_callback, jsonObject.toString());
	}

	public interface OnButtonClick {
		public void onButtonClick();
	}

	OnButtonClick onViewButtonClick = new OnButtonClick() {
		@Override
		public void onButtonClick() {
			callBackPluginJs(CALLBACK_ON_VIEW_BUTTON_CLICK, "");
		}
	};

	OnButtonClick onFragmentButtonClick = new OnButtonClick() {
		@Override
		public void onButtonClick() {
			callBackPluginJs(CALLBACK_ON_FRAGMENT_BUTTON_CLICK, "");
		}
	};
}
