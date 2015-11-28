package com.longhorn.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import com.longhorn.utils.ReaderHelper;
import com.longhorn.utils.Singleton;

/**
 * Created by suntec on 14/11/15.
 */
public class SHDatabaseProvider {

    private final Context mContext;

    private SQLiteDatabase mDb;
    private SHDatabaseHandler mDbHelper;
    protected static final String TAG = "SHDatabaseProvider";

     public SHDatabaseProvider(Context context) {

         this.mContext = context;

        mDbHelper = new SHDatabaseHandler(mContext);
try {
	createDatabase();
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
    }


    public SHDatabaseProvider createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }


    public SHDatabaseProvider open()
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
           
        }
        return this;
    }


    public void close()
    {
        mDbHelper.close();
    }

    
    public SHDatabaseProvider openWritableDatabase()
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getWritableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
           
        }
        return this;
    }


    
    public ArrayList<EPubContent> getSearchedRecord(String searchString)
    {
    	
    	//open database for reading
    	open();
        ArrayList<EPubContent> ePubContents = new ArrayList<EPubContent>();

        String wSearchString = (searchString != "")? " and Content_Name like  \"%"+searchString+"%\"" : "";

      	
        String whereClause = " where 1 = 1  "+wSearchString;

        try
        {
            String sql ="SELECT * FROM Contents  "+whereClause+" group by Content_Name" ;
            //Log.d(TAG, sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {

                if (mCur.moveToFirst()) {
                    do {
                    	
                    	EPubContent ePub = new EPubContent(mCur.getString(1), mCur.getString(2), mCur.getInt(3), mCur.getInt(4), mCur.getString(5),mCur.getInt(0), mCur.getInt(7) );
                         ePubContents.add(ePub);
                        Log.d("getAllBooks()", ePub.toString());
                    } while (mCur.moveToNext());
                }


            }
            return ePubContents;
        }
        catch (Exception mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }finally {
        	  //close database
            close();
		}
        
      
    }

    public boolean activateApp(String deviceID){
    	
    	
    	openWritableDatabase();
    	
		 try
	        {
	            String sql ="INSERT INTO app_activation (device_id,date_time) VALUES ('"+deviceID+"',strftime('%s','now'))";
	            //Log.d(TAG, sql);
	        	mDb.execSQL(sql);
	            return true;
	          
	        }
	        catch (Exception mSQLException)
	        {
	            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
	            
	            //throw mSQLException;
	            return false;
	            
	        }finally {
	        	  //close database
	            close();
			}
	        
    		
    }
    
    
    public boolean validatePascode(String pascode){
    	
    	
    	
    	//open database for reading
    	open();

        try
        {
            String sql ="SELECT count(*) FROM app_secure where pascode =  '"+pascode+"'" ;
            //Log.d(TAG, sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {

                if (mCur.moveToFirst()) {
                     if(mCur.getInt(0) > 0){
                    	 return true;
                     }else{
                    	 return false;
                     }
                	
                }else {
                	
                	return false;
                }


            }else {
            	return false;
            }
            
        }
        catch (Exception mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }finally {
        	  //close database
            close();
		}
        
    	
    }
    
    public boolean isAppActivated(String deviceID){
    	
    	
    	//open database for reading
    	open();

        try
        {
            String sql ="SELECT count(*) FROM app_activation where device_id =  '"+deviceID+"'" ;
            //Log.d(TAG, sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {

                if (mCur.moveToFirst()) {
                     if(mCur.getInt(0) > 0){
                    	 return true;
                     }else{
                    	 return false;
                     }
                	
                }else {
                	
                	return false;
                }


            }else {
            	return false;
            }
            
        }
        catch (Exception mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }finally {
        	  //close database
            close();
		}
        
    	
    }
    
    
    
    public Boolean removeFromFavorite(int recordID){
    	
		openWritableDatabase();
	
		 try
	        {
	            String sql ="UPDATE Contents SET is_favorite = 0  where ID = "+recordID;
	            //Log.d(TAG, sql);
	        	mDb.execSQL(sql);
	           
	          
	        }
	        catch (Exception mSQLException)
	        {
	            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
	            throw mSQLException;
	            
	            
	        }finally {
	        	  //close database
	            close();
			}
	        
		
		
	return true;
}

    
    public Boolean addToFavirite(int recordID){
    	
    		openWritableDatabase();
    	
    		 try
    	        {
    	            String sql ="UPDATE Contents SET is_favorite = 1  where ID = "+recordID;
    	            //Log.d(TAG, sql);
    	            	mDb.execSQL(sql);
    	           
    	            	return true;
    	           
    	          
    	        }
    	        catch (Exception mSQLException)
    	        {
    	            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
    	            throw mSQLException;
    	            
    	            
    	        }finally {
    	        	  //close database
    	            close();
    			}
    	        
    		
    		
    	
    }
    
    
    public ArrayList<EPubContent> getFavorites(String type, String languageID, String standardID)
    {
    	
    	
    	//open database for reading
    	open();
        ArrayList<EPubContent> ePubContents = new ArrayList<EPubContent>();

             String wCont = (type != "")? " and Content_Type = '"+type+"'" : "";

        String wLanId = (languageID != "") ? " and Language = "+languageID : "";
      //  String wStandard = (standardID != "") ? " and Standard = "+standardID : "";
        

        String whereClause = " where is_favorite = 1  "+wCont+wLanId;

        try
        {
            String sql ="SELECT * FROM Contents  "+whereClause;
            //Log.d(TAG, sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {

                if (mCur.moveToFirst()) {
                    do {

                    	EPubContent ePub = new EPubContent(mCur.getString(1), mCur.getString(2), mCur.getInt(3), mCur.getInt(4), mCur.getString(5),mCur.getInt(0), mCur.getInt(7) );
                         ePubContents.add(ePub);
                        Log.d("getAllBooks()", ePub.toString());
                    } while (mCur.moveToNext());
                }




            }
            return ePubContents;
        }
        catch (Exception mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }finally {
        	  //close database
            close();
		}
        
      
    }


    
    
    
    public ArrayList<EPubContent> getFillteredRecord(String type, String languageID, String standardID)
    {
    	
    	//open database for reading
    	open();
        ArrayList<EPubContent> ePubContents = new ArrayList<EPubContent>();

             String wCont = (type != "")? " and Content_Type = '"+type+"'" : "";

        String wLanId = (languageID != "") ? " and Language = "+languageID : "";
        String wStandard = (standardID != "") ? " and Standard = "+standardID : "";
       
        if(Singleton.getInstance().selectedLan != 1 && standardID.equalsIgnoreCase("7") ){
        	wStandard = " and ( Standard = "+standardID+" or Standard = 8) ";
        }

        String whereClause = " where 1 = 1  "+wCont+wLanId+wStandard;

        try
        {
            String sql ="SELECT * FROM Contents  "+whereClause;
            //Log.d(TAG, sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {

                if (mCur.moveToFirst()) {
                    do {

                        EPubContent ePub = new EPubContent(mCur.getString(1), mCur.getString(2), mCur.getInt(3), mCur.getInt(4), mCur.getString(5),mCur.getInt(0), mCur.getInt(7) );
                        ePubContents.add(ePub);
                        Log.d("getAllBooks()", ePub.toString());
                    } while (mCur.moveToNext());
                }




            }
            return ePubContents;
        }
        catch (Exception mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }finally {
        	  //close database
            close();
		}
        
      
    }


    
    public ArrayList<EPubContent> getFillteredRecord(String type, String languageID)
    {
    	//open database for reading
    	open();
        ArrayList<EPubContent> ePubContents = new ArrayList<EPubContent>();

             String wCont = (type != "")? " and Content_Type = '"+type+"'" : "";

        String wLanId = (languageID != "") ? " and Language = "+languageID : "";

        String whereClause = " where 1 = 1  "+wCont+wLanId;

        try
        {
            String sql ="SELECT * FROM Contents  "+whereClause;

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {

                if (mCur.moveToFirst()) {
                    do {

                    	EPubContent ePub = new EPubContent(mCur.getString(1), mCur.getString(2), mCur.getInt(3), mCur.getInt(4), mCur.getString(5),mCur.getInt(0), mCur.getInt(7) );
                          ePubContents.add(ePub);
                        Log.d("getAllBooks()", ePub.toString());
                    } while (mCur.moveToNext());
                }




            }
            return ePubContents;
        }
        catch (Exception mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }finally {
        	  //close database
            close();
		}
    }




    public ArrayList<EPubContent> getAllRecordsForStandard(int standardID)
    {
    			open();
        ArrayList<EPubContent> ePubContents = new ArrayList<EPubContent>();
        String wh = " and Standard = "+standardID;
        
        if(Singleton.getInstance().selectedLan != 1 && standardID == 7){
        	wh = " and (Standard = "+standardID+" or Standard = 8)";
        }

        try
        {
            
        	//String sql ="SELECT * FROM Contents where 1=1 "+wh+" and Language = "+Singleton.getInstance().selectedLan;
        	String sql ="SELECT * FROM Contents where 1=1 "+wh;

            Cursor mCur = mDb.rawQuery(sql, null);
            
            if (mCur!=null)
            {

                if (mCur.moveToFirst()) {
                    do {

                    	EPubContent ePub = new EPubContent(mCur.getString(1), mCur.getString(2), mCur.getInt(3), mCur.getInt(4), mCur.getString(5),mCur.getInt(0), mCur.getInt(7) );
                         ePubContents.add(ePub);
                        Log.d("getAllBooks()", ePub.toString());
                    } while (mCur.moveToNext());
                }




            }
            close();
            return ePubContents;
        }
        catch (Exception mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }






    public ArrayList<EPubContent> getAllRecords()
    {

        ArrayList<EPubContent> ePubContents = new ArrayList<EPubContent>();



        try
        {
            String sql ="SELECT * FROM Contents";

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur!=null)
            {

                if (mCur.moveToFirst()) {
                    do {

                    	EPubContent ePub = new EPubContent(mCur.getString(1), mCur.getString(2), mCur.getInt(3), mCur.getInt(4), mCur.getString(5),mCur.getInt(0), mCur.getInt(7) );
                         ePubContents.add(ePub);
                        Log.d("getAllBooks()", ePub.toString());
                    } while (mCur.moveToNext());
                }




            }
            return ePubContents;
        }
        catch (Exception mSQLException)
        {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

}
