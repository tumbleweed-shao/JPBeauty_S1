package com.jpbeauty.jpbeauty_s1;

import java.io.File;
import java.io.IOException;
import cn.domob.android.ads.DomobAdEventListener;
import cn.domob.android.ads.DomobAdManager.ErrorCode;
import cn.domob.android.ads.DomobAdView;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class JPBeautyMainActivity extends Activity {
	final String ROOTFOLDER = "BeautyEye/";
	LinearLayout lltop;
	LinearLayout ll2;
	ImageView largeIV;
	WallpaperManager wallpaperManager;
	Integer[] mImageIds;
	DomobAdView mAdview320x50;// AD

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jpbeauty_main);
		copyBeautyToSD();
		
		ll2 = (LinearLayout) findViewById(R.id.linebottom);
		lltop = (LinearLayout) findViewById(R.id.lineTop);
		lltop.post(new Runnable() {
			@Override
			public void run() {
				InitController();
			}
		});
		largeIV = (ImageView) findViewById(R.id.imageView1);
		String initPicPath = Environment
				.getExternalStorageDirectory().toString()
				+ "/"
				+ ROOTFOLDER
				+ "beauty_1.jpg";
		Bitmap bmp = BitmapFactory.decodeFile(initPicPath);
		largeIV.setImageBitmap(bmp);
		largeIV.setTag(initPicPath);
		wallpaperManager = WallpaperManager.getInstance(this);
		initGalarry();
		InitAD();
	}

	private void InitController() {
		Window window = getWindow();
		View currentView = window.findViewById(Window.ID_ANDROID_CONTENT);
		int mesureHeight = currentView.getMeasuredHeight();
		int measuredWidth = currentView.getMeasuredWidth();
		
		lltop.setLayoutParams(new RelativeLayout.LayoutParams(measuredWidth,
				(int) (mesureHeight * 0.85)));

		Button btnLink = (Button) findViewById(R.id.btnSetDesktop);
		btnLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String imageTag = (String) largeIV.getTag();
				Bitmap o = BitmapFactory
						.decodeFile(imageTag);
				Bitmap bitmap = Bitmap.createScaledBitmap(o, getScreenSize(JPBeautyMainActivity.this).getWidth(), getScreenSize(JPBeautyMainActivity.this).getHeight(), true); 
				try {
					wallpaperManager.setBitmap(bitmap);
					//finish();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Display getScreenSize(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay();
	}

	private void InitAD() {
		LinearLayout mAdContainer = (LinearLayout) findViewById(R.id.lineAD);
		mAdview320x50 = new DomobAdView(JPBeautyMainActivity.this,
				"56OJw3touNPeAuwIFK", "16TLu49vApI7YNU-pQtVn5Bk",
				DomobAdView.INLINE_SIZE_320X50);
		mAdview320x50.setKeyword("game");
		mAdview320x50.setUserGender("male");
		mAdview320x50.setUserBirthdayStr("2000-08-08");
		mAdview320x50.setUserPostcode("123456");
		mAdview320x50.setAdEventListener(new DomobAdEventListener() {

			@Override
			public void onDomobAdReturned(DomobAdView adView) {
				// Log.i("DomobSDKDemo", "onDomobAdReturned");
			}

			@Override
			public void onDomobAdOverlayPresented(DomobAdView adView) {
				// Log.i("DomobSDKDemo", "overlayPresented");
			}

			@Override
			public void onDomobAdOverlayDismissed(DomobAdView adView) {
				// Log.i("DomobSDKDemo", "Overrided be dismissed");
			}

			@Override
			public void onDomobAdClicked(DomobAdView arg0) {
				// Log.i("DomobSDKDemo", "onDomobAdClicked");
			}

			@Override
			public void onDomobAdFailed(DomobAdView arg0, ErrorCode arg1) {
				// Log.i("DomobSDKDemo", "onDomobAdFailed");
			}

			@Override
			public void onDomobLeaveApplication(DomobAdView arg0) {
				// Log.i("DomobSDKDemo", "onDomobLeaveApplication");
			}

			@Override
			public Context onDomobAdRequiresCurrentContext() {
				return JPBeautyMainActivity.this;
			}
		});
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mAdview320x50.setLayoutParams(layout);
		mAdContainer.addView(mAdview320x50);
	}

	private void copyBeautyToSD() {
		FileUtilities fu = new FileUtilities();
		fu.createFolder(ROOTFOLDER);
		fu.copyAssets(this, ROOTFOLDER);
	}

	private int getImageScale(String imagePath, int maxWidth, int maxHeight) {
		BitmapFactory.Options option = new BitmapFactory.Options();
		// set inJustDecodeBounds to true, allowing the caller to query the
		// bitmap info without having to allocate the
		// memory for its pixels.
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, option);

		int scale = 1;
		while (option.outWidth / scale >= maxWidth
				|| option.outHeight / scale >= maxHeight) {
			scale *= 2;
		}
		return scale;
	}
	private void initGalarry(){
		HorizontalScrollView gallery = (HorizontalScrollView) findViewById(R.id.gallery1);
		LinearLayout topLinearLayout = new LinearLayout(this);
		topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

		for (int count = 1; count <= new File(Environment
				.getExternalStorageDirectory().toString() + "/" + ROOTFOLDER)
				.list().length; count++) {
			final ImageView imageView = new ImageView(this);
			BitmapFactory.Options option = new BitmapFactory.Options();
			String currentImgPath = Environment.getExternalStorageDirectory()
					.toString() + "/" + ROOTFOLDER + "beauty_" + count + ".jpg";
			option.inSampleSize = getImageScale(currentImgPath, 160, 160);
			Bitmap bm = BitmapFactory.decodeFile(currentImgPath, option);
			imageView.setImageBitmap(bm);
			imageView.setTag(currentImgPath);
			imageView.setLayoutParams(new LinearLayout.LayoutParams((int)(getScreenSize(JPBeautyMainActivity.this).getWidth() * 0.2), (int)(getScreenSize(JPBeautyMainActivity.this).getHeight() * 0.15)));
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setPadding(0, 0, 0, 0);
			topLinearLayout.addView(imageView);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BitmapFactory.Options clickedImageOption = new BitmapFactory.Options();
					String clickedImagePath = (String) imageView.getTag();
					clickedImageOption.inSampleSize = getImageScale(
							clickedImagePath, largeIV.getWidth(),
							largeIV.getHeight());
					Bitmap clickedBm = BitmapFactory.decodeFile(
							clickedImagePath, clickedImageOption);
					largeIV.setImageBitmap(clickedBm);
					largeIV.setTag(clickedImagePath);
				}
			});
		}
		gallery.addView(topLinearLayout);
	}
}
