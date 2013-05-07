package app.seamolec.siebenreader.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import app.seamolec.siebenreader.R;

public class DashboardActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dashboard_layout);
		
		// dashboard open book button
		Button btnOpenBook = (Button) findViewById(R.id.btn_open_book);
		
		// dashboard setting button
		Button btnSetting = (Button) findViewById(R.id.btn_setting);
				
		// dashboard help button
		Button btnHelp = (Button) findViewById(R.id.btn_help);
				
		// dashboard about button
		Button btnAbout = (Button) findViewById(R.id.btn_about);
		
		// listener when button open book clicked
		btnOpenBook.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), BrowseEpubActivity.class);
				startActivity(i);
				
			}
		});
		
		// listener when button setting clicked
		btnSetting.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(getApplicationContext(), SettingActivity.class);
						startActivity(i);
						
					}
		});
		
		// listener when button help clicked
		btnHelp.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(DashboardActivity.this)
				.setTitle("Көмек")
				.setMessage("- Кітап ашу : дисктен.epub extention бар файлды ашыңыз\n" +
							"- Көмек : бұл қосымшаны қалай пайдалану қажет\n" +
							"> келесі бетке өту үшін оңнан солға жүргізіңіз\n" +
							"> алдыңғы бетке өту үшін солдан оңға жүргізіңіз\n" +
							"- Біз туралы : осы қосымшаны жасаушы")
				.setPositiveButton("Жабу", null)
				.show();
			}
		});
		
		// listener when button about clicked
		btnAbout.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), AboutActivity.class);
				startActivity(i);
			}
		});
		
	}
	
}