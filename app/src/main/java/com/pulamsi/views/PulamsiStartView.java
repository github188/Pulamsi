package com.pulamsi.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;

public class PulamsiStartView extends View {
	private Bitmap bitmap;
	private Bitmap bitmap1;
	private MyThread thread;
	private float rx = 0;

	public PulamsiStartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PulamsiStartView(Context context) {
		super(context);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.start_bac);
		bitmap1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.start_line);
		if (Math.abs(bitmap.getWidth() / 2) > Math.abs(getWidth() / 2)) {
			rx = Math.abs(bitmap.getWidth() / 2) - Math.abs(getWidth() / 2);
		} else {
			rx = Math.abs(getWidth() / 2) - Math.abs(bitmap.getWidth() / 2);
		}
		rx += (int) (70 * PulamsiApplication.ScreenDensity + 0.5f);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		canvas.drawBitmap(bitmap, (getWidth() / 2) - (bitmap.getWidth() / 2),
				(getHeight() / 2) -(int) (20 * PulamsiApplication.ScreenDensity + 0.5f), paint);
		canvas.drawBitmap(bitmap1, rx, (getHeight() / 2)
				- (bitmap1.getHeight() / 2)-(int) (20 * PulamsiApplication.ScreenDensity + 0.5f), paint);
		if (thread == null) {
			thread = new MyThread();
			thread.start();
		}

	}

	class MyThread extends Thread {

		@Override
		public void run() {

			while (true) {
				rx += 3;
				if (rx > getWidth() / 2 + bitmap.getWidth() / 2
						- (int) (30 * PulamsiApplication.ScreenDensity + 0.5f)) {
					break;
				}
				postInvalidate();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
