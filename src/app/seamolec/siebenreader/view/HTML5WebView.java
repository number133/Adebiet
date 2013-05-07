package app.seamolec.siebenreader.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import app.seamolec.siebenreader.R;
import app.seamolec.siebenreader.io.Settings;

public class HTML5WebView extends WebView {

	private Context mContext;
	private MyWebChromeClient mWebChromeClient;
	private View mCustomView;
	private FrameLayout mCustomViewContainer;
	private WebChromeClient.CustomViewCallback mCustomViewCallback;

	private FrameLayout mContentView;
	private FrameLayout mBrowserFrameLayout;
	private FrameLayout mLayout;
	private GestureDetector gDetector;

	static final String LOGTAG = "HTML5WebView";

	@SuppressLint("SetJavaScriptEnabled")
	private void init(Context context) {
		mContext = context;
		Activity a = (Activity) mContext;

		mLayout = new FrameLayout(context);

		mBrowserFrameLayout = (FrameLayout) LayoutInflater.from(a).inflate(
				R.layout.html5_custom_screen, null);
		mContentView = (FrameLayout) mBrowserFrameLayout
				.findViewById(R.id.main_content);
		mCustomViewContainer = (FrameLayout) mBrowserFrameLayout
				.findViewById(R.id.fullscreen_custom_content);

		mLayout.addView(mBrowserFrameLayout, COVER_SCREEN_PARAMS);

		mWebChromeClient = new MyWebChromeClient();
		setWebChromeClient(mWebChromeClient);

		setWebViewClient(new MyWebViewClient());

		// Configure the webview
		WebSettings s = getSettings();
		// s.setBuiltInZoomControls(true);
		// s.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
		// s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		// s.setUseWideViewPort(true);
		s.setLoadWithOverviewMode(true);
		s.setSavePassword(true);
		s.setSaveFormData(true);
		s.setJavaScriptEnabled(true);
		s.setAllowFileAccess(true);
		// s.setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");

		mContentView.addView(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gDetector.onTouchEvent(event) || super.onTouchEvent(event);
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gDetector = gestureDetector;
	}

	public HTML5WebView(Context context) {
		super(context);
		init(context);
	}

	public HTML5WebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public HTML5WebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public FrameLayout getLayout() {
		return mLayout;
	}

	public boolean inCustomView() {
		return (mCustomView != null);
	}

	public void hideCustomView() {
		mWebChromeClient.onHideCustomView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((mCustomView == null) && canGoBack()) {
				goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private class MyWebChromeClient extends WebChromeClient {
		private Bitmap mDefaultVideoPoster;
		private View mVideoProgressView;

		@Override
		public void onShowCustomView(View view,
				WebChromeClient.CustomViewCallback callback) {
			// Log.i(LOGTAG, "here in on ShowCustomView");
			HTML5WebView.this.setVisibility(View.GONE);

			// if a view already exists then immediately terminate the new one
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}

			mCustomViewContainer.addView(view);
			mCustomView = view;
			mCustomViewCallback = callback;
			mCustomViewContainer.setVisibility(View.VISIBLE);
		}

		@Override
		public void onHideCustomView() {

			if (mCustomView == null)
				return;

			// Hide the custom view.
			mCustomView.setVisibility(View.GONE);

			// Remove the custom view from its container.
			mCustomViewContainer.removeView(mCustomView);
			mCustomView = null;
			mCustomViewContainer.setVisibility(View.GONE);
			mCustomViewCallback.onCustomViewHidden();

			HTML5WebView.this.setVisibility(View.VISIBLE);

			// Log.i(LOGTAG, "set it to webVew");
		}

		@Override
		public Bitmap getDefaultVideoPoster() {
			// Log.i(LOGTAG, "here in on getDefaultVideoPoster");
			if (mDefaultVideoPoster == null) {
				mDefaultVideoPoster = BitmapFactory.decodeResource(
						getResources(), R.drawable.default_video_poster);
			}
			return mDefaultVideoPoster;
		}

		@Override
		public View getVideoLoadingProgressView() {
			// Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

			if (mVideoProgressView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				mVideoProgressView = inflater.inflate(
						R.layout.html5_video_loading, null);
			}
			return mVideoProgressView;
		}

		/*
		 * @Override public void onReceivedTitle(WebView view, String title) {
		 * ((Activity) mContext).setTitle(title); }
		 */

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			((Activity) mContext).getWindow().setFeatureInt(
					Window.FEATURE_PROGRESS, newProgress * 100);
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
		}
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i(LOGTAG, "shouldOverrideUrlLoading: " + url);
			// don't override URL so that stuff within iframe can work properly
			// view.loadUrl(url);
			return false;
		}
	}

	static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);

	
	public void update(Settings settings){
		updateFontSize(settings.getFontSize());
		updateBgColor(settings.getBgColor());
	}
	
	private void updateFontSize(int fontSize) {
		WebSettings webSettings = this.getSettings();
		switch (fontSize) {
		case 0:
			webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
			break;
		case 1:
			webSettings.setTextSize(WebSettings.TextSize.SMALLER);
			break;
		case 2:
			webSettings.setTextSize(WebSettings.TextSize.NORMAL);
			break;
		case 3:
			webSettings.setTextSize(WebSettings.TextSize.LARGER);
			break;
		case 4:
			webSettings.setTextSize(WebSettings.TextSize.LARGEST);
			break;
		}

	}

	private void updateBgColor(int bgColor){
		this.setBackgroundColor(bgColor);
	}
}