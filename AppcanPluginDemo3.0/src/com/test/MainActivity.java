package com.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.widgetone.uexDemo.R;

import com.camera.LogcatHelper;
import com.test.ColorPickerView.OnColorChangedListener;
import com.test.ScreenTool.Screen;

import Decoder.BASE64Encoder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;

public class MainActivity extends Activity {
	Context context;
	static final int BACKGROUND_COLOR = Color.WHITE;
	// 画笔种类
	int hblx = 0;
	public SharedPreferences sharedPreferences;
	Dialog dialog = null;
	TextView hbzl;// 画笔种类
	String[] hbary;// 画笔种类数组
	Screen screen;
	LayoutParams p;
	LinearLayout.LayoutParams Params;
	PaintView mView;
	boolean tcstate = false;// 填充状态
	boolean isDraw = false;// 是否绘画过
	int countXY;// 画布上不同点的像素个数
	static Bitmap startbitmap;
	TextView clstate;
	static Bitmap bitmap;
	String imagepath;
	int flag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		setContentView(EUExUtil.getResLayoutID("plugin_uexscrawl_main"));
		context = this;
		screen = ScreenTool.getScreenPix(this);
		sharedPreferences = getSharedPreferences("lxqcsp", MODE_PRIVATE);
		p = getWindow().getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (screen.getHeightPixels() * 1);
		p.width = (int) (screen.getWidthPixels() * 1);
		Params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		// Params = new LinearLayout.LayoutParams(600,300);
		mView = new PaintView(context);
		mView.setHuaBiKd(8);
		LinearLayout linear = (LinearLayout) findViewById(EUExUtil
				.getResIdID("linear"));
		linear.addView(mView, Params);
		// mView.requestFocus();

		/*
		 * getImageBitmap(); // 绘制历史签名 if (startbitmap != null) { mView.start();
		 * }
		 */

		/*
		 * // 填充 View tianchong = findViewById(R.id.tianchong); final TextView
		 * tcstatetv = (TextView) findViewById(R.id.tcstate);
		 * tcstatetv.setText(sharedPreferences.getString("hbtc", "否")); if
		 * ("是".equals(tcstatetv.getText().toString())) { tcstate = false; }
		 * else { tcstate = true; } mView.setTianChong();
		 * tianchong.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { mView.setTianChong(); if
		 * (tcstate) { sharedPreferences.edit().putString("hbtc", "是").commit();
		 * tcstatetv.setText("是"); } else {
		 * sharedPreferences.edit().putString("hbtc", "否").commit();
		 * tcstatetv.setText("否"); } } });
		 */
		// 宽度
		/*
		 * View kuangdu=findViewById(R.id.kuangdu); final TextView
		 * kdstate=(TextView) findViewById(R.id.kdstate);
		 * kdstate.setText(sharedPreferences.getInt("hbkd", 0)+"");
		 * mView.setHuaBiKd
		 * (Integer.parseInt(kdstate.getText().toString().trim()));
		 */
		/*
		 * kuangdu.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { showKdDialog(kdstate); }
		 * });
		 */

		Button cancle = (Button) findViewById(EUExUtil.getResIdID("cancle"));
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		/*
		 * int color=sharedPreferences.getInt("color", Color.BLACK); int
		 * alpha=255-sharedPreferences.getInt("tmd", 0); mView.setColor(color,
		 * alpha); clstate.setBackgroundColor(color);
		 */

		/*
		 * // 画笔选择 View huabi = findViewById(R.id.huabi); hbzl=(TextView)
		 * findViewById(R.id.hbzl); hbary=new String[] { "随笔", "橡皮" };
		 * hblx=sharedPreferences.getInt("hblx", 0); mView.setHuaBi(hblx);
		 * huabi.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { dialog=new
		 * AlertDialog.Builder(context).setTitle("选择所要绘制的图形")
		 * .setSingleChoiceItems(hbary, hblx, new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { hblx
		 * = arg1; sharedPreferences.edit().putInt("hblx", arg1).commit();
		 * mView.setHuaBi(arg1); dialog.dismiss(); } }) .setNegativeButton("取消",
		 * null) .show(); } });
		 */

		// 保存
		Button save = (Button) findViewById(EUExUtil.getResIdID("save"));
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String pathtemp = "";
				String basestr = "";
				int red = 255;// 白色像素R
				int R;// 像素R值
				int pixelColor;
				ByteArrayOutputStream baos = null;
				try {
					File file = createImageFile();
					pathtemp = file.getAbsolutePath();
					bitmap = mView.getCachebBitmap();
					// 开始按600*300缩放
					int width = bitmap.getWidth();
					int height = bitmap.getHeight();
					// 计算缩放比例
					float scaleWidth = ((float) 400) / width;
					float scaleHeight = ((float) 600) / height;
					// 取得想要缩放的matrix参数
					Matrix matrix = new Matrix();
					matrix.postScale(scaleWidth, scaleHeight);
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
							matrix, true);
					// 缩放结束
					baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
					byte[] photoBytes = baos.toByteArray();
					// 判断图片大小，如果小于3K就提示是否强制保存
					if (photoBytes != null) {
						BASE64Encoder encoder = new BASE64Encoder();
						basestr = encoder.encode(photoBytes);
						new FileOutputStream(file).write(photoBytes);
					}

					int h = bitmap.getHeight();
					int w = bitmap.getWidth();
					for (int y = 0; y < h; y++) {
						if (isDraw) {
							break;
						}
						for (int x = 0; x < w; x++) {
							pixelColor = bitmap.getPixel(x, y);
							R = Color.red(pixelColor);
							if (R != red) {
								countXY++;
								if (countXY > 200) {
									isDraw = true;
									break;
								}
							}
						}
					}

					final Intent it = new Intent();
					it.putExtra("imgpath", pathtemp);
					it.putExtra("basestr", basestr);
					if (isDraw) {
//						Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT)
//								.show();
						setResult(Activity.RESULT_OK, it);
						finish();
					} else {
						if (countXY < 10) {
							Toast.makeText(context, "还未签字!请先签字",
									Toast.LENGTH_SHORT).show();
						} else {
							new AlertDialog.Builder(MainActivity.this)
									.setTitle("系统提示")
									.setMessage("检测签名可能未完成，是否继续提交?")
									// 设置显示内容
									.setPositiveButton(
											"确定提交",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method stub
													// 在这里做要处理的事情
													setResult(
															Activity.RESULT_OK,
															it);
													finish();
												}
											})
									.setNegativeButton(
											"重新签字",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// TODO Auto-generated
													// method stub
													dialog.dismiss();
												}
											}).show();
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (baos != null) {
							baos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});

		/*
		 * // 清空 Button clear = (Button) findViewById(R.id.clear);
		 * clear.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { if (flag == 0 &&
		 * startbitmap != null) { mView.start(); } if (flag != 0) {
		 * mView.clear(); } } });
		 */
		// 新建
		Button newbuild = (Button) findViewById(EUExUtil.getResIdID("newbuild"));
		newbuild.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/*
				 * new AlertDialog.Builder(context) .setTitle("新建画布")
				 * .setMessage("新建功能会清除画布中所有内容，是否继续？") .setNegativeButton("继续",
				 * new DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface arg0, int arg1)
				 * { mView.clear(); int color = sharedPreferences.getInt(
				 * "color", Color.BLACK); int alpha = 255 - sharedPreferences
				 * .getInt("tmd", 0); mView.setColor(color, alpha); hblx =
				 * sharedPreferences.getInt("hblx", 0); mView.setHuaBi(hblx);
				 * flag = flag + 1; } }).setPositiveButton("取消", null).create()
				 * .show();
				 */
				countXY = 0;
				isDraw = false;
				mView.clear();
			}
		});

	}

	public static void getImageBitmap() {
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/signimg";
		// startbitmap = getImageBitmap("1234", path);
		startbitmap = getImageBitmap("1", path);
	}

	public static Bitmap getImageBitmap(String ydh, String path) {
		// 函数GetTestXlsFileName功能：遍历fileAbsolutePath目录下的所有指定扩展名文件
		// 并将文件名保存在Vector中
		String filepath = "";
		Bitmap bitmap = null;
		File file = new File(path);
		if (file.exists()) {
			File[] subFile = file.listFiles();
			for (int i = 0; i < subFile.length; i++) {
				// 判断是否为文件夹
				if (!subFile[i].isDirectory()) {
					String tempName = subFile[i].getName();
					// 判断是否为xls或xlsx结尾
					if (tempName.trim().toLowerCase().endsWith(".png")
							&& tempName.contains(ydh)) {
						filepath = path + "/" + tempName;
						break;
					}
				}

			}
			if (!"".equals(filepath)) {
				File file1 = new File(filepath);
				if (file1.exists()) {
					bitmap = BitmapFactory.decodeFile(filepath);
				}
			}
		}

		return bitmap;
	}

	/** 存放文件位置 */
	private File createImageFile() throws IOException {
		File image = null;
		String fileName = "";
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			fileName = Environment.getExternalStorageDirectory().getPath()
					+ "/signimg";
			// deleteImage(fileName, "1234");//删除之前的图片

			File file = new File(fileName);
			if (!file.exists()) {
				file.mkdirs();
			}
			// image = new File(fileName + "/1234_" +
			// String.valueOf(System.currentTimeMillis()) + ".png");
			image = new File(fileName + "/1234" + ".png");
			imagepath = image.getAbsolutePath();
		} else {
			Toast.makeText(context, "没有SD卡", Toast.LENGTH_SHORT).show();
		}
		return image;
	}

	/** 删除指定文件夹下指定图片 */
	public static void deleteImage(String path, String ydh) {
		File f = new File(path);
		if (f.exists()) {
			File[] fl = f.listFiles();
			for (int i = 0; i < fl.length; i++) {
				if (fl[i].toString().endsWith(".png")
						&& fl[i].toString().contains(ydh)) {
					fl[i].delete();
				}
			}
		}
	}

	class PaintView extends View {
		public Paint paint;
		private Canvas cacheCanvas;
		private Bitmap cachebBitmap;
		private Path path;

		public Bitmap getCachebBitmap() {
			return cachebBitmap;
		}

		public PaintView(Context context) {
			super(context);
			init();
		}

		private void init() {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(3);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			path = new Path();
			cachebBitmap = Bitmap.createBitmap(p.width, p.height,
					Config.ARGB_8888);
			cacheCanvas = new Canvas(cachebBitmap);
			cacheCanvas.drawColor(Color.WHITE);
		}

		// 清空签名
		public void clear() {
			if (cacheCanvas != null) {
				cur_x = 0;
				cur_y = 0;
				isDraw = false;
				paint.setColor(BACKGROUND_COLOR);
				cacheCanvas.drawPaint(paint);
				paint.setColor(Color.BLACK);
				cacheCanvas.drawColor(Color.WHITE);
				invalidate();
			}
		}

		// 设置填充状态
		public void setTianChong() {
			if (!tcstate) {
				paint.setStyle(Paint.Style.FILL);
				tcstate = true;
			} else {
				paint.setStyle(Paint.Style.STROKE);
				tcstate = false;
			}
		}

		// 进来时绘制历史签名
		public void start() {
			Bitmap dragimg = Bitmap.createScaledBitmap(startbitmap, p.width,
					p.height, true);
			cacheCanvas.drawBitmap(dragimg, 0, 0, paint);
			invalidate();
		}

		// 设置颜色
		public void setColor(int color, int alpha) {
			paint.setColor(color);
			paint.setAlpha(alpha);
		};

		// 设置画笔
		public void setHuaBi(int arg1) {
			hbzl.setText(hbary[arg1]);
			int color = sharedPreferences.getInt("color", Color.BLACK);
			int alpha = 255 - sharedPreferences.getInt("tmd", 0);
			setHuaBiKd(sharedPreferences.getInt("hbkd", 0));
			setColor(color, alpha);
			if (arg1 == 0) {

			} else if (arg1 == 1) {
				paint.setColor(Color.WHITE);
				setHuaBiKd(20);
			}
		}

		// 设置画笔宽度
		public void setHuaBiKd(int a) {
			paint.setStrokeWidth(a);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// canvas.drawColor(BRUSH_COLOR);

			canvas.drawBitmap(cachebBitmap, 0, 0, null);
			canvas.drawPath(path, paint);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {

			int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
			int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
			if (curW >= w && curH >= h) {
				return;
			}

			if (curW < w)
				curW = w;
			if (curH < h)
				curH = h;

			Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
					Bitmap.Config.ARGB_8888);
			Canvas newCanvas = new Canvas();
			newCanvas.setBitmap(newBitmap);
			if (cachebBitmap != null) {
				newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
			}
			cachebBitmap = newBitmap;
			cacheCanvas = newCanvas;
		}

		private float cur_x = 0;
		private float cur_y = 0;

		// private long currentTime = 0;
		// private long currentFirstTime = 0;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// isDraw = true;
			float x = event.getX();
			float y = event.getY();

			// "随笔", "直线", "矩形", "椭圆", "圆形","圆点", "橡皮"
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				// currentFirstTime = System.currentTimeMillis();
				cur_x = x;
				cur_y = y;
				if (hblx == 0) {
					path.moveTo(cur_x, cur_y);
				} else if (hblx == 1) {
					path.moveTo(cur_x, cur_y);
				}
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				if (hblx == 0) {
					path.quadTo(cur_x, cur_y, x, y);
					cur_x = x;
					cur_y = y;
				} else if (hblx == 1) {
					path.quadTo(cur_x, cur_y, x, y);
					cur_x = x;
					cur_y = y;
				}
				break;
			}

			case MotionEvent.ACTION_UP: {
				// currentTime = System.currentTimeMillis() - currentFirstTime;
				if (hblx == 0) {
					cacheCanvas.drawPath(path, paint);
					path.reset();
				} else if (hblx == 1) {
					cacheCanvas.drawPath(path, paint);
					path.reset();
				}

				break;
			}
			}

			invalidate();

			return true;
		}
	}
}
