package com.ting.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundView extends ImageView {

	private int minRadius = 0;

	public RoundView(Context context) {
		super(context);
	}

	public RoundView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Config.ARGB_8888, true);

		int w = getWidth(), h = getHeight();

		// // border
		// Paint paint = new Paint();
		// paint.setAntiAlias(true);
		// paint.setColor(Color.parseColor("#00000000"));
		//
		// canvas.drawCircle(w / 2 + 0.3f, h / 2 + 0.3f, 50, paint);
		// canvas.drawCircle(w / 2 + 0.3f, h / 2 + 0.3f, w / 2 + 1.5f, paint);

		int pic_radius = Math.min(w, h) / 2;
		Bitmap roundBitmap = getCroppedBitmap(bitmap, pic_radius);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
				Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		canvas.drawBitmap(roundBitmap, 0, 0, null);
	}

	public Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius * 2 || bmp.getHeight() != radius * 2)
			sbmp = Bitmap
					.createScaledBitmap(bmp, radius * 2, radius * 2, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
		
		paint.setAntiAlias(true);
//		paint.setStrokeJoin(join)
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#7f97d2"));

		if (minRadius > 0 && minRadius < radius) {
			paint.setStrokeWidth((float) (radius - minRadius-5));
			paint.setStyle(Style.STROKE);
			canvas.drawCircle(sbmp.getWidth() / 2 + 0.5f,
					sbmp.getHeight() / 2 + 0.5f, (radius - minRadius) / 2
							+ minRadius, paint);
		} else {
			paint.setStrokeWidth((float) 1.0);
			canvas.drawCircle(sbmp.getWidth() / 2 + 0.5f,
					sbmp.getHeight() / 2 + 0.5f, radius - 1, paint);
		}

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

	public int getMinRadius() {
		return minRadius;
	}

	public void setMinRadius(int minRadius) {
		this.minRadius = minRadius;
	}

}
