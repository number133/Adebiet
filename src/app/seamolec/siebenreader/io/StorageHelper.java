package app.seamolec.siebenreader.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;

public class StorageHelper {
	private Context ctx;
	private static final String SETTINGS_FILE_NAME = "adebiet_settings";
	
	public StorageHelper(Context context){
		this.ctx = context;
	}
	
	public void writeSettings(Settings settings){
		FileOutputStream fos;
		try {
			fos = ctx.openFileOutput(SETTINGS_FILE_NAME,
					Context.MODE_PRIVATE);
			fos.write(settings.getFontSize());
			fos.write(settings.getBgColor());
			fos.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Settings readSettings(){
		Settings settings = new Settings();
		FileInputStream fis;
		int buff = -1;
		try {
			fis =  ctx.openFileInput(SETTINGS_FILE_NAME);
			buff = fis.read();
			if(buff>0){
			settings.setFontSize(buff);
			}
			buff = fis.read();
			if(buff>0){
			settings.setBgColor(buff);
			}
			fis.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return settings;
	}

}
