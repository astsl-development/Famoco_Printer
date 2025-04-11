package com;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class FamocoPrinter extends CordovaPlugin {

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);        
        Context context = this.cordova.getActivity().getApplicationContext();     
    }
    

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        Context context = cordova.getActivity().getApplicationContext();
        PX400Printer printer = new PX400Printer(context);

        if(action.equals("printText")){
            String text = args.getString(0);
            
            try{    
                printer.open();            
                printer.printText(text);            
                printer.close();
                callbackContext.success("Printed Successfully");
                return true;
            }catch(Exception ex){
                callbackContext.error("Unable to perform print.\nError Details:\n");
                ex.printStackTrace();
            }
            
        }
        return false;
    }

}
