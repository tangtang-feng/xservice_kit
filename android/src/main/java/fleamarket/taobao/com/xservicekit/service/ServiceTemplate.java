package fleamarket.taobao.com.xservicekit.service;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fleamarket.taobao.com.xservicekit.handler.MessageDispatcher;
import fleamarket.taobao.com.xservicekit.handler.MessageHandler;
import fleamarket.taobao.com.xservicekit.handler.MessageResult;
import fleamarket.taobao.com.xservicekit.handler.flutter.FlutterMessage;
import fleamarket.taobao.com.xservicekit.handler.flutter.FlutterMessageDispatcher;
import fleamarket.taobao.com.xservicekit.handler.flutter.FlutterMessageHost;
import fleamarket.taobao.com.xservicekit.message.MessengerFacade;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class ServiceTemplate implements Service,MessageHandler {

    private EventSink eventSink;
    private Object args;
    private MessageDispatcher mDispatcher;
    private String mName = "";
    private Map<String,List<ServiceEventListner>> mEventListners = new HashMap<>();

    public ServiceTemplate(String name) {
        mName = name;
        mDispatcher = new FlutterMessageDispatcher();
        mDispatcher.setContext(this);
        this.registerHandler(this);
    }

    @Override
    public String serviceName() {
        return mName;
    }

    @Override
    public String methodChannelName() {
        return mName + "_method_channel";
    }

    @Override
    public String eventChannelName() {
        return mName + "_event_channel";
    }


    @Override
    public void didRecieveEventSink(EventSink eventSink, Object args) {
        this.eventSink = eventSink;
        this.args = args;
    }

    @Override
    public void didCancelEvenStream(Object args) {
        this.args = null;
        this.eventSink = null;
    }

    @Override
    public void invoke(String name, Object args, String chanelName, MethodChannel.Result result) {
        MessengerFacade.sharedInstance().sendMessage(name,args,chanelName,result);
    }

    @Override
    public void registerHandler(MessageHandler handler) {
        mDispatcher.registerHandler(handler);
    }

    @Override
    public void start() {

        //Register message channels
        MessengerFacade.sharedInstance().registerMethodChannel(this.methodChannelName());
        MessengerFacade.sharedInstance().registerEventChannel(this.eventChannelName());

        //Connect to message handlers.
        MessengerFacade.sharedInstance().setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, final MethodChannel.Result result) {
                FlutterMessageHost host = new FlutterMessageHost(ServiceTemplate.this.methodChannelName());
                FlutterMessage msg = new FlutterMessage(methodCall.method, methodCall.arguments, host);
                mDispatcher.dispatch(msg, new MessageResult() {
                    @Override
                    public void success(Object var1) {
                        result.success(var1);
                    }

                    @Override
                    public void error(String var1, String var2, Object var3) {
                        result.error(var1,var2,var3);
                    }

                    @Override
                    public void notImplemented() {
                        result.notImplemented();
                    }
                });
            }
        }, this.methodChannelName());

        MessengerFacade.sharedInstance().setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object o, final EventChannel.EventSink eventSink) {
                ServiceTemplate.this.didRecieveEventSink(new EventSink() {
                    @Override
                    public void success(Object var1) {
                        eventSink.success(var1);
                    }

                    @Override
                    public void error(String var1, String var2, Object var3) {
                        eventSink.error(var1, var2, var3);
                    }

                    @Override
                    public void endOfStream() {
                        eventSink.endOfStream();
                    }
                }, o);
            }

            @Override
            public void onCancel(Object o) {

            }
        }, this.eventChannelName());

    }

    @Override
    public void end() {
        MessengerFacade.sharedInstance().setMethodCallHandler(null,this.methodChannelName());
        MessengerFacade.sharedInstance().setStreamHandler(null,this.eventChannelName());
    }

    @Override
    public void emitEvent(Map obj) {
        if (this.eventSink != null) {
            this.eventSink.success(this.checkType(obj));
        }
    }



    private Map checkType(Map param){

        /*
        int -> int
        double -> double
        bool -> boolean
        String -> String
        List -> List
        Map -> Map
        * */
        final Map<String,Boolean> supporedType = new HashMap<>();
        supporedType.put("Integer",true);
        supporedType.put("Double",true);
        supporedType.put("Boolean",true);
        supporedType.put("String",true);


        Iterator it = param.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry pair = (Map.Entry)it.next();

            //For null values.
            if(pair == null || pair.getKey() == null || pair.getValue() == null) continue;

            String typeName = pair.getValue().getClass().getSimpleName();
            if (pair.getValue() instanceof Map<?,?> || pair.getValue() instanceof List<?>){
                //Do nothing.
            }else{
                if (!supporedType.get(pair.getValue().getClass().getSimpleName())){
                    //remove unsuppored type.
                    it.remove();
                }
            }


        }

        return param;
    }


    @Override
    public boolean onMethodCall(String name, Map args, MessageResult result) {
        this.onEvent((String) args.get("event"),(Map) args.get("params"));
        return true;
    }

    @Override
    public Object getContext() {
        return null;
    }

    @Override
    public void setContext(Object obj) {
        //Do nothing
    }

    @Override
    public List<String> handleMessageNames() {
        List<String> list = new ArrayList<>();
        list.add("__event__");
        return list;
    }

    @Override
    public String service() {
        return this.serviceName();
    }

    private void onEvent(String event , Map params){
        if(event == null){
            return;
        }

        List<ServiceEventListner> listners = mEventListners.get(event);
        if(listners != null){
            for(ServiceEventListner l : listners){
                l.onEvent(event,params);
            }
        }
    }

    @Override
    public void emitEvent(String even, Map obj) {
        if(even == null) return;

        Map<Object,Object> msg = new HashMap<>();
        msg.put("event",even);
        if(obj != null){
            msg.put("params",obj);
        }

        this.invoke("__event__", msg, methodChannelName(), new MethodChannel.Result() {
            @Override
            public void success(Object o) {
            }

            @Override
            public void error(String s, String s1, Object o) {
            }

            @Override
            public void notImplemented() {
            }
        });
    }

    @Override
    public void addEventListner(String event, ServiceEventListner listner) {
        if(event == null || listner == null){
            return;
        }

        List<ServiceEventListner> listners = mEventListners.get(event);
        if(listners == null){
            listners = new ArrayList<>();
            mEventListners.put(event,listners);
        }

        listners.add(listner);
    }

    @Override
    public void removeEventListner(String event, ServiceEventListner listner) {
        if(event == null || listner == null){
            return;
        }

        List<ServiceEventListner> listners = mEventListners.get(event);
        if(listners != null){
            listners.remove(listner);
        }
    }
}
