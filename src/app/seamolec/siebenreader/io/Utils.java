package app.seamolec.siebenreader.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	public String SDCARD_STATE = Environment.getExternalStorageState();
	public String DIR_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
	public String DIR_APP_NAME = "siebenreader";
	public String OUTPUT_EPUB_FILE = "sieben.epub";
	public String DIR_OUTPUT = DIR_SDCARD + File.separator + DIR_APP_NAME; // /mnt/sdcard/siebenreader

	// create folder "siebenreader" on sd card
	public void createDirectory(String path, Context context) {
		if (Environment.MEDIA_MOUNTED.equals(SDCARD_STATE)) {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
				Log.i("Create", "Create dir : " + path);
				//Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "Error : SD CARD unmounted", Toast.LENGTH_SHORT).show();
		}
	}

	// delete folder "siebenreader" on sd card
	public void deleteDirectory(File dir, Context context) {
		if (Environment.MEDIA_MOUNTED.equals(SDCARD_STATE)) {
			
			Log.i("Delete", "Delete Previous Top : " + dir.getPath());
	        if (dir.isDirectory())
	        {
	            String[] children = dir.list();
	            for (int i = 0; i < children.length; i++) 
	            {
	               File temp =  new File(dir, children[i]);
	               if(temp.isDirectory())
	               {
	                   Log.i("Delete", "Delete Dir : " + temp.getPath());
	                   deleteDirectory(temp, context);
	               }
	               else
	               {
	                   Log.i("Delete", "Delete File : " + temp.getPath());
	                   boolean b = temp.delete();
	                   if(b == false)
	                   {
	                       Log.e("Delete", "Delete Fail");
	                   }
	               }
	            }

	            dir.delete();
	        }
			
		} else {
			Toast.makeText(context, "Error : SD CARD unmounted", Toast.LENGTH_SHORT).show();
		}
	}

	public void copyFile(String inputPath, String inputFile, String outputPath) {
		InputStream in = null;
		OutputStream out = null;
		try {
			
			in = new FileInputStream(inputPath + inputFile);
			out = new FileOutputStream(outputPath + inputFile);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;

			// write the output file
			out.flush();
			out.close();
			out = null;
			
			File from = new File(outputPath, inputFile);
			File to = new File(outputPath, OUTPUT_EPUB_FILE);
			from.renameTo(to);
			
		} catch (FileNotFoundException fnfe1) {
			Log.e("tag", fnfe1.getMessage());
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}

	}
}