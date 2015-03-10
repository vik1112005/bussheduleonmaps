package bussheduleonmap.client;

import java.util.ArrayList;
import bussheduleonmap.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
@SuppressWarnings("unused")
public class Bussheduleonmap implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	private final String mapsContainer = "maps";

	  @Override
	  public void onModuleLoad() {
	    loadMapApi();
	  }

	  private void loadMapApi() {
	    boolean sensor = true;

	    // load all the libs for use in the maps
	    ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
	    loadLibraries.add(LoadLibrary.ADSENSE);
	    loadLibraries.add(LoadLibrary.DRAWING);
	    loadLibraries.add(LoadLibrary.GEOMETRY);
	    loadLibraries.add(LoadLibrary.PANORAMIO);
	    loadLibraries.add(LoadLibrary.PLACES);
	    loadLibraries.add(LoadLibrary.WEATHER);
	    loadLibraries.add(LoadLibrary.VISUALIZATION);

	    Runnable onLoad = new Runnable() {
	      @Override
	      public void run() {
	        draw();
	      }
	    };

	    LoadApi.go(onLoad, loadLibraries, sensor);
	  }
	  private void draw() {
		    HTML html = new HTML("<h1>РАСПИСАНИЕ</h1></br><h3>Городских автобусов Молодечно</h3>");

		    RootPanel.get(mapsContainer).add(html);

		    drawFusionMap();

		  }
	  private void addMapWidget(Widget widget) {
		    RootPanel.get(mapsContainer).add(widget);
		  }
	private void drawFusionMap() {
	    FusionTablesMapWidget wMap = new FusionTablesMapWidget();
	    addMapWidget(wMap);
	  }
		
	}

