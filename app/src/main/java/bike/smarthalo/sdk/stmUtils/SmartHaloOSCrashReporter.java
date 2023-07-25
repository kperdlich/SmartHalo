package bike.smarthalo.sdk.stmUtils;

import bike.smarthalo.sdk.bluetooth.bleNotifications.BleNotificationSourceContract;
import bike.smarthalo.sdk.helpers.DebugLoggerContract;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.processors.FlowableProcessor;
import io.reactivex.rxjava3.processors.PublishProcessor;

/* loaded from: classes.dex */
public class SmartHaloOSCrashReporter implements SmartHaloOSCrashReporterContract {
    private static final String TAG = "SmartHaloOSCrashReporter";
    private BleNotificationSourceContract bleNotificationSource;
    private Disposable bleNotificationSubscription;
    private SmartHaloOSCrash currentCrash;
    private DebugLoggerContract debugLogger;
    private FlowableProcessor<SmartHaloOSCrash> smartHaloOSCrashSource = PublishProcessor.<SmartHaloOSCrash>create().toSerialized();

    public SmartHaloOSCrashReporter(BleNotificationSourceContract bleNotificationSourceContract, DebugLoggerContract debugLoggerContract) {
        this.debugLogger = debugLoggerContract;
        this.bleNotificationSubscription = bleNotificationSourceContract.observeStmLogs().subscribe(new Consumer() { // from class: bike.smarthalo.sdk.stmUtils.-$$Lambda$SmartHaloOSCrashReporter$9aeqqKAnp3WUlPZy0KqLIXGWs_k

            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                SmartHaloOSCrashReporter.this.buildSHOSCrashLog((String) obj);
            }
        });
    }

    public void buildSHOSCrashLog(String str) {
        SmartHaloOSCrash build = SmartHaloOSCrash.build(str);
        if (build != null) {
            if (build.crashType == SmartHaloOSCrash.CrashType.CrashDataPart) {
                this.currentCrash.crashData.add(build.id);
            } else if (build.crashType != SmartHaloOSCrash.CrashType.Other) {
                this.currentCrash = build;
            }
            SmartHaloOSCrash smartHaloOSCrash = this.currentCrash;
            if (smartHaloOSCrash == null || !smartHaloOSCrash.isValid()) {
                return;
            }
            this.smartHaloOSCrashSource.onNext(this.currentCrash);
            DebugLoggerContract debugLoggerContract = this.debugLogger;
            String str2 = TAG;
            debugLoggerContract.log(str2, "STM Crash : " + this.currentCrash.crashType.toString());
            this.currentCrash = null;
        }
    }

    @Override // bike.smarthalo.sdk.stmUtils.SmartHaloOSCrashReporterContract
    public void dispose() {
        Disposable disposable = this.bleNotificationSubscription;
        if (disposable != null) {
            disposable.dispose();
            this.bleNotificationSubscription = null;
        }
    }

    @Override // bike.smarthalo.sdk.stmUtils.SmartHaloOSCrashReporterContract
    public Flowable<SmartHaloOSCrash> observeSmartHaloOSCrash() {
        return this.smartHaloOSCrashSource;
    }
}