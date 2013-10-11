package ie.koala.sigapp.ui;

import com.google.analytics.tracking.android.EasyTracker;

import ie.koala.sigapp.simonkenyon.R;
import ie.koala.sigapp.util.GlobalObjects;
import ie.koala.sigapp.util.QRCode;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class QrCodeActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code);
		
		TextView textView = (TextView) findViewById(R.id.qr_code_text);
		ImageView imageView = (ImageView) findViewById(R.id.qr_code_image);
		
		String QrCodeText = GlobalObjects.getWhatToShare();
		Bitmap bitmap = QRCode.generateBitmap(QrCodeText);
		
		textView.setText(QrCodeText);
		imageView.setImageBitmap(bitmap);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (GlobalObjects.isAnalyticsEnabled()) {
			EasyTracker.getInstance(this).activityStart(this);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (GlobalObjects.isAnalyticsEnabled()) {
			EasyTracker.getInstance(this).activityStop(this);
		}
	}

}
