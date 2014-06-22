package model.mapilelu.potable_001;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author Pilar
 *
 */
/**
 * DBHandler_model
 * 
 *
 */

public class DBHandler_model {
	
	
	// Version  
	private static final int DATABASE_VERSION = 1; 
	// Nombre de la BD
	private static final String DATABASE_NAME = "DBpotable001";
	// Nombre de la tabla a crear 
	private static final String TABLE_NAME = "Tablepotable001";
	
	// Nombres de los campos de la tabla 
    public static final String KEY_ROWID = "_id";//_id: un campo autoincrement
    public static final String KEY_NAME = "field_name";
	public static final String KEY_ON_OFF = "field_on_off";
	public static final String KEY_NOTES = "field_notes";
	public static final String KEY_LATITUDE = "field_latitude";
	public static final String KEY_LONGITUDE = "field_longitude";
	
	//creamos la tabla
	private final static String CREATE_TABLE =
			"create table " + TABLE_NAME + "( " 
			+ KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_NAME + " text, "
			+ KEY_ON_OFF + " text, "
			+ KEY_NOTES + " text, "
			+ KEY_LATITUDE + " text, "
			+ KEY_LONGITUDE + " text);";
		
	//declaramos un contexto
	private final Context context;
	//SQLiteOpenHelper
	//A helper class to manage database creation and version management.
	//declaramos un DBHelper
	private DBHelper dbh;
	//declaramos una base de datos de SQLite
	private SQLiteDatabase db;
	
	//constructor de la clase
	   public DBHandler_model(Context ctx){
	    	this.context= ctx;
	    }//DBHandler_model
			
	   //el metodo open devuelve un objeto DBHandler_model
	   public DBHandler_model open() throws SQLException{
	    	
	    	dbh= new DBHelper(context);
	    	db= dbh.getWritableDatabase();
	    	return this;  	
	    }//open
	   
	   public void close(){
	    	dbh.close();
	    }//close
	   
	   //la forma en la que guardar en la base de datos
	   //los nuevos registros
	   public long insertNew(Main_model m) { 
   		
	    	ContentValues cv = new ContentValues();
	    		cv.put(KEY_NAME, m.getFountainName());
	    		cv.put(KEY_ON_OFF, m.getIsItWorking().toString());
	    		cv.put(KEY_NOTES, m.getNotes());
	    		cv.put(KEY_LATITUDE, m.getLatitude());
	    		cv.put(KEY_LONGITUDE, m.getLongitude());
	    		
	    	 		
	    	return db.insert(TABLE_NAME, null, cv);				
	    }//insertNew
	   
	   //borrar un registro
	   public boolean delete(long rowId) { 
		 //No entiendo el funcionamiento
	    	return db.delete(TABLE_NAME, KEY_ROWID + "=" + rowId, null) > 0; 
	    }//delete
	   
	   //�qu� hace este m�todo?
	   //una consulta a la base de datos para sacar el listado en el list_layout.xml?
	   //por lo que parece, no s�lo consultan lo que quieren que sea visible
	   //en la lista, sino todo.
	   public Cursor query() {
	    	 	 Cursor mCursor = db.query(TABLE_NAME, 
	    			 new String[] {KEY_ROWID, 
	    	 			 KEY_NAME, KEY_ON_OFF, KEY_NOTES,
	    	 			 KEY_LATITUDE, KEY_LONGITUDE
	    	 			 },	    			   
	    			 null, null, null, null, null, null);    
	 
	    	if (mCursor != null) {    
	    		 mCursor.moveToFirst();  
	    	}   
	    	 return mCursor; 
	    	
	  	} //query
	  
	   public Cursor getItem(long rowId) throws SQLException { 
	    	Cursor c =
	    	db.query(true, TABLE_NAME, 
	    			new String[] {KEY_ROWID,
	    			KEY_NAME, KEY_ON_OFF, KEY_NOTES,
   	 			 	KEY_LATITUDE, KEY_LONGITUDE
   	 			 	}, 
	    			KEY_ROWID + " = "+rowId, 
	    			null, null, null, null, null); 
	    	//el n�mero de null en funci�n de qu�? si pongo 6, como los campos de arriba:
	    	//peta, no entiende query
	
	    	if (c != null) {
	    		c.moveToFirst(); 
	    	}
	    	return c;
	    }//getItem
	   
	   //m�todo para actualizar
	   public boolean refreshItem(long rowId, String nam, String on, String not,
			   String lat, String lng)
	   { 
		   String[] rows= {rowId+""};
		   
				//estos son logs para facilitar el debugging
				Log.e("BD", "RefreshItem");
			ContentValues args = new ContentValues();
			args.put(KEY_NAME, nam);
				Log.e("BD", nam);
			args.put(KEY_ON_OFF, on);
				Log.e("BD", on);
			args.put(KEY_NOTES, not);	
				Log.e("BD", not);
			args.put(KEY_LATITUDE, lat);
				Log.e("BD", lat);
			args.put(KEY_LONGITUDE, lng);
				Log.e("BD", lng);
				
			
				Log.e("BD", "update ==1"); 
			
			return db.update(TABLE_NAME, args, 
			KEY_ROWID+" =? ", rows)==1; 
			
	   }//refreshItem

	   //�para qu� ponemos este m�todo DBHelper?
	   //extends SQLiteOpenHelper: A helper class to manage database creation and version management.
	   private static class DBHelper extends SQLiteOpenHelper {

			public DBHelper(Context ctx) {
				//crea la BD				
				super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
			}//constructor DBHelper
			
			@Override
			//onCreate(SQLiteDatabase db)
			//Called when the database is created for the first time.
			public void onCreate(SQLiteDatabase db){
				//crea el esquema de tablas
				System.out.println(CREATE_TABLE);
				db.execSQL(CREATE_TABLE);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, 
									int vOld, int vNew){
				//no hacemos nada
			}	//onUpgrade	
		}//class DBHelper


	   public String getFountainName(Cursor c){
		   return(c.getString(1));
	   }
	   public String getIsItWorking(Cursor c){
		   return(c.getString(2));
	   }
	   
	   public String getNotes(Cursor c){
		   return(c.getString(3));
	   }
	   
	   public String getLatitude(Cursor c){
		   return(c.getString(4));
	   }
	   public String getLongitude(Cursor c){
		   return(c.getString(5));
	   }
	   
}//end
