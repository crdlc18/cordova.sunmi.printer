package cordova.sunmi.printer;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import com.sunmi.printer.extprinterservice.ExtPrinterService;

public class SunmiPrinter extends Activity {

    private ExtPrinterService printerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindPrinterService();
    }

    private void bindPrinterService() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.extprinterservice");
        intent.setAction("com.sunmi.extprinterservice.PrinterService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            printerService = ExtPrinterService.Stub.asInterface(service);
            Log.d("SunmiPrinter", "Printer Service Connected");
            printText("Hello from Sunmi Printer!");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            printerService = null;
            Log.d("SunmiPrinter", "Printer Service Disconnected");
        }
    };

    private void printText(String text) {
        if (printerService != null) {
            try {
                printerService.printText(text);
                Log.d("SunmiPrinter", "Printed: " + text);
            } catch (Exception e) {
                Log.e("SunmiPrinter", "Print Error", e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (printerService != null) {
            unbindService(serviceConnection);
        }
    }
}
