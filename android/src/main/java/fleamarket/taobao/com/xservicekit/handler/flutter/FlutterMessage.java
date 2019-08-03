package fleamarket.taobao.com.xservicekit.handler.flutter;

import fleamarket.taobao.com.xservicekit.handler.Message;
import fleamarket.taobao.com.xservicekit.handler.MessageHost;

public class FlutterMessage implements Message {

    private String mName;
    private Object mArgs;
    private MessageHost mHost;

    public FlutterMessage(String name,Object args, MessageHost host){
        this.mName = name;
        this.mArgs = args;
        this.mHost = host;
    }

    @Override
    public String name() {
        return mName;
    }

    @Override
    public Object arguments() {
        return mArgs;
    }

    @Override
    public MessageHost Host() {
        return mHost;
    }
}
