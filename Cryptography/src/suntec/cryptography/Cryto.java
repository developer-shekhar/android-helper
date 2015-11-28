
package suntec.cryptography;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cryto {

	private final static int IV_LENGTH = 16; // Default length with Default 128
	// key AES encryption
	private final static int DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE = 1024;

	private final static String ALGO_RANDOM_NUM_GENERATOR = "SHA1PRNG";
	private final static String ALGO_SECRET_KEY_GENERATOR = "AES";
	private final static String ALGO_VIDEO_ENCRYPTOR = "AES/CBC/PKCS5Padding";
	private final static String EPUB_FOLDER = "/Users/suntec/Documents/LongHon/eBook Collected/eBook Collected/";
	static Cryto myCryto;
	@SuppressWarnings("resource")
	public static void encrypt(SecretKey key, AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		try {
			// byte[] iv = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
			// 0x07, 0x72, 0x6F, 0x5A, (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
			// 0x07, 0x72, 0x6F, 0x5A };
			// AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			Cipher c = Cipher.getInstance(ALGO_VIDEO_ENCRYPTOR);
			c.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			out = new CipherOutputStream(out, c);
			int count = 0;
			byte[] buffer = new byte[DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE];
			while ((count = in.read(buffer)) >= 0) {
				out.write(buffer, 0, count);
			}
		} finally {
			out.close();
		}
	}

	@SuppressWarnings("resource")
	public static void decrypt(SecretKey key, AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		try {
			// byte[] iv = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
			// 0x07, 0x72, 0x6F, 0x5A, (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
			// 0x07, 0x72, 0x6F, 0x5A };
			// AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			Cipher c = Cipher.getInstance(ALGO_VIDEO_ENCRYPTOR);
			c.init(Cipher.DECRYPT_MODE, key, paramSpec);
			out = new CipherOutputStream(out, c);
			int count = 0;
			byte[] buffer = new byte[DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE];
			while ((count = in.read(buffer)) >= 0) {
				out.write(buffer, 0, count);
			}
		} finally {
			out.close();
		}
	}

	public ArrayList<String> listFilesForFolder(final File folder) {

		ArrayList<String> myFileList = new ArrayList<>();

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {

				if (fileEntry.getName().trim().equalsIgnoreCase(".DS_Store"))
					continue;
				
				if (fileEntry.getName().trim().endsWith(".epub")){
					myFileList.add(fileEntry.getName().trim());
					
				}
				// myFileList.add(fileEntry.getPath());
			
				System.out.println(fileEntry.getName());
			}
		}
		return myFileList;
	}

	public static void main(String[] args) {
		
	
		
		myCryto = new Cryto();
		
		//myCryto.extractAllCoverImages();
		 //System.exit(0);
		
		 // final File folder = new File("/Volumes/SuntecHD/testlab/ecryptor");
		final File folder = new File("/Users/suntec/Documents/LongHon/eBook Collected/eBook Collected");

		ArrayList<String> fileList = myCryto.listFilesForFolder(folder);

		

		//File inFile = new File("/Volumes/SuntecHD/testlab/ecryptor/Gum-on-the-gate.mp4");
		//File outFile = new File("/Volumes/SuntecHD/testlab/ecryptor/encry-Gum-on-the-gate.mp4");
		//File outFile_dec = new File("/Volumes/SuntecHD/testlab/ecryptor/decry-Gum-on-the-gate.mp4");

		try {
			SecretKey key = KeyGenerator.getInstance(ALGO_SECRET_KEY_GENERATOR).generateKey();

			byte[] keyData = key.getEncoded();
			// if you want to store key bytes to db so its just how to
			// //recreate back key from bytes array
			SecretKey key2 = new SecretKeySpec(keyData, 0, keyData.length, ALGO_SECRET_KEY_GENERATOR);

			byte[] iv = new byte[IV_LENGTH];
			SecureRandom.getInstance(ALGO_RANDOM_NUM_GENERATOR).nextBytes(iv); // If
			// storing
			// separately
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

			String sourceRootPath = "/Users/suntec/Documents/LongHon/eBook Collected/eBook Collected";
			String encryRootPath = "/Users/suntec/Documents/LongHon/eBook Collected/eBook Collected/epubtestEncry";

			for (String fileName : fileList) {
				
				File sourceFile = new File(sourceRootPath + "/" + fileName.trim());
				System.out.println(sourceFile);
				File destFile = new File(encryRootPath + "/" + fileName.trim());

				Cryto.encrypt(key, paramSpec, new FileInputStream(sourceFile), new FileOutputStream(destFile));
				// Cryto.decrypt(key2, paramSpec, new FileInputStream(outFile),
				// new FileOutputStream(outFile_dec));
			}

			//store key
			myCryto.writeIVTofile(iv, keyData);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	public void writeIVTofile(byte[] iv, byte[] keyData){
		
		// Write IV
        FileOutputStream fs;
		try {
			fs = new FileOutputStream(new File("/Volumes/SuntecHD/android_projects/LongHon/fileIVBytes"));
			BufferedOutputStream bos = new BufferedOutputStream(fs);
	        bos.write(iv);
	        bos.close();
	        
	        fs = new FileOutputStream(new File("/Volumes/SuntecHD/android_projects/LongHon/fileKeyBytes"));
			BufferedOutputStream bos2 = new BufferedOutputStream(fs);
			bos2.write(keyData);
			bos2.close();
	        
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public byte[] getKeyData(){
		
		// Read IV
        byte[] fileData = new byte[IV_LENGTH];
        DataInputStream dis = null;

        try {
			dis = new DataInputStream(new FileInputStream(new File("fileKeyBytes")));
			  dis.readFully(fileData);
			  if (dis != null) {
		            dis.close();
		        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
        
        return fileData;
	}
	
	
public byte[] getIVData(){
		
		// Read IV
        byte[] fileData = new byte[IV_LENGTH];
        DataInputStream dis = null;

        try {
			dis = new DataInputStream(new FileInputStream(new File("fileIVBytes")));
			  dis.readFully(fileData);
			  if (dis != null) {
		            dis.close();
		        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
        
        return fileData;
	}
	
	
	
	
	
	
	public void extractAllCoverImages(){
		
		final File folder = new File("/Users/suntec/Documents/LongHon/eBook Collected/eBook Collected/");
		String destinationFolder = "extractedEpubCover";
			ArrayList<String> fileList = myCryto.listFilesForFolder(folder);
			
			for (String fileName : fileList) { 
				
				extractcoverimage(fileName,destinationFolder);
			}
			
			
	}
	
	
	public  String extractcoverimage(String ePubName, String destinationFolder) {

		System.out.println("ePubName="+ePubName);
		
		try {
			int BUFFER = 2048;
			String inputZip = EPUB_FOLDER + ePubName;
			
			File sourceZipFile = new File(inputZip);
			File unzipDestinationDirectory = new File(EPUB_FOLDER + destinationFolder);
			unzipDestinationDirectory.mkdir();
			String coverImageName =  ePubName.replace(".epub", ".jpg");
			System.out.println("++++"+ePubName);
			File destFile = new File(unzipDestinationDirectory, coverImageName);
			
			if (!destFile.exists()) {
				ZipFile zipFile;
				zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);

				// Process each entry
				

				ZipEntry entry = zipFile.getEntry("OEBPS/images/cover.jpg");
				if (entry == null)
					entry = zipFile.getEntry("OEBPS/image/cover.jpg");
				
				String currentEntry = entry.getName();
				currentEntry.length();
				File destinationParent = destFile.getParentFile();
				destinationParent.mkdirs();

				BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
				int currentByte;
				// buffer for writing file
				byte data[] = new byte[BUFFER];

				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();

				zipFile.close();

			}

			System.out.println(destFile.getAbsolutePath());
			return destFile.getAbsolutePath();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return EPUB_FOLDER + "coverdefault.jpg";
	}

}