package bussheduleonmap.client;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.Size;
import com.google.gwt.maps.client.controls.MapTypeStyle;
import com.google.gwt.maps.client.events.fusiontablemouse.FusionTablesMouseMapEvent;
import com.google.gwt.maps.client.events.fusiontablemouse.FusionTablesMouseMapHandler;
import com.google.gwt.maps.client.layers.FusionTablesLayer;
import com.google.gwt.maps.client.layers.FusionTablesLayerOptions;
import com.google.gwt.maps.client.layers.FusionTablesMarkerOptions;
import com.google.gwt.maps.client.layers.FusionTablesQuery;
import com.google.gwt.maps.client.layers.FusionTablesStyle;
import com.google.gwt.maps.client.maptypes.MapTypeStyleElementType;
import com.google.gwt.maps.client.maptypes.MapTypeStyleFeatureType;
import com.google.gwt.maps.client.maptypes.MapTypeStyler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.reveregroup.gwt.imagepreloader.FitImage;
import com.reveregroup.gwt.imagepreloader.FitImageLoadEvent;
import com.reveregroup.gwt.imagepreloader.FitImageLoadHandler;
import com.reveregroup.gwt.imagepreloader.ImageLoadEvent;
import com.reveregroup.gwt.imagepreloader.ImageLoadHandler;
import com.reveregroup.gwt.imagepreloader.ImagePreloader;


@SuppressWarnings("unused")
public class FusionTablesMapWidget extends Composite {

  private VerticalPanel pWidget;
  static private int x;
  private MapWidget mapWidget;
  private final String mapsContainer = "maps";

  public FusionTablesMapWidget() {

    pWidget = new VerticalPanel();

    initWidget(pWidget);

    draw();
  }

  private void draw() {

    pWidget.clear();
     pWidget.add(new HTML("<br>Найдите нужную остановку и кликните по ней."));

    setupMap();

    setupFusionTablesLayer();

  }

  private void setupMap() {
    //Молодечно 
    LatLng center = LatLng.newInstance(54.31, 26.85);

    //убираем остановки автобусов используя для этого стили 
    MapTypeStyle[] mapTypeStyles = new MapTypeStyle[1];
	mapTypeStyles[0] = MapTypeStyle.newInstance();
	
	MapTypeStyleFeatureType mapTypeStyleFeatureType = MapTypeStyleFeatureType.TRANSIT__STATION__BUS;
	mapTypeStyles[0].setFeatureType(mapTypeStyleFeatureType);
	
	MapTypeStyleElementType mapTypeStyleElementType = MapTypeStyleElementType.LABELS;
	mapTypeStyles[0].setElementType(mapTypeStyleElementType);
	
	MapTypeStyler[] stylers = new MapTypeStyler[1];
	stylers[0]=MapTypeStyler.newVisibilityStyler("off");
	mapTypeStyles[0].setStylers(stylers);
    
    MapOptions options = MapOptions.newInstance();
    options.setCenter(center);
    options.setZoom(13);
    options.setMapTypeStyles(mapTypeStyles);
    options.setMapTypeId(MapTypeId.ROADMAP);

    mapWidget = new MapWidget(options);
    pWidget.add(mapWidget);
    RootPanel rp=RootPanel.get(mapsContainer);
    pWidget.getElement().getClientHeight();
    pWidget.getElement().getClientWidth();
    mapWidget.setPixelSize((int)(Window.getClientWidth()*0.85) ,(int)(Window.getClientHeight()*0.7));
  }

  private void setupFusionTablesLayer() {

    String select = "Location";
    String from = "1LlwfFhKXBfO_w9NNfXC9zLRuWvOn8tO4VEtZ3cMB";

    FusionTablesQuery query = FusionTablesQuery.newInstance();
    query.setSelect(select);
    query.setFrom(from);

    FusionTablesLayerOptions options = FusionTablesLayerOptions.newInstance();
    options.setSuppressInfoWindows(true);
    options.setQuery(query);
    
    final PopupPanel imagePopup = new PopupPanel(true);
		 imagePopup.setAnimationEnabled(true);
		imagePopup.ensureDebugId("cwBasicPopup-imagePopup");
		 //меняем маркер через стили
	    FusionTablesMarkerOptions markeropts = FusionTablesMarkerOptions.newInstance();
	    markeropts.setIconName("bus");
	    FusionTablesStyle ftstyles  = FusionTablesStyle.newInstance();
	    ftstyles.setMarkerOptions(markeropts);
	    FusionTablesStyle[] array = new FusionTablesStyle[1];
	    array[0] = ftstyles;
	    JsArray<FusionTablesStyle> jstyles=ArrayHelper.toJsArray(array);
	    options.setStyles(jstyles );	
    FusionTablesLayer layer = FusionTablesLayer.newInstance(options);
   layer.addClickHandler(new FusionTablesMouseMapHandler() {
      public void onEvent(FusionTablesMouseMapEvent event) {
    	  
    	  
		
        String infoHtml =event.getInfoWindowHtml();
       String[] infoArr =  infoHtml.trim().split("http");
        VerticalPanel vp=new VerticalPanel();
        
        for (int i = 1; i < infoArr.length; i++) {
        	if (i==(infoArr.length-1)){
        		infoArr[i]=infoArr[i].trim().split("jpeg")[0];
        		infoArr[i]=infoArr[i]+"jpeg";
        		
        	}
        	infoArr[i]="http"+infoArr[i];
        	
        	ImagePreloader.load(infoArr[i] , new OnLoad());
        	FitImage fi = new FitImage();
             fi.addFitImageLoadHandler(new OnLoad());
             fi.setFixedWidth((int)(Window.getClientWidth()*0.5));
             fi.setUrl(infoArr[i]);
        	vp.add(fi);
        	
        	}
        
        LatLng latlng = event.getLatLng();

        Size pixelOffset = event.getPixelOffset();

       final ScrollPanel panel = new ScrollPanel(); 
        panel.add(vp);
        imagePopup.setPixelSize((int)(Window.getClientWidth()*0.5)+20 ,(int)(Window.getClientHeight()*0.7));
    	 imagePopup.setWidget(panel);
       imagePopup.center();
    	
      }
    });
    layer.setMap(mapWidget);
  }
  
  
    class OnLoad implements ImageLoadHandler, FitImageLoadHandler {
      
      public OnLoad() {
         
      }
      
     
      
      public void imageLoaded(ImageLoadEvent event) {
              if (event.isLoadFailed()) {
                      Window.alert( "Ошибка загрузки расписания!");
              } else {
            	 if(event.getDimensions().getWidth()>x) {
                    	
                    	 x=event.getDimensions().getWidth(); 
                   }
              }
             
      }
      
      public void imageLoaded(FitImageLoadEvent event) {
              if (event.isLoadFailed()) {
            	  Window.alert( "Ошибка загрузки расписания!");
              } else {
            	 if(event.getFitImage().getOriginalWidth()>x) 
                	 x=event.getFitImage().getOriginalWidth();
                }
      }
}
  
  

}
