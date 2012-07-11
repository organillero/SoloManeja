package mx.ferreyra.solomaneja.persistencia;



import static mx.ferreyra.solomaneja.recursos.Recursos.TABLE_LOG;
import static mx.ferreyra.solomaneja.recursos.Recursos.TABLE_RTE;
import static mx.ferreyra.solomaneja.recursos.Recursos.TL_ID_RTE;
import static mx.ferreyra.solomaneja.recursos.Recursos.*;
import static mx.ferreyra.solomaneja.recursos.Recursos.TL_LNG;
import static mx.ferreyra.solomaneja.recursos.Recursos.TL_TIME;
import static mx.ferreyra.solomaneja.recursos.Recursos.TL_TIPO;
import static mx.ferreyra.solomaneja.recursos.Recursos.TRTE_ISROUTE;
import static mx.ferreyra.solomaneja.recursos.Recursos.TRTE_TIME_FIN;
import mx.ferreyra.solomaneja.recursos.Recursos;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper{

	private Context context;
	private String dbName;

	private  static final String CREATE_TABLE1 = "CREATE TABLE " +
			TABLE_RTE + " ("+
			TRTE_TIME_INI + "  UNSIGNED BIG INT, " + 
			TRTE_TIME_FIN  + " UNSIGNED BIG INT, " +
			TRTE_ISROUTE + " BOOLEAN);";

	private  static final String CREATE_TABLE2 = "CREATE TABLE " +
			TABLE_LOG + " ("+
			TL_ID_RTE + " BIGNINT, " + 
			TL_LAT + " TEXT, " + 
			TL_LNG  + " TEXT, " +
			TL_TIPO + " INTEGER, " +
			TL_TIME  + " TEXT, "+
			TL_CONSEC  + " INTEGER );" 

			;


			public MyDBHelper(Context context, String dbName, CursorFactory factory, int version) {
				super(context, dbName, factory, version);

				this.dbName = dbName;
				this.context = context;

			}

			@Override
			public void onCreate(SQLiteDatabase db) {

				try {			
					db.execSQL(CREATE_TABLE1);
					db.execSQL(CREATE_TABLE2);
				} catch (Exception e) {
					Log.e(Recursos.TAG, "MyDbHelper Oncreate, execpcion al crear las tablas" + e.getMessage());
				}



			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion ) {

				//db.execSQL("drop table if exists " + Recursos.TABLE_CARRITO);
				context.deleteDatabase(dbName);
				onCreate(db);
			}



}
