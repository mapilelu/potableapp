package controller.mapilelu.potable_001;
/**
 * copiando de Potable_140318_03
 * */

import mapilelu.potable_001.R;
import model.mapilelu.potable_001.DBHandler_model;
import model.mapilelu.potable_001.Main_model;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity_controller 
	extends FragmentActivity 
	implements
	ConnectionCallbacks,
    OnConnectionFailedListener,
    LocationListener,
    OnMyLocationButtonClickListener 
    
    /**
     * Esta actividad muestra marcadores de nuestros registros de fuentes
     * As� como nuestra localizaci�n actual.
     * 
     * This demo shows how GMS Location can be used to check for changes to the users location.  The
     * "My Location" button uses GMS Location to set the blue dot representing
     *  the users location. To track changes to the users location on the map,
     *   we request updates from the {@link LocationClient}.
     * 
     * */
	
{
	
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private TextView mMessageView;
		
	// These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	//colores de los marcadores
	//http://developer.android.com/reference/com/google/android/gms/maps/model/BitmapDescriptorFactory.html
	public static final float HUE_BLUE=240.0f;
	public static final float HUE_RED=0.0f;
	public static final float HUE_VIOLET=270.0f;
	//t�tulo que aparecer� en el marcador
	public String title;
	
	//variables double para longitud y latitud
	public double longitude;
	public double latitude;
	
	//objetos de nuestro modelo y cursor de base de datos
	private DBHandler_model dbh;
	private Cursor cursor;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//desplegamos nuestro map_layout
		setContentView(R.layout.map_layout);
		//abrimos nuestra base de datos		
				dbh = new DBHandler_model(this);
		        dbh.open();
//		mMessageView = (TextView) findViewById(R.id.message_text);
		//mostramos el mapa
//		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);		
		
//		Intent i;
//		i=getIntent();
//		long aux;
		//recogemos el id del objeto de la lista que solicit� el men� contextual
//		aux=i.getLongExtra("identificator", 0);
		//Debemos usar DBHandler_model en vez de Main_model, como sugiri� Ra�l.
		        //En Main_model no hay referencia al _id.
		//Usamos el constructor de la clase DBHandler_Model,
		//que es este:
		////constructor de la clase
		   //public DBHandler_model(Context ctx){
		    //	this.context= ctx;
		    //}//DBHandler_model
		//�en vez de (null) habr�a que poner otra cosa, supongo
//		DBHandler_model fd = new DBHandler_model(null);         				
		//cogemos el elemento de la lista
//        fd=(DBHandler_model) dbh.getItem(aux);
        
//        latitude=Double.parseDouble(fd.getLatitude(cursor));
//        longitude=Double.parseDouble(fd.getLongitude(cursor));
        
        
//		//a�adimos los marcadores
//		addMarkersToMap();
		   //hacemos zoom
//		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),10));
	}//onCreate
//	
	private void addMarkersToMap() {
		   
		   cursor = dbh.query();
		   
		   do{
			   latitude=Double.parseDouble(dbh.getLatitude(cursor));
			   longitude=Double.parseDouble(dbh.getLongitude(cursor));
			   title= dbh.getFountainName(cursor);

			   
			   
			   if(dbh.getIsItWorking(cursor).equals("DOESWORK")){
			   
				   mMap.addMarker(new MarkerOptions()
				    .title(title)
				    .icon(BitmapDescriptorFactory.defaultMarker(HUE_BLUE))
				    .position(new LatLng(latitude, longitude)));
				   
			   } if(dbh.getIsItWorking(cursor).equals("TAP")){
			   
				   mMap.addMarker(new MarkerOptions()
				    .title(title)
				    .icon(BitmapDescriptorFactory.defaultMarker(HUE_VIOLET))
				    .position(new LatLng(latitude, longitude)));
	 
			   }  else if(dbh.getIsItWorking(cursor).equals("OTHER")){
				   
				   mMap.addMarker(new MarkerOptions()
				    .title(title)
				    .icon(BitmapDescriptorFactory.defaultMarker(HUE_RED))
				    .position(new LatLng(latitude, longitude)));
			   }
		   }while(cursor.moveToNext());
	    }//addMarkersToMap
	
	 @Override
	    protected void onResume() {
	        super.onResume();
	        setUpMapIfNeeded();
	        setUpLocationClientIfNeeded();
	        mLocationClient.connect();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        if (mLocationClient != null) {
	            mLocationClient.disconnect();
	        }
	    }

	    private void setUpMapIfNeeded() {
	        // Do a null check to confirm that we have not already instantiated the map.
	        if (mMap == null) {
	            // Try to obtain the map from the SupportMapFragment.
	            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
	                    .getMap();
	            // Check if we were successful in obtaining the map.
	            if (mMap != null) {
	                mMap.setMyLocationEnabled(true);
	                mMap.setOnMyLocationButtonClickListener(this);
	                //una vez vemos el mapa, ponemos los marcadores
	                addMarkersToMap();
	                
	            }
	        }
	    }

	    private void setUpLocationClientIfNeeded() {
	        if (mLocationClient == null) {
	            mLocationClient = new LocationClient(
	                    getApplicationContext(),
	                    this,  // ConnectionCallbacks
	                    this); // OnConnectionFailedListener
	        }
	    }

	    /**
	     * Button to get current Location. This demonstrates how to get the current Location as required
	     * without needing to register a LocationListener.
	     */
	    public void showMyLocation(View view) {
	        if (mLocationClient != null && mLocationClient.isConnected()) {
	            String msg = "Location = " + mLocationClient.getLastLocation();
	            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	        }
	        else{
	        	Toast.makeText(getApplicationContext(), "Please, wait", Toast.LENGTH_SHORT).show();
	        }
	    }

	    /**
	     * Implementation of {@link LocationListener}.
	     */
	    @Override
	    public void onLocationChanged(Location location) {
//	        mMessageView.setText("Location = " + location);
	    }

	    /**
	     * Callback called when connected to GCore. Implementation of {@link ConnectionCallbacks}.
	     */
	    @Override
	    public void onConnected(Bundle connectionHint) {
	        try {
				mLocationClient.requestLocationUpdates(
				        REQUEST,
				        this);  // LocationListener
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	    /**
	     * Callback called when disconnected from GCore. Implementation of {@link ConnectionCallbacks}.
	     */
	    @Override
	    public void onDisconnected() {
	        // Do nothing
	    	Toast.makeText(this, "onDisconnected", Toast.LENGTH_SHORT).show();
	    }

	    /**
	     * Implementation of {@link OnConnectionFailedListener}.
	     */
	    @Override
	    public void onConnectionFailed(ConnectionResult result) {
	        // Do nothing
	    	Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
	    }

	    @Override
	    public boolean onMyLocationButtonClick() {
	        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
	        // Return false so that we don't consume the event and the default behavior still occurs
	        // (the camera animates to the user's current position).
	        return false;
	    }

}//fin
