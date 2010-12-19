package de.android1.overlaymanager.demo;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.GestureDetector.OnDoubleTapListener;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;

import de.android1.overlaymanager.*;


public class DemoView extends MapActivity {

	MapView mapView;
	OverlayManager overlayManager;
	private Builder areYouSureDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mapView = (MapView) findViewById(R.id.mapview);
		//mapView.setBuiltInZoomControls(true);

		overlayManager = new OverlayManager(getApplication(), mapView);
		
		createOverlayWithListener();

	}

	public void createOverlayWithListener() {
	/*  With this OnOverlayGestureListener you can react on high-level user-interaction.
 	 * 	If the user makes a tap, double-tap or long-press on the map it will invoke
	 *	the appropriate listener-method and will pass two interesting parameter:
	 *	GeoPoint: the point where the user hit the map.
	 *  ManagedOverlayItem: if the user also hit a marker, otherwise this will be null.
     */
	   
		
		 final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 //builder = new AlertDialog.Builder(this);

		 ManagedOverlay managedOverlay = overlayManager.createOverlay("listenerOverlay", getResources().getDrawable(R.drawable.marker));
		//here we call the GeoHelper Class which helps us print our markers
		for (int i = 0; i < 40; i = i + 3) {
			managedOverlay.createItem(GeoHelper.geopoint[i], "Item" + i);
		}
		managedOverlay.setOnOverlayGestureListener(new ManagedOverlayGestureDetector.OnOverlayGestureListener() {


			public boolean onZoom(ZoomEvent zoom, ManagedOverlay overlay) {
				return false;
			}
			
			//if()
			//
			//
			
			public boolean onDoubleTap(MotionEvent e, ManagedOverlay overlay, GeoPoint point, ManagedOverlayItem item) 
			{
				
				final GeoPoint point1 = point;
				//Snippet para GeoPoint
				List<Address> addresses;
				String add = "";
				Geocoder geoCoder = new Geocoder(
		                   getBaseContext(), Locale.getDefault());
		               try {
		            	       addresses= geoCoder.getFromLocation(
		                       point.getLatitudeE6()  / 1E6, 
		                       point.getLongitudeE6() / 1E6, 1);

		                   
		                   if (addresses.size() > 0) 
		                   {
		                       for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
		                            i++)
		                          add += addresses.get(0).getAddressLine(i) + "\n";
		                   }
		               }
		               catch (IOException ex) {                
		                   ex.printStackTrace();
		               }  
		               
				
		               

			    builder.setTitle("Is this the Location?")
			    .setMessage("You created a Marker in: \n" + add)
			           .setCancelable(false)
			           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
			            	   printMarker(point1);
			               }

						private void printMarker(GeoPoint point) {
							 //Print the Human-readable Address of the touch
							
							Drawable defaultmarker = getResources().getDrawable(R.drawable.marker);     

						    ManagedOverlay managedOverlay = overlayManager.createOverlay(defaultmarker);
						   
						    //creating some marker:
						    managedOverlay.createItem(point);
						   
						    //registers the ManagedOverlayer to the MapView
						    overlayManager.populate(); 	
						   
						    //overlayManager.removeOverlay(managedOverlay);
						   
						//	showNotificationToTheUser(point);
																		
						}
			           })
			           .setNeutralButton("Discard", new DialogInterface.OnClickListener(){
			        	   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
			            	   finish();
			        	   }
			        	   })
			           .setNegativeButton("No", new DialogInterface.OnClickListener() {
			               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
			        		   dialog.cancel();

			               }
			           });
			    final AlertDialog alert = builder.create();
			    alert.show();
			
			    
				return true;
			}	

			public void onLongPress(MotionEvent arg0, ManagedOverlay arg1) {
				// TODO Auto-generated method stub
				return;
			}


			public void onLongPressFinished(MotionEvent arg0,
					ManagedOverlay arg1, GeoPoint arg2, ManagedOverlayItem arg3) {
				return;
			}


			public boolean onScrolled(MotionEvent arg0, MotionEvent arg1,
					float arg2, float arg3, ManagedOverlay arg4) {
				return false;
			}


			public boolean onSingleTap(MotionEvent arg0, ManagedOverlay arg1,
					GeoPoint arg2, ManagedOverlayItem arg3) {
				// TODO Auto-generated method stub
				return false;
			}			
		});
		overlayManager.populate();
		
		
	}

/*
	protected void showNotificationToTheUser(GeoPoint point) {
		   Geocoder geoCoder = new Geocoder(
                   getBaseContext(), Locale.getDefault());
               try {
                   List<Address> addresses = geoCoder.getFromLocation(
                       point.getLatitudeE6()  / 1E6, 
                       point.getLongitudeE6() / 1E6, 1);

                   String add = "";
                   if (addresses.size() > 0) 
                   {
                       for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
                            i++)
                          add += addresses.get(0).getAddressLine(i) + "\n";
                   }
                   showAreYouSureDialog(add);
               }
               catch (IOException ex) {                
                   ex.printStackTrace();
               }  		
	}

	private void showAreYouSureDialog(String add) {
		
	    areYouSureDialog = new AlertDialog.Builder(this);
	    areYouSureDialog.setTitle("Is this the Location?")
	    .setMessage("You created a Marker in: \n" + add)
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	            	   printMarker();
	               }
	           })
	           .setNeutralButton("Discard", new DialogInterface.OnClickListener(){
	        	   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	            	   finish();
	        	   }
	        	   })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	        		   dialog.cancel();

	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}*/
	
	

	

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
}