package app.seamolec.siebenreader.view;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import app.seamolec.siebenreader.R;
import app.seamolec.siebenreader.io.Settings;
import app.seamolec.siebenreader.io.StorageHelper;

public class SettingActivity extends Activity {
	private StorageHelper storageHelper = null;
	private SeekBar fontSizeSeekBar = null;
	private TextView fontSizeNameTextView = null;
	private Settings appSettings;
	private Button colorButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setTitle(R.string.title_activity_setting);
		storageHelper = new StorageHelper(this);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	private void init() {
		initSettings();
		initFontSize();
		initColor();
	}
	
	private void initSettings(){
		appSettings = storageHelper.readSettings();
	}

	private void initFontSize() {
		fontSizeNameTextView = (TextView) findViewById(R.id.fontSizeName);
		fontSizeSeekBar = (SeekBar) findViewById(R.id.fontSizeSeekBar);

		fontSizeSeekBar.setMax(4);

		fontSizeSeekBar.setProgress(appSettings.getFontSize());

		updateFontSizeNameView(fontSizeNameTextView, appSettings.getFontSize());

		try {
			fontSizeSeekBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						public void onStopTrackingTouch(SeekBar arg0) {
						}

						public void onStartTrackingTouch(SeekBar arg0) {
						}

						public void onProgressChanged(SeekBar arg0,
								int progress, boolean arg2) {
							updateFontSizeNameView(fontSizeNameTextView,
									progress);
							appSettings.setFontSize(progress);
							storageHelper.writeSettings(appSettings);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateFontSizeNameView(TextView textView, int size) {
		switch (size) {
		case 0:
			textView.setText(WebSettings.TextSize.SMALLEST.toString());
			break;
		case 1:
			textView.setText(WebSettings.TextSize.SMALLER.toString());
			break;
		case 2:
			textView.setText(WebSettings.TextSize.NORMAL.toString());
			break;
		case 3:
			textView.setText(WebSettings.TextSize.LARGER.toString());
			break;
		case 4:
			textView.setText(WebSettings.TextSize.LARGEST.toString());
			break;
		}
	}

	private void initColor() {
		colorButton = (Button) findViewById(R.id.colorButton);
		colorButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pickColor();				
			}
		});
	}
	
	private void pickColor(){
		 AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, appSettings.getBgColor(), new OnAmbilWarnaListener() {
			 
	            public void onCancel(AmbilWarnaDialog dialog){
	            }
	 
	            public void onOk(AmbilWarnaDialog dialog, int color) {
	            	appSettings.setBgColor(color);
	            }
	        });
	        dialog.show();
	}
}
