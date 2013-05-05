package app.seamolec.siebenreader.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.TOCReference;
import android.util.Log;
import app.seamolec.siebenreader.io.Utils;

public class EpubScanner {

	public ArrayList<String> tocTitle = new ArrayList<String>();
	public ArrayList<String> tocHref = new ArrayList<String>();
	private Utils utils;
	
	public void getTOC(List<TOCReference> tocReferences, int depth) {
		if (tocReferences == null) {
			return;
		}
		int i = 0;
		for (TOCReference tocReference : tocReferences) {
			
			tocTitle.add(tocReference.getTitle());
			tocHref.add(tocReference.getResource().getHref());

			Log.i("Title ", tocTitle.toString());
			Log.i("Href ", tocHref.get(i));
			i++;
			
			getTOC(tocReference.getChildren(), depth + 1);
		}
	}
	
	public String getEpubVersion(){
		utils = new Utils();
		String epubV3 = utils.DIR_OUTPUT + File.separator + "OEBPS" + File.separator;
		String epubV2 = utils.DIR_OUTPUT + File.separator;
		
		File file = new File(epubV3);
		if(file.exists()){
			return epubV3;
		}	
		return epubV2;
	}

}
