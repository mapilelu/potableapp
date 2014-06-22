package controller.mapilelu.potable_001;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;

import mapilelu.potable_001.R;
import model.mapilelu.potable_001.DBHandler_model;
import model.mapilelu.potable_001.Main_model;
import model.mapilelu.potable_001.Main_model.Working;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Se cuelga si no hay conexi�n al guardar el formulario
 * http://stackoverflow.com/questions/22703462/google-map-app-is-crashing-2-ways1if-there-is-no-intenet-connection-2-if-gps 
 * 
 * */

public class FormActivity_controller 
	extends Activity 
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
	
	///este atributo es un wrapper y permite el tipo primitivo long 
	//y también el NULL, que es lo que tendremos cuando sea crear 
	//nuevo registro
	private Long mRowId;
	//objeto de nuestra clase DBHandler_model
	private DBHandler_model dbh;
	//elementos del form_layout
	private TextView mLatitudeView;
	private TextView mLongitudeView;
	private EditText name_form_activity;
	private RadioGroup works_rg_form_activity;
	private EditText notes_form_activity;
	//opciones del radiogroup
	private String works_string_form_activity;
	private Working works_Working_form_activity;
		
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		dbh = new DBHandler_model(this);
		//despliega el layout (form_layout.xml)
		setContentView(R.layout.form_layout);
        //para que no gire la pantalla cuando pones el movil en modo landscape
		//y as� no tener que redefinir los layouts para que se vean bien cuando
		//cambies la orientaci�n de tu dispositivo
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
//		if(!WIFI_SERVICE.equals(true)){
//			Toast.makeText(this, R.string.nowificonnection, Toast.LENGTH_SHORT).show();
//		}
//		if (!REQUEST.equals(true)){
//			Toast.makeText(this, R.string.nogpsconnection, Toast.LENGTH_SHORT).show();
//		}
		//llamamos al método launchForm que despliega el formulario
		launchForm();
		//iniciamos mRowId (lo cual significaaaa... ???????????)
		mRowId = savedInstanceState != null
				? savedInstanceState.getLong(DBHandler_model.KEY_ROWID)
						: null;			
	
	}//onCreate
	
	private void launchForm(){
		//los elementos de nuestro form_layout.xml
		mLatitudeView = (TextView)findViewById(R.id.latitude_view);
		mLongitudeView = (TextView)findViewById(R.id.longitude_view);
		name_form_activity = (EditText) findViewById(R.id.name_form_layout);
		works_rg_form_activity = (RadioGroup) findViewById(R.id.rdg_form_layout);
		notes_form_activity = (EditText) findViewById(R.id.notes_form_layout);
		
		if(mRowId!=null){
			setRowIdFromIntent();
			refreshFields();
		}		
	}//launchForm
	
	public void onConnected(Bundle connectionHint) {
        mLocationClient.requestLocationUpdates(
                REQUEST,
                this);  // LocationListener
    }
	//http://stackoverflow.com/questions/22703462/google-map-app-is-crashing-2-ways1if-there-is-no-intenet-connection-2-if-gps
	@Override
	public void onLocationChanged(Location location) {
		
		String latitude_saveState = String.valueOf(mLocationClient.getLastLocation().getLatitude());
		String longitude_saveState = String.valueOf(mLocationClient.getLastLocation().getLongitude());
    
	}//onLocationChanged
    /**
     * Callback called when disconnected from GCore. Implementation of {@link ConnectionCallbacks}.
     */
    public void onDisconnected() {
        // vamos a pantalla principal
    	Intent i = (new Intent (this, MainActivity_controller.class));
    	startActivity(i);
    	Toast.makeText(this, "onDisconnected", Toast.LENGTH_SHORT).show();
    }
    /**
     * Implementation of {@link OnConnectionFailedListener}.
     */
    public void onConnectionFailed(ConnectionResult result) {
    	// vamos a pantalla principal
//    	Intent i = (new Intent (this, MainActivity_controller.class));
//    	startActivity(i);
    	Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }
    	
	private void refreshFields() {
		
		if(mRowId != null){
			//
			Cursor cur = dbh.getItem(mRowId);
			//
			cur.moveToFirst();
			//los campos de la base de datos
			name_form_activity.setText(cur.getString(cur.getColumnIndex(DBHandler_model.KEY_NAME)));
			notes_form_activity.setText(cur.getString(cur.getColumnIndex(DBHandler_model.KEY_NOTES)));
			works_string_form_activity = cur.getString(cur.getColumnIndex(DBHandler_model.KEY_ON_OFF));

			if (works_string_form_activity.equals("DOESWORK")){
				works_rg_form_activity.check(R.id.rdg_form_layout);
				works_Working_form_activity = Working.DOESWORK;
			}
			if(works_string_form_activity.equals("TAP")) {
				works_rg_form_activity.check(R.id.rdg_form_layout);
				works_Working_form_activity = Working.TAP;
			}
			else if (works_string_form_activity.equals("OTHER")) {
				works_rg_form_activity.check(R.id.rdg_form_layout);
				works_Working_form_activity = Working.OTHER;
			}
			mLatitudeView.setText(cur.getString(cur.getColumnIndex(DBHandler_model.KEY_LATITUDE)));
			mLongitudeView.setText(cur.getString(cur.getColumnIndex(DBHandler_model.KEY_LONGITUDE)));

		
			cur.close();
		}
		
	}//refreshFields

	private void setRowIdFromIntent() {
		//�por qu� == null?
		if(mRowId == null){
			//No entiendo lo del Bundle
			Bundle extras = getIntent().getExtras();
			//
			mRowId = extras != null
					?extras.getLong(DBHandler_model.KEY_ROWID)
							: null;
		}
		
	}//setRowIdFromIntent
	
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(DBHandler_model.KEY_ROWID, mRowId);  
	} //onSaveInstanceState

	public void saveState(){
		
		//los campos de la base de datos
		String name_saveState = name_form_activity.getText().toString();
		
		switch(works_rg_form_activity.getCheckedRadioButtonId()){
		
		case R.id.on_form_layout:
			works_Working_form_activity = Working.DOESWORK;
			works_string_form_activity = "DOESWORK";
			break;
		case R.id.tap_form_layout:
			works_Working_form_activity = Working.TAP;
			works_string_form_activity = "TAP";
			break;
		case R.id.other_form_layout:
			works_Working_form_activity = Working.OTHER;
			works_string_form_activity = "OTHER";
			break;
		
		}//switch works

		String notes_saveState = notes_form_activity.getText().toString();
		
		String latitude_saveState = "";
		String longitude_saveState = "";
		
		if(mLocationClient.isConnected()){
			//TODO El quid de la cuesti�n. aqu� capturamos la localizaci�n que nos da google maps.
			latitude_saveState = String.valueOf(mLocationClient.getLastLocation().getLatitude());
			longitude_saveState = String.valueOf(mLocationClient.getLastLocation().getLongitude());
		}
		
		else{
			Context ctx= getApplicationContext();
			Toast.makeText(ctx, R.string.nogpsconnection, Toast.LENGTH_SHORT).show();
			latitude_saveState = "No connection";
			longitude_saveState = "No connection";
		}
		
		
		//si mRowId es nulo, hay que crear un nuevo item
		//si tiene un valor, hay que actualizar un item
		//
		if (mRowId == null){
			long id =
					dbh.insertNew
					(new Main_model(name_saveState, works_Working_form_activity, notes_saveState, latitude_saveState, longitude_saveState));
					dbh.refreshItem(id, name_saveState,	works_string_form_activity, notes_saveState, latitude_saveState, longitude_saveState);
					
					if(id>0){
						mRowId = id;
					}
			
		}//if (mRowId == null)
		else{
			
			dbh.refreshItem(mRowId, name_saveState,
							works_string_form_activity, notes_saveState,
							latitude_saveState, longitude_saveState);

		}//else
	}//saveState
	
	//***************************************************
	//////
	////// M�todo para el tratamiento del bot�n guardar
	//////
	//***************************************************
	
	//----------------------------
	/////de form_layout.xml
	//-----------------------------
		
	public void onClickButtonSave(View v) { 
		
		//salva estado
		saveState();
		//aparece un mensaje informando de la opción elegida
  		Context ctx= getApplicationContext();
		Toast.makeText(ctx, R.string.saving, Toast.LENGTH_SHORT).show();
		//establece el Result
		setResult(RESULT_OK);
		//vuelve a la actividad que hizo el IntentForResult
		finish();
		
	}//onClickButtonSave
	
	//M�todos del ciclo de vida de la aplicaci�n
	protected void onResume(){
		//si se ha recuperado??
		super.onResume();
		setUpLocationClientIfNeeded();
        mLocationClient.connect();
		dbh.open();
		setRowIdFromIntent();
		refreshFields();
	}//onResume
	
	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    getApplicationContext(),
                    this,  // ConnectionCallbacks
                    this); // OnConnectionFailedListener
        }
		
	}

	protected void onPause(){
		super.onPause();
		if (mLocationClient != null) {
            mLocationClient.disconnect();}
		dbh.close();
	}//onPause
	
	public void onDestroy(){
		super.onDestroy();
		dbh.close();
	}//onDestroy


}//Fin
