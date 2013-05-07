package app.seamolec.siebenreader.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import app.seamolec.siebenreader.R;
import app.seamolec.siebenreader.io.StorageHelper;
import app.seamolec.siebenreader.io.Utils;
import app.seamolec.siebenreader.parser.EpubScanner;

public class EpubViewerActivity extends Activity {

	private Utils utils;
	private Book book;
	private Spine spine;
	private EpubScanner epubScan;
	private HTML5WebView mWebView;
	private String epubVersion;
	private int currrentPage, maxPage;
	private StorageHelper storageHelper = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		utils = new Utils();
		epubScan = new EpubScanner();
		currrentPage = 0;

		try {
			book = (new EpubReader())
					.readEpub(new FileInputStream(utils.DIR_OUTPUT
							+ File.separator + utils.OUTPUT_EPUB_FILE));
			setTitle(book.getTitle().toString());

			spine = book.getSpine();
			List<SpineReference> spineList = spine.getSpineReferences();
			maxPage = spineList.size();

			epubScan.getTOC(book.getTableOfContents().getTocReferences(), 0);
			mWebView = new HTML5WebView(this);
			mWebView.setGestureDetector(new GestureDetector(
					new CustomeGestureDetector()));

			epubVersion = epubScan.getEpubVersion();

			if (savedInstanceState != null) {
				mWebView.restoreState(savedInstanceState);
			} else {
				mWebView.loadUrl("file:///" + epubVersion
						+ spine.getResource(currrentPage).getHref());
			}

			storageHelper = new StorageHelper(this);
			mWebView.update(storageHelper.readSettings());

			setContentView(mWebView.getLayout());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Error", e.getMessage());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_ebook, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.menu_toc) {
			startActivity(new Intent(this, TOCActivity.class));
		} else if (item.getItemId() == R.id.menu_dashboard) {
			startActivity(new Intent(this, DashboardActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mWebView.saveState(outState);
	}

	@Override
	public void onStop() {
		super.onStop();
		mWebView.stopLoading();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.inCustomView()) {
				mWebView.hideCustomView();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public class CustomeGestureDetector extends SimpleOnGestureListener {
		int count = 0;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1 == null || e2 == null)
				return false;
			if (e1.getPointerCount() > 1 || e2.getPointerCount() > 1)
				return false;
			else {
				try {
					// go to next page
					if (e1.getX() - e2.getX() > 100
							&& Math.abs(velocityX) > 800) {

						Log.i("swipe", "right to left");
						Animation anim = AnimationUtils.loadAnimation(
								getApplicationContext(), R.anim.right_to_left);
						mWebView.startAnimation(anim);

						if (count < maxPage) {
							count++;
							mWebView.loadUrl("file:///"
									+ epubVersion
									+ spine.getResource(currrentPage + count)
											.getHref());
							setContentView(mWebView.getLayout());

							Log.i("buka halaman kanan : "
									+ (currrentPage + count), spine
									.getResource(currrentPage + count)
									.getHref());
						}
						return true;
					}
					// go to prev page
					else if (e2.getX() - e1.getX() > 100
							&& Math.abs(velocityX) > 800) {

						Log.i("swipe", "left to right");
						Animation anim = AnimationUtils.loadAnimation(
								getApplicationContext(), R.anim.left_to_right);
						mWebView.startAnimation(anim);

						if (count > 0) {
							count--;
							mWebView.loadUrl("file:///"
									+ epubVersion
									+ File.separator
									+ spine.getResource(currrentPage + count)
											.getHref());
							setContentView(mWebView.getLayout());

							Log.i("buka halaman kiri : "
									+ (currrentPage + count), spine
									.getResource(currrentPage + count)
									.getHref());
						}
						return true;
					}
				} catch (Exception e) {

				}
				return false;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (storageHelper != null) {
			mWebView.update(storageHelper.readSettings());
		}
	}
}