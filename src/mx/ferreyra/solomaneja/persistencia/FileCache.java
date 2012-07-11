package mx.ferreyra.solomaneja.persistencia;

import java.io.File;

import mx.ferreyra.solomaneja.utils.Utils;
import android.content.Context;

@SuppressWarnings("javadoc")
public class FileCache {

	private File cacheDir;

	public FileCache(Context context){

		this.cacheDir=context.getCacheDir();
		if(!this.cacheDir.exists())
			this.cacheDir.mkdirs();
	}

	public File getFile(String url){

		String filename = Utils.getSHA1(url);
		File f = new File(this.cacheDir, filename);
		return f;

	}

	public void clear(){
		File[] files=this.cacheDir.listFiles();
		for(File f:files)
			f.delete();
	}

}