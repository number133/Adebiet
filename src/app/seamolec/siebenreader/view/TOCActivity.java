package app.seamolec.siebenreader.view;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class TOCActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		WebView webView = new WebView(this);
		webView.loadUrl("file:///");
		setContentView(webView);
	}
	
}