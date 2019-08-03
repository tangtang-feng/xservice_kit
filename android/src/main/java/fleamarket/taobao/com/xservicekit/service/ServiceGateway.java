package fleamarket.taobao.com.xservicekit.service;

import java.util.HashMap;
import java.util.Map;

import fleamarket.taobao.com.xservicekit.handler.MessageHandler;

public class ServiceGateway {

    private  static final  ServiceGateway instance = new ServiceGateway();

    private Map<String,Service> mServices = new HashMap<>();

    public static ServiceGateway sharedInstance(){
        return instance;
    }

    public void addService(Service serivice){
        if (serivice != null){
            mServices.put(serivice.serviceName(),serivice);
            serivice.start();
        }
    }

    public void registerHandler(MessageHandler handler){
        mServices.get(handler.service()).registerHandler(handler);
    }

    public void removeService(Service service){
        if (service != null){
            mServices.remove(service.serviceName());
            service.end();
        }
    }

}
