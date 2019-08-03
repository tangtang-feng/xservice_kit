package fleamarket.taobao.com.xservicekit.message;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;

public class MessengerFacade {

    private BinaryMessenger messenger;

    private  static  MessengerFacade instance = new MessengerFacade();

    //Store current active channels.
    private Map<String,MethodChannel> mMethodChannels = new HashMap<>();
    private Map<String,EventChannel> mEventChannels = new HashMap<>();


    //To store register and handlers when messengaers not up.
    private Map<String,MethodChannel.MethodCallHandler> mPendingMethodHandlers = new HashMap<>();
    private Map<String,EventChannel.StreamHandler> mPendingEventStreamHandlers = new HashMap<>();
    private Map<String,String> mPendingEventChannels = new HashMap<>();
    private Map<String,String> mPendingMethodChannels = new HashMap<>();

    public static MessengerFacade sharedInstance(){
        return instance;
    }

    private  MessengerFacade(){

    }

    public void setMessenger(BinaryMessenger msger){
        this.messenger = msger;
        if (this.messenger != null){
            this.loadPendings();
        }
    }


    public BinaryMessenger getMessenger() {
        return messenger;
    }

    private void loadPendings(){

        for(String mKey : mPendingMethodChannels.keySet()){
            this.registerMethodChannel(mKey);
            this.setMethodCallHandler(mPendingMethodHandlers.get(mKey),mKey);
        }

        for(String mKey : mPendingEventChannels.keySet()){
            this.registerEventChannel(mKey);
            this.setStreamHandler(mPendingEventStreamHandlers.get(mKey),mKey);
        }

        this.mPendingMethodChannels.clear();
        this.mPendingEventChannels.clear();
        this.mPendingEventStreamHandlers.clear();
        this.mPendingMethodHandlers.clear();
    }

    private  MethodChannel methodChannelForName(String name){
        if(name == null){
            return null;
        }
        return mMethodChannels.get(name);
    }

    private EventChannel eventChannelForName(String name){
        if(name == null){
            return null;
        }
        return mEventChannels.get(name);
    }


    public void sendMessage(String name, Object args, String chanelName, MethodChannel.Result result){
        if (name == null){
            return;
        }
        MethodChannel mChanel = this.methodChannelForName(chanelName);
        if (mChanel != null){
            mChanel.invokeMethod(name,args,result);
        }
        //TODO: should handle possible pending messages.
    }

    public void setMethodCallHandler(MethodChannel.MethodCallHandler handler,String channelName){

        if (handler == null && channelName != null){
            mMethodChannels.remove(channelName);
            return;
        }

        if (channelName == null){
            return;
        }

        MethodChannel mChanel = this.methodChannelForName(channelName);
        if (mChanel != null){
            mChanel.setMethodCallHandler(handler);
        }else{
            mPendingMethodHandlers.put(channelName,handler);
        }
    }

    public void setStreamHandler(EventChannel.StreamHandler handler, String channelName){

        if (handler == null && channelName != null){
            mEventChannels.remove(channelName);
            return;
        }

        if (channelName == null){
            return;
        }

        EventChannel eChannel = this.eventChannelForName(channelName);
        if (eChannel != null){
            eChannel.setStreamHandler(handler);
        }else{
            mPendingEventStreamHandlers.put(channelName,handler);
        }
    }

    public void registerMethodChannel(String name){

        if(name == null){
            return ;
        }

        if(this.messenger == null){
            this.mPendingMethodChannels.put(name,name);
        }else{
            MethodChannel methodChannel = mMethodChannels.get(name);
            if (methodChannel == null){
                methodChannel = new MethodChannel(this.messenger,name);
                mMethodChannels.put(name,methodChannel);
            }
        }
    }

    public void registerEventChannel(String name){
        if(this.messenger == null){
            this.mPendingEventChannels.put(name,name);
        }else{
            EventChannel eventChannel = mEventChannels.get(name);
            if (eventChannel == null){
                eventChannel = new EventChannel(this.messenger,name);
                mEventChannels.put(name,eventChannel);
            }
        }
    }

    public Collection<MethodChannel> getAllMethodChannels(){
       return mMethodChannels.values();
    }

    public Collection<EventChannel> getAllEventChannels(){
        return mEventChannels.values();
    }

}
