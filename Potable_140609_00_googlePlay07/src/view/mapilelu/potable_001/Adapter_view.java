package view.mapilelu.potable_001;


import mapilelu.potable_001.R;
import model.mapilelu.potable_001.DBHandler_model;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/*
 * En la clase Adapter_view.java
 * gestionamos el contenido
 * de lo que aparece en la lista,
 * en el Cursor
 * 
 * el adapter te indica donde quieres 
 * instanciar el objeto del holder y con qué formato
 * */

public class Adapter_view extends CursorAdapter {
	//declaramos los objetos de Activity, Cursor y de nuestro DBHandler
	//�Qu� Activity? no estamos dando ning�n nombre de clase del paquete controller.
	private Activity act;
	//el cursor recorre la base de datos
	private Cursor cur;
	//un objeto de nuestro modelo de manejador de base de datos
	private DBHandler_model dbh;
	
	//constructor
	public Adapter_view(Activity a, Cursor c){
		super (a,c);
		this.act=a;
		this.cur=c;
	}

	@Override
	public void bindView(View row_view, Context context, Cursor cursor) {
		// reutiliza una vista ya existente
		/*
		 *  le indicamos el contexto, que es la actividad.
		 *  el contexto es lo que necesita la base de datos 
		 *  para mostrarse.
		 *   le pasas la actividad a la que tiene que mandarte los datos
		 * */
		Holder_view holder_adapter=(Holder_view)row_view.getTag();
		dbh=new DBHandler_model(act);
		//rellenamos con los datos
		//lamamos al holder, 
		//al metodo que me rellena los campos de la fila (populateFrom()
		
		holder_adapter.populateFrom(cursor, dbh);
	
	}

	@Override
	// Genera una vista nueva
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		
		//infla la vista de la actividad
		LayoutInflater inflater=act.getLayoutInflater();
		//infla la fila
		View row_adapter=inflater.inflate(R.layout.row_layout, parent, false);
		//
		Holder_view holder_adapter=new Holder_view(row_adapter);
		//etiquetas la vista con el objeto
		row_adapter.setTag(holder_adapter);
		//retorna la fila
		return(row_adapter);
	}

}//class Adapter_view
