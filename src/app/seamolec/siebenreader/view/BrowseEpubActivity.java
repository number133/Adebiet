package app.seamolec.siebenreader.view;

import java.io.File;
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import app.seamolec.siebenreader.R;
import app.seamolec.siebenreader.io.Utils;
import app.seamolec.siebenreader.io.EpubExtractor;

public class BrowseEpubActivity extends ListActivity {
	
	private File mCurrentNode = null;
	private File mLastNode = null;
	private File mRootNode = null;
	private ArrayList<File> mFiles = new ArrayList<File>();
	private BrowseEpubCustomAdapter mAdapter = null;
	private Utils utils = null;
	private ProgressDialog progressDialog;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_main);
        mAdapter = new BrowseEpubCustomAdapter(this, R.layout.browse_list_row, mFiles);
	    setListAdapter(mAdapter);
	    if (savedInstanceState != null) {
	    	mRootNode = (File)savedInstanceState.getSerializable("root_node");
	    	mLastNode = (File)savedInstanceState.getSerializable("last_node");
	    	mCurrentNode = (File)savedInstanceState.getSerializable("current_node");
	    }
	    
	    utils = new Utils();
	    File dir = new File(utils.DIR_OUTPUT);
		utils.deleteDirectory(dir, BrowseEpubActivity.this);
		utils.createDirectory(utils.DIR_OUTPUT, BrowseEpubActivity.this);
	    
	    refreshFileList();
    }
    
    private void refreshFileList() {
		if (mRootNode == null) mRootNode = new File(Environment.getExternalStorageDirectory().toString());
		if (mCurrentNode == null) mCurrentNode = mRootNode; 
		mLastNode = mCurrentNode;
		File[] files = mCurrentNode.listFiles();
		mFiles.clear();
		mFiles.add(mRootNode);
		mFiles.add(mLastNode);
		if (files!=null) {
			for (int i = 0; i< files.length; i++) mFiles.add(files[i]);
		}
		mAdapter.notifyDataSetChanged();
	}
    
    @Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("root_node", mRootNode);
		outState.putSerializable("current_node", mCurrentNode);
		outState.putSerializable("last_node", mLastNode);
		super.onSaveInstanceState(outState);
	} 
    
    /**
     * Listview on click handler.
     */
	@Override
	public void onListItemClick(ListView parent, View v, int position, long id){   
		final File f = (File) parent.getItemAtPosition(position);
		if (position == 1) {
			if (mCurrentNode.compareTo(mRootNode)!=0) {
				mCurrentNode = f.getParentFile();
				refreshFileList();
			}
		} else if (f.isDirectory()) {
			mCurrentNode = f;
			refreshFileList();
		} else {
			if(f.getName().endsWith(".epub")){
				//Log.i("info", f.getParent()); // /mnt/sdcard/siebenreader
				//Log.i("info", f.getName()); // epub file name from sd card
				//Log.i("info", utils.DIR_OUTPUT + File.separator); // /mnt/sdcard/siebenreader/
				progressDialog = ProgressDialog.show(BrowseEpubActivity.this, "Loading", "Please wait");
				new Thread() {
					public void run() {
						try{
							utils.copyFile(f.getParent() + File.separator, f.getName(), utils.DIR_OUTPUT + File.separator); // rename epub file to sieben.epub and copy to /mnt/sdcard/siebenreader/
							
							EpubExtractor epubExtract = new EpubExtractor();
							epubExtract.unzip(utils.DIR_OUTPUT + File.separator + utils.OUTPUT_EPUB_FILE, utils.DIR_OUTPUT + File.separator); // extract file /mnt/sdcard/siebenreader/sieben.epub
							//Log.i("info", "file : " + utils.DIR_OUTPUT + File.separator + utils.OUTPUT_EPUB_FILE); 
							//Log.i("info", "dest : " + utils.DIR_OUTPUT + File.separator);
							
							Intent i = new Intent(getApplicationContext(), EpubViewerActivity.class);
							startActivity(i);
							
						}catch (Exception e) {
							Log.e("tag", e.getMessage());
						}
						progressDialog.dismiss();
					}
				}.start();	
			}
			else{
				Toast.makeText(this, "Not Epub File! Please choose another one.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}