package com.jpbeauty.jpbeauty_s1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class FileUtilities {

	private final String LOGTAG = "File utilities -- >";
	private String SDPATH;

	public String getSDPath() {
		return SDPATH;
	}

	public FileUtilities() {
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	private File createFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	public File createFolder(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	public void copyAssets(Context context, String destFolderName) {
		AssetManager assetManager = context.getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		for (String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {
				if (filename.toLowerCase().contains("beauty")) {

					File outFile = new File(getSDPath() + destFolderName,
							filename);
					if (!outFile.exists()) {
						in = assetManager.open(filename);
						out = new FileOutputStream(outFile);
						copyFile(in, out);
						in.close();
						in = null;
						out.flush();
						out.close();
						out = null;
					}
				}
			} catch (IOException e) {
				Log.e("tag", "Failed to copy asset file: " + filename, e);
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

}
