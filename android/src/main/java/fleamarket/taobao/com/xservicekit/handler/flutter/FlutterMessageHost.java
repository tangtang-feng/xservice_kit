package fleamarket.taobao.com.xservicekit.handler.flutter;

import fleamarket.taobao.com.xservicekit.handler.MessageHost;

public class FlutterMessageHost implements MessageHost {

    private String mName;

    public FlutterMessageHost(String name){
        mName = name;
    }


    @Override
    public String name() {
        return mName;
    }
}
