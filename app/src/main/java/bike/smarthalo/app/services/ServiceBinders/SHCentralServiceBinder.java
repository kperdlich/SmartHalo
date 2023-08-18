package bike.smarthalo.app.services.ServiceBinders;

import android.os.Binder;

import bike.smarthalo.app.services.SHCentralService;

public class SHCentralServiceBinder extends Binder {
    private SHCentralService centralService;

    public SHCentralServiceBinder(SHCentralService sHCentralService) {
        this.centralService = sHCentralService;
    }

    public SHCentralService getService() {
        return centralService;
    }

}
