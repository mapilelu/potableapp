package controller.mapilelu.potable_001;

import mapilelu.potable_001.R;
import model.mapilelu.potable_001.DBHandler_model;
import model.mapilelu.potable_001.Main_model;
import model.mapilelu.potable_001.Main_model.Working;
import view.mapilelu.potable_001.Adapter_view;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.RadioGroup;

/*
 * Esta clase es la equivalente a la GUIAV8_Main.java.
 * Hace lo siguiente:
 * Infla el layout de la lista de fuentes ya registradas, en este caso,
 * Se despliega el men� contextual pinchando en una de las filas
 * Se gestiona el tratamiento de cada una de las opciones del men� (ver en mapa, editar y borrar)
 * */

public class ListActivity_controller extends ListActivity {
	
	
		
		//variables estáticas que nos sirven para
		//definir la actividad: o ver en mapa, o editar o borrar.
		private static final int ACTIVITY_MAP=1;
		private static final int ACTIVITY_EDIT=0;

		/**
		 * Aqu� realizamos el comienzo de la conexi�n con la base de datos
		 * de nuestro paquete model.
		 * 
		 * */
		//declaramos objetos
		//de la clase modelo manejador de la base de datos propio DBHandler_model.java
		private DBHandler_model dbh_main;
		//de la clase de la vista de adaptador propia: Adapter_view.java
		private Adapter_view adapter_main;
		//y del Cursor de la base de datos
		private Cursor model_main;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //despliega el layout de la lista de fuentes
	        setContentView(R.layout.list_layout);
	        //para que no gire la pantalla cuando pones el movil en modo landscape
			//y as� no tener que redefinir los layouts para que se vean bien cuando
			//cambies la orientaci�n de tu dispositivo
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        //instanciamos la base de datos
	        dbh_main = new DBHandler_model(this);
	        //abro la base de datos
	        dbh_main.open();
	        //para ir probando, insertamos uno nuevo cada vez que 
	        //se abre la actividad (estamos en onCreate)
//	        dbh_main.insertNew
//	        (new Main_model("Parque Paraiso", Working.DOESWORK, 
//	        		"Probando el onCreate de ListActivity_controller", null, null));
	        //	public Main_model(String na, 
			//Working w, SimpleDateFormat date, String no)
	        //instanciamos el m�todo para escribir los datos en la BD
	        writeData();
	        	        
	        //instanciamos el men� contextual
	        registerForContextMenu(getListView());
	    }//onCreate
	    
	    private void writeData(){
	    	//con un cursor, recorremos la lista 
	    	model_main = dbh_main.query();
	    	adapter_main = new Adapter_view(this, model_main);
	    	setListAdapter(adapter_main);
	   	}//writeData

	   private void editItem(Long id){
	    	Intent i = new Intent(this, FormActivity_controller.class);
	    	i.putExtra(DBHandler_model.KEY_ROWID, id);
	    	i.putExtra("identificator", id);
	    	startActivityForResult (i, ACTIVITY_EDIT);
	    }//editItem
	   
	   private void showMap(Long id){
		   Intent i = new Intent(this, MapActivity_controller.class);
		   i.putExtra(DBHandler_model.KEY_ROWID, id);
		   i.putExtra("identificator", id);
	    
		   //ahora usamos un startActivityForResult con una etiqueta
		   //el contenedor Android devolver� un segundo parametro con el resultado
	    	startActivity(i);
	   }//showMap
	   /**
	    * Este m�todo se disparar� cuando pulsemos con clic corto en un elemento de la lista.
	    * Mostrar� el formulario y el bot�n de guardar estar� inhabilidado.
	    * 
	    * */
	   private void viewItem(Long id){
		   //lanzamos una nueva actividad
		   Intent i = new Intent(this, FormActivity_controller.class);
		   //mandamos a la nueva actividad el id
	    	i.putExtra(DBHandler_model.KEY_ROWID, id);
	    	i.putExtra("identificator", id); 
	    	
		   //deshabilitamos la edici�n e interacci�n de los campos del formulario
		   final Button saveButton = (Button) findViewById(R.id.btn_save);
		   saveButton.setEnabled(false);
		   final EditText nameET = (EditText) findViewById(R.id.name_form_layout);
		   nameET.setEnabled(false);
		   final EditText notesET = (EditText) findViewById(R.id.notes_form_layout);
		   notesET.setEnabled(false);
		   final RadioGroup workingBG = (RadioGroup) findViewById(R.id.rdg_form_layout);
		   workingBG.setClickable(false);
 	
	   }//viewItem
	    
	    protected void onListItemClick(ListView l, View v, int position, long id){
	    	
	    	super.onListItemClick(l, v, position, id);
	    	
	    	//clic corto
	    	editItem(id);
	    }//onListItemClick
	    
	    public void onCreateContextMenu(ContextMenu menuC, View v, ContextMenuInfo mInfo){
	    	super.onCreateContextMenu(menuC, v, mInfo);
	    	//opciones que contiene el menú contextual
	    	MenuInflater mi = getMenuInflater();
	    	mi.inflate(R.menu.row_context_menu, menuC);
	    }//onCreateContextMenu
	    
	    //recibimos los resultados de la actividad (Intent) que habíamos
	    //lanzado desde aquí (FormActivity_controller)
	    //TODO hay que recibir tambi�n lo del mapa, si es que lo necesitamos...
	    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
	    	//este metodo recoge lo que el otro intent devuelve y luego vuelve con el que estaba
	    	super.onActivityResult(requestCode, resultCode, intent);
	    	//recarga la lista
	    	writeData();
	    }//onActivityResult
	    
	    //este método hay que cambiarlo enterito
	    //tiene que contener las opciones "editar, borrar y ver en mapa"
	    public boolean onContextItemSelected(MenuItem item){
	    	//invocamos un objeto AdapterContextMenuInfo
	    	AdapterContextMenuInfo info =
	    			//le pasamos la informaci�n del men�
	    			(AdapterContextMenuInfo) item.getMenuInfo();
	    	switch(item.getItemId()){
		    	case R.id.menu_delete:
		    		dbh_main.delete(info.id);
		    		writeData();
		    		return true;
		    	case R.id.menu_edit: 
		    		editItem(info.id);
		    		return true;  		
		    	case R.id.menu_map:
		    		showMap(info.id);
		    		return true;
	    	}//switch
	    	return super.onContextItemSelected(item);
	    }//onContextItemSelected
	    

	
}//fin
