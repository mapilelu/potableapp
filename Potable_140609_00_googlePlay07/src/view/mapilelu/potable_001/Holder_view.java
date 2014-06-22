package view.mapilelu.potable_001;

import mapilelu.potable_001.R;
import model.mapilelu.potable_001.DBHandler_model;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



/*
 * En la clase Holder_view.java
 * gestionamos lo que se va a ver en 
 * cada fila de la lista.
 * Está íntimamente relacionada con 
 * row_layout.xml
 * 
 * 
 * */

public class Holder_view {
	//declaramos lo que mostrará nuestro holder
	//En cada fila, tendremos el icono que indique si funciona o no
	//y el nombre de la fuente.
	//aún no estoy muy segura de si el nombre sería muy interesante
	//igual las coordenadas, o cualquier otra cosa, irían mejor
	
	private TextView name_holder=null;
	private ImageView icon_holder=null;

	
	
	Holder_view(View row_layout){
		//contiene las referencias
		//a elementos de la fila
		name_holder=(TextView)row_layout.findViewById(R.id.row_name);
		icon_holder=(ImageView)row_layout.findViewById(R.id.icon);
		
		
	}
	
	void populateFrom(Cursor c, DBHandler_model db){
		//logica para personalizar esas referencias
		name_holder.setText(db.getFountainName(c));
		//variable String para adjudicar uno de los iconos
		//verde si funciona
		//rojo si no funciona
		String works=db.getIsItWorking(c);
		if(works.equals("DOESWORK")){
			icon_holder.setImageResource(R.drawable.azul);
		}
		else if (works.equals("TAP")){
			icon_holder.setImageResource(R.drawable.singrifo);
		}
		else if (works.equals("OTHER")){
			icon_holder.setImageResource(R.drawable.rojo);
		}
		//TODO �cambiamos las im�genes?
	}
	
	
	

}
