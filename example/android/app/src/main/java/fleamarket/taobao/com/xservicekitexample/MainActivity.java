package fleamarket.taobao.com.xservicekitexample;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import fleamarket.taobao.com.xservicekit.handler.MessageResult;
import fleamarket.taobao.com.xservicekit.service.ServiceEventListner;
import fleamarket.taobao.com.xservicekitexample.DemoService0.service.DemoService0;
import fleamarket.taobao.com.xservicekitexample.loader.ServiceLoader;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

import android.os.Handler;
import android.util.Log;

public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ServiceLoader.load();

    GeneratedPluginRegistrant.registerWith(this);


  }

  private void test(){

    System.out.println("Send Native message sent to flutter");

    //Send messaget to flutter
    DemoService0.MessageToFlutter(new MessageResult<Map>() {
      @Override
      public void success(Map var1) {
        System.out.println( "Native message sent to flutter success!");
      }

      @Override
      public void error(String var1, String var2, Object var3) {
        System.out.println( "Send Native message to flutter error");
      }

      @Override
      public void notImplemented() {

        System.out.println( "Send Native message to flutter not implemented");

      }
    },"This a message from native to flutter");


    DemoService0.getService().emitEvent("test",new HashMap());
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();

    DemoService0.getService().addEventListner("test", new ServiceEventListner() {
      @Override
      public void onEvent(String name, Map params) {
        System.out.println("Did recieve broadcast even from flutter");
      }
    });

    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
          test();
      }
    }, 2000);




  }
}
