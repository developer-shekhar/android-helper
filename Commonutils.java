package com.longhorn.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Commonutils
{


	private static String Login="login";
    private static String profile="profile";
    private static final String EPUB_FOLDER = Environment.getExternalStorageDirectory()+File.separator+"epubtest";
    
    public static void setTypeFace(Context context,String fontfile,TextView... textview)
    {
        Typeface tf =Typeface.createFromAsset(context.getAssets(), fontfile);

        for(TextView txtview:textview)
        {
            txtview.setTypeface(tf);

        }
    }



    public static void setTypeFace(Context context,String fontfile,Button...textview)
    {
        Typeface tf =Typeface.createFromAsset(context.getAssets(), fontfile);

        for(Button txtview:textview)
        {
            txtview.setTypeface(tf);

        }
    }


    public static void setTypeFace(Context context,String fontfile,EditText...textview)
    {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), fontfile);

        for(EditText txtview:textview)
        {
            txtview.setTypeface(tf);

        }
    }



    private String extractcoverimage(String inputZip, String destinationFolder)
	{


try{
int BUFFER = 2048;
File sourceZipFile = new File(inputZip);
File unzipDestinationDirectory = new File(EPUB_FOLDER+File.separator+destinationFolder);
unzipDestinationDirectory.mkdir();
File destFile = new File(unzipDestinationDirectory, "cover.jpg");
if(!destFile.exists())
{	ZipFile zipFile;
zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);

// Process each entry




ZipEntry entry =  zipFile.getEntry("OEBPS/images/cover.jpg");
if(entry==null)
	 entry =  zipFile.getEntry("OEBPS/image/cover.jpg");
String currentEntry = entry.getName();



File destinationParent = destFile.getParentFile();
destinationParent.mkdirs();

	BufferedInputStream is = new BufferedInputStream(
			zipFile.getInputStream(entry));
	int currentByte;
	// buffer for writing file
	byte data[] = new byte[BUFFER];

	FileOutputStream fos = new FileOutputStream(destFile);
	BufferedOutputStream dest = new BufferedOutputStream(fos,
			BUFFER);

	while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
		dest.write(data, 0, currentByte);
	}
	dest.flush();
	dest.close();
	is.close();


zipFile.close();

}


return destFile.getAbsolutePath();

}
catch (Exception e) {
	e.printStackTrace();
}
	

return EPUB_FOLDER+"/coverdefault.jpg";
}


}
