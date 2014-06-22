package controller.mapilelu.potable_001;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;

import mapilelu.potable_001.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//

//http://developer.android.com/google/play/licensing/adding-licensing.html#imports
/*
 * La clase MainActivity_controller
 * gestiona la actividad de inicio:
 * Botones New y View
 * 
 * */

//extendemos la interfaz Activity
public class MainActivity_controller extends Activity 
implements 
LocationListener, 
ConnectionCallbacks, 
OnConnectionFailedListener {
	
	 private LocationClient mLocationClient;
	    private TextView mMessageView;
	    
	    // These settings are the same as the settings for the map. They will in fact give you updates
	    // at the maximal rates currently possible.
	    private static final LocationRequest REQUEST = LocationRequest.create()
	            .setInterval(5000)         // 5 seconds
	            .setFastestInterval(16)    // 16ms = 60fps
	            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //despliega el main_layout.xls
        setContentView(R.layout.main_layout);
    	//para que no gire la pantalla cuando pones el movil en modo landscape
		//y as� no tener que redefinir los layouts para que se vean bien cuando
		//cambies la orientaci�n de tu dispositivo
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
   }//onCreate
    
    //***************************************************
  	//////
  	////// Metodos para el tratamiento de los botones
  	//////
  	//***************************************************
  	//--------------------------
  	/////de main_layout.xml
  	//----------------------------

    //botón NUEVO/NEW
  	public void onClickButtonNew(View v){
  		
  		//aparece un mensaje informando de la opción elegida
  		Context ctx= getApplicationContext();
		Toast.makeText(ctx, R.string.new_, Toast.LENGTH_SHORT).show();
  		
  		//declaramos la actividad que se disparará cuando pulsemos el botón
  		Intent i = new Intent (this, FormActivity_controller.class);
  		//lanzamos la actividad
  		startActivity(i);
	
  	}//onClickButtonNew
  	
  	//Botón LISTA DE FUENTES/VIEW
  	public void onClickButtonView(View v){
  		
  		Context ctx= getApplicationContext();
		Toast.makeText(ctx, R.string.view, Toast.LENGTH_SHORT).show();
  		
  		//declaramos la actividad que se disparará cuando pulsemos el botón
  		Intent i = new Intent (this, ListActivity_controller.class);
  		//lanzamos la actividad
  		startActivity(i);
  		
  	}//onClickButtonView	
  	
  	public void onClickAboutApp(View v){
  		
  		Context ctx= getApplicationContext();
		Toast.makeText(ctx, R.string.about, Toast.LENGTH_SHORT).show();
  		

  		//Desplegamos un layout con texto e im�genes
		setContentView(R.layout.about_layout);
    	//para que no gire la pantalla cuando pones el movil en modo landscape
		//y as� no tener que redefinir los layouts para que se vean bien cuando
		//cambies la orientaci�n de tu dispositivo
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  		
  	}//onClickAboutApp
  	
  	public void onClickButtonMap(View v){
  		Intent i = new Intent(this, MapActivity_controller.class);
  		startActivity(i);
  	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// vamos a pantalla principal
//    	Intent i = (new Intent (this, MainActivity_controller.class));
//    	startActivity(i);
    	Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(
                REQUEST,
                this);  // LocationListener
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location arg0) {
		String latitude_saveState = String.valueOf(mLocationClient.getLastLocation().getLatitude());
		String longitude_saveState = String.valueOf(mLocationClient.getLastLocation().getLongitude());
		
	}
    
}//fin
