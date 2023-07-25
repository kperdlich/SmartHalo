package bike.smarthalo.sdk;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

/* loaded from: classes.dex */
public class ObservableTimerHelper {
    public static Observable<Long> getTimerOnMainThread(long j) {
        return Observable.timer(j, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Long> getObservableTimer(long j, @Nullable Runnable runnable) {
        return getObservableTimer(0L, j, null, runnable);
    }

    public static Observable<Long> getObservableTimer(long j, @Nullable Runnable runnable, @Nullable Runnable runnable2) {
        return getObservableTimer(0L, j, runnable, runnable2);
    }

    public static Observable<Long> getObservableTimer(long j, long j2, @Nullable final Runnable runnable, @Nullable final Runnable runnable2) {
        if (j < 0) {
            j = 0;
        }
        if (j2 < 0) {
            j2 = 0;
        }
        return Observable.concat(Observable.timer(j, TimeUnit.MILLISECONDS).doOnComplete(new Action() { // from class: bike.smarthalo.sdk.-$$Lambda$ObservableTimerHelper$iaJ5JVaBq13_TQyyUmpNzc9Coao
            @Override // io.reactivex.functions.Action
            public final void run() {
                if (runnable != null) {
                    runnable.run();
                }
            }
        }), Observable.timer(j2, TimeUnit.MILLISECONDS).doOnComplete(new Action() { // from class: bike.smarthalo.sdk.-$$Lambda$ObservableTimerHelper$DHxOfYNNYVcWJC6gc3GG-bCbE-k
            @Override // io.reactivex.functions.Action
            public final void run() {
                if (runnable2 != null) {
                    runnable2.run();
                }
            }
        })).subscribeOn(Schedulers.io());
    }
}