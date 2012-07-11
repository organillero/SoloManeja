package mx.ferreyra.solomaneja.persistencia;

import java.util.ArrayList;
import java.util.Calendar;

import mx.ferreyra.solomaneja.pojo.MyPoint;
import mx.ferreyra.solomaneja.pojo.NewRoute;
import mx.ferreyra.solomaneja.recursos.Recursos;
import mx.ferreyra.solomaneja.recursos.Recursos.TIPO;
import mx.ferreyra.solomaneja.utils.Utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDB {
	private SQLiteDatabase db;
	private MyDBHelper dbhelper;

	private static final int DATABASE_VERSION = 9; 

	public MyDB (Context context){
		this.dbhelper = new MyDBHelper(context, Recursos.DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void open (){
		try {
			this.db =this.dbhelper.getWritableDatabase();
		} catch (Exception e) {
			this.db =this.dbhelper.getReadableDatabase();
		}

	}

	public void close(){
		this.db.close();
	}

	public boolean isNull(){
		return this.db==null? true:false;
	}

	public long startRoute(){

		
		ContentValues newTaskValue = new ContentValues();
		Calendar.getInstance().getTimeInMillis();
		newTaskValue.put(Recursos.TRTE_TIME_INI, Utils.getCurrentDate() );
		newTaskValue.put(Recursos.TRTE_ISROUTE, true);
		return this.db.insert(Recursos.TABLE_RTE, null, newTaskValue);


	}

	public long stopRoute(long id){

		ContentValues newTaskValue = new ContentValues();
		newTaskValue.put(Recursos.TRTE_TIME_FIN, Utils.getCurrentDate());

		return  this.db.update(Recursos.TABLE_RTE, newTaskValue, "rowid = '"+ id+ "'", null);

	}

	public void deleteRoute(Long id){
		this.db.delete(Recursos.TABLE_LOG, Recursos.TL_ID_RTE + "= '"+ id+ "'" , null);
		this.db.delete(Recursos.TABLE_RTE, "rowid = '"+ id+ "'" , null);//(Recursos.TABLE_CARRITO, null, newTaskValue);

	}


	public long addPoint (long idRoute,  Double lat, Double lng, TIPO tipo, String time, int consec){

		//String tmp = String.format("%3.6d", lat);

		ContentValues newTaskValue = new ContentValues();
		newTaskValue.put(Recursos.TL_ID_RTE, idRoute);
		newTaskValue.put(Recursos.TL_LAT, String.format("%3.6f", lat));
		newTaskValue.put(Recursos.TL_LNG, String.format("%3.6f", lng));
		newTaskValue.put(Recursos.TL_TIPO, tipo.getText());
		newTaskValue.put(Recursos.TL_TIME, time);
		newTaskValue.put(Recursos.TL_CONSEC, consec);
		return this.db.insert(Recursos.TABLE_LOG, null, newTaskValue);

	}

	public NewRoute getRoute (Long idRoute){

		String where=new String(Recursos.TL_ID_RTE +"='" + idRoute +"'" );
		Cursor c = this.db.query(Recursos.TABLE_LOG, null, where, null, null, null,null);

		
		NewRoute route = new NewRoute ();
		ArrayList <MyPoint> points = new ArrayList<MyPoint>();
		MyPoint point;

		if (c.moveToFirst()) {

			do {
				point = new MyPoint(); 
				point.lat = c.getString(c.getColumnIndex(Recursos.TL_LAT));
				point.lng =c.getString(c.getColumnIndex(Recursos.TL_LNG));
				point.tipo = c.getString(c.getColumnIndex(Recursos.TL_TIPO));
				point.fecha = c.getString(c.getColumnIndex(Recursos.TL_TIME));
				point.consecutivo = c.getInt(c.getColumnIndex(Recursos.TL_CONSEC));
				
				points.add(point);

			} while (c.moveToNext());
		}

		route.points=points;
		
		if (c != null && !c.isClosed()) {
			c.close();
		}
		

		where=new String(" rowid ='" + idRoute +"'" );
		c = this.db.query(Recursos.TABLE_RTE, null, where, null, null, null,null);
		
		if (c.moveToFirst()) {
			route.iniDate = c.getString(c.getColumnIndex(Recursos.TRTE_TIME_INI));
			route.endDate = c.getString(c.getColumnIndex(Recursos.TRTE_TIME_FIN));
		}
		
		if (c != null && !c.isClosed()) {
			c.close();
		}

		deleteRoute (idRoute);
		
		return route;
	}
	/*
	public void addProduct(Product product){

		int cantidad = getProductCantidad (product.sku);
		if (cantidad < 0)
			insertProduct(product);
		else
			updateProduct (product, cantidad +1);

		return;


	}


	public long insertProduct (Product product){

		//long time = Calendar.getInstance().getTimeInMillis();

		try {
			String cantidad= "1";//new String(new Integer(1).toString());
			ContentValues newTaskValue = new ContentValues();
			newTaskValue.put(Recursos.CARRITO_SKU, product.sku);
			newTaskValue.put(Recursos.CARRITO_OBJETO, Serialize.toByteAray(product));
			newTaskValue.put(Recursos.CARRITO_CANTIDAD, 1);
			//newTaskValue.put(Recursos.CARRITO_TOC,  time);
			//newTaskValue.put(Recursos.CARRITO_TOM, time);
			new Thread(new CarritoRunn(this.context, getUdid(this.context), "add_product", product.sku, cantidad )).start();
			return this.db.insert(Recursos.TABLE_CARRITO, null, newTaskValue);
		} catch (Exception e) {
			//Log.e(Recursos.TAG, "MyDB insertProduct, exepcion al insertar" + e.getMessage());
		}



		return -1l;
	}

	public long deleteProduct (String sku ){
		String cantidad= "0";//new Integer(0).toString();
		try {
			new Thread(new CarritoRunn(this.context, getUdid(this.context), "del_product", sku, cantidad )).start();
			return this.db.delete(Recursos.TABLE_CARRITO, "sku =" + sku, null);//(Recursos.TABLE_CARRITO, null, newTaskValue);
		} catch (Exception e) {
			//Log.e(Recursos.TAG, "MyDB insertProduct, exepcion al eliminar" + e.getMessage());
		}

		return-1l;
	}




	public long updateProduct (Product product, int cantidad){

		try {
			String cant= Integer.valueOf(cantidad).toString();
			long time = Calendar.getInstance().getTimeInMillis();

			ContentValues newTaskValue = new ContentValues();
			newTaskValue.put(Recursos.CARRITO_CANTIDAD, cantidad);
			newTaskValue.put(Recursos.CARRITO_TOM, time);

			long temp = this.db.update(Recursos.TABLE_CARRITO, newTaskValue, "sku =" + product.sku, null);

			if (temp != 0){
				new Thread(new CarritoRunn(this.context, getUdid(this.context), "edit_product", product.sku, cant)).start();
				return temp;
			}
			else 
				return insertProduct(product);




		} catch (Exception e) {
			//Log.e(Recursos.TAG, "MyDB insertProduct, exepcion al insertar" + e.getMessage());
		}

		return -1l;

	}

	public int getProductCantidad (String sku){

		String[] campos = new String[] {Recursos.CARRITO_CANTIDAD};
		String[] args = new String[] {sku};

		int ans= -1;

		Cursor c = this.db.query(Recursos.TABLE_CARRITO, campos, "sku = ?", args, null, null, null);

		if (c.moveToFirst()) {
			ans = c.getInt(c.getColumnIndex(Recursos.CARRITO_CANTIDAD));

		}

		return ans;
	}


	public Cursor getProducts (){

		Cursor c = this.db.query(Recursos.TABLE_CARRITO, null, null, null, null, null, Recursos.CARRITO_TOC +  " DESC");

		return c;
	}
	private String getUdid (Context context){
		return  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}
	 */
}
