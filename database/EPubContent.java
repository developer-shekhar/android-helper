package com.longhorn.database;

import java.io.Serializable;

/**
 * Created by suntec on 14/11/15.
 */
public class EPubContent implements Serializable {

       // public EPubContent(){}
        private String name, type,description;
        private  int languageId, standard, recordID, isFav;


    public int getIsFav() {
			return isFav;
		}

		public void setIsFav(int isFav) {
			this.isFav = isFav;
		}

	public EPubContent(String name, String type, int languageId, int standard,String description, int recordID, int isFav ){

                this.name = name;
                this.type = type;
                this.languageId = languageId;
                this.standard = standard;
                this.description = description;
                this.recordID = recordID;
                this.isFav = isFav;
    }

    public int getRecordID() {
    	
    	return this.recordID;
    }
    
    public String getName(){

         return this.name;
    }

    public String getType() {

            return this.type;
    }


    public String getDescription(){

         return this.description;
    }

    public  int getLanguageId(){

        return this.languageId;
    }

    public int getStandard() {

       return this.standard;
    }

    public String toString(){

        return " name = "+name+" type = "+this.type+" description = "+this.description+" languageId = "+this.languageId ;
    }

}
