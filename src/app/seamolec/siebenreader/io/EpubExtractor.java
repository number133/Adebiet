package app.seamolec.siebenreader.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.util.Log;

public class EpubExtractor {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void unzip(String inputZip, String destinationDirectory) {

		int BUFFER = 2048;
		List zipFiles = new ArrayList();
		File sourceZipFile = new File(inputZip);
		File unzipDestinationDirectory = new File(destinationDirectory);
		unzipDestinationDirectory.mkdir();
		
		ZipFile zipFile;
		// Open Zip file for reading
		try {
			
			zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);

			// Create an enumeration of the entries in the zip file
			Enumeration zipFileEntries = zipFile.entries();

			// Process each entry
			while (zipFileEntries.hasMoreElements()) {
				// grab a zip file entry
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();

				String currentEntry = entry.getName();
				Log.i("info entry : ", currentEntry);

				File destFile = new File(unzipDestinationDirectory,
						currentEntry);

				if (currentEntry.endsWith(".epub")) {
					zipFiles.add(destFile.getAbsolutePath());
				}

				// grab file's parent directory structure
				File destinationParent = destFile.getParentFile();

				// create the parent directory structure if needed
				destinationParent.mkdirs();

				try {
					// extract file if not a directory
					if (!entry.isDirectory()) {
						BufferedInputStream is = new BufferedInputStream(
								zipFile.getInputStream(entry));
						int currentByte;
						// establish buffer for writing file
						byte data[] = new byte[BUFFER];

						// write the current file to disk
						FileOutputStream fos = new FileOutputStream(destFile);
						BufferedOutputStream dest = new BufferedOutputStream(
								fos, BUFFER);

						// read and write until last byte is encountered
						while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
							dest.write(data, 0, currentByte);
						}
						dest.flush();
						dest.close();
						is.close();
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			zipFile.close();
			
			for (Iterator iter = zipFiles.iterator(); iter.hasNext();) {
				String zipName = (String) iter.next();
				unzip(zipName, destinationDirectory + File.separatorChar
						+ zipName.substring(0, zipName.lastIndexOf(".epub")));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}