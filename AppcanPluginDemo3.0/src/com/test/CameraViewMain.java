package com.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import com.camera.ImageThumbnail;
import com.camera.LogcatHelper;

import Decoder.BASE64Encoder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraViewMain extends Activity implements OnClickListener {

	Intent cameraIntent = null;
	Uri imageUri = null;
	private Button reset_pic;
	private Button save_pic;
	private Button cancle_pic;
	private ImageView photoshow;
	Bitmap bitmap = null;
	String imagepath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(EUExUtil.getResLayoutID("plugin_uexcamera_view"));

		// LogcatHelper.getInstance(this).start();

		reset_pic = (Button) findViewById(EUExUtil.getResIdID("reset_pic"));
		save_pic = (Button) findViewById(EUExUtil.getResIdID("save_pic"));
		cancle_pic = (Button) findViewById(EUExUtil.getResIdID("cancle_pic"));
		photoshow = (ImageView) findViewById(EUExUtil.getResIdID("photoshow"));

		reset_pic.setOnClickListener(this);
		save_pic.setOnClickListener(this);
		cancle_pic.setOnClickListener(this);

		cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imageUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "/workupload.jpg"));
		// 指定照片保存路径（SD卡），workupload.jpg为一个临时文件，每次拍照后这个图片都会被替换
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(cameraIntent, 1);
	}

	@SuppressWarnings("resource")
	@SuppressLint({ "SdCardPath", "NewApi" })
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = null;
		String basestrpic = "";
		if (v.getId() == EUExUtil.getResIdID("reset_pic")) {
			cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			imageUri = Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory(),
							"/workupload.jpg"));
			// 指定照片保存路径（SD卡），workupload.jpg为一个临时文件，每次拍照后这个图片都会被替换
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(cameraIntent, 1);

		}
		if (v.getId() == EUExUtil.getResIdID("save_pic")) {
			if(bitmap == null){
				Toast.makeText(CameraViewMain.this, "请先拍照", Toast.LENGTH_LONG).show();
				return;
			}
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			FileOutputStream b = null;
			String filename = Environment.getExternalStorageDirectory()
					+ "/photopath/";
			File file = new File(filename);
			if (!file.exists()) {
				file.mkdirs();
			}
			String fileName = filename + "cache" + ".png";
			

			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//
//					//String string = null;
//					//ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//					//if (bitmap != null) {
//						//bitmap.compress(CompressFormat.PNG, 100, bStream);
//						//byte[] bytes = bStream.toByteArray();
//						//string = Base64.encodeToString(bytes, Base64.DEFAULT);
//
//						//Intent it = new Intent();
//						//it.putExtra("imgpath", filename);
//						//it.putExtra("basestrpic", string);
//						//setResult(Activity.RESULT_OK, it);
//						//string = null;
//						//finish();
//					//} else {
//					//	finish();
//					//}
//				}
//			}).start();

			// 将Bitmap转换成字符串

			// // 开始按600*300缩放
			// int width = bitmap.getWidth();
			// int height = bitmap.getHeight();
			// // 计算缩放比例
			// float scaleWidth = ((float) 200) / width;
			// float scaleHeight = ((float) 300) / height;
			// // 取得想要缩放的matrix参数
			// Matrix matrix = new Matrix();
			// matrix.postScale(scaleWidth, scaleHeight);
			// bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
			// true);
			//
			// try {
			// b = new FileOutputStream(fileName);
			// bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
			// // 转base64字符串
			// baos = new ByteArrayOutputStream();
			// bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			// byte[] photoBytes = baos.toByteArray();
			// if (photoBytes != null) {
			// BASE64Encoder encoder = new BASE64Encoder();
			// basestrpic = encoder.encode(photoBytes);
			// new FileOutputStream(fileName).write(photoBytes);
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// } finally {
			// try {
			// b.flush();
			// b.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			//
			// }

			 Intent it = new Intent();
			 it.putExtra("imgpath", fileName);
			 //it.putExtra("basestrpic", basestrpic);
			 setResult(Activity.RESULT_OK, it);
			 finish();

		}
		if (v.getId() == EUExUtil.getResIdID("cancle_pic")) {
			CameraViewMain.this.finish();
			// finish();
			// return;
		}

	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bitmap = null;
		//Log.d("jochen", "onDestroy()=="+bitmap);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			Bitmap camorabitmap = BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory()
					+ "/workupload.jpg");
			if (null != camorabitmap) {
				// 下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。
				// int scale = ImageThumbnail.reckonThumbnail(
				// camorabitmap.getWidth(), camorabitmap.getHeight(), 500,
				// 600);
				// bitmap = ImageThumbnail.PicZoom(camorabitmap,
				// camorabitmap.getWidth() / scale,
				// camorabitmap.getHeight() / scale);
				
				bitmap = ImageThumbnail.PicZoom(camorabitmap, 600, 800);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				// camorabitmap.recycle();
				// 将处理过的图片显示在界面上
//				Toast.makeText(
//						CameraViewMain.this,
//						"width=" + bitmap.getWidth() + ";height="
//								+ bitmap.getHeight(), Toast.LENGTH_LONG).show();
				photoshow.setImageBitmap(bitmap);

			}

		}
	}

}
