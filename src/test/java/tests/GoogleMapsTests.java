package tests;

import org.testng.annotations.Test;

public class GoogleMapsTests extends BaseClass {
	 @SuppressWarnings("static-access")
	 @Test
	   public void f() {
	 	  googleMaps.getDirections(operations.getProperty(configFilePath, "source"), operations.getProperty(configFilePath, "destination"));
	 	  
	   }
}
