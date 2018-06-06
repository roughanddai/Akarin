package io.akarin.api;

import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.ThreadFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import co.aikar.timings.Timing;
import co.aikar.timings.Timings;

public abstract class Akari {
    /**
     * A common logger used by mixin classes
     */
    public final static Logger logger = LogManager.getLogger("Akarin");
    
    /**
     * Temporarily disable desync timings error, moreover it's worthless to trace async operation
     */
    public static volatile boolean silentTiming;
    
    /**
     * A common thread pool factory
     */
    public static final ThreadFactory STAGE_FACTORY = new ThreadFactoryBuilder().setNameFormat("Akarin Schedule Thread - %1$d").build();
    
    /**
     * Main thread callback tasks
     */
    public static final Queue<Runnable> callbackQueue = Queues.newConcurrentLinkedQueue();
    
    /*
     * Timings
     */
    private static Timing callbackTiming;
    
    public static Timing callbackTiming() {
        if (callbackTiming == null) {
            try {
                Method ofSafe = Timings.class.getDeclaredMethod("ofSafe", String.class);
                ofSafe.setAccessible(true);
                callbackTiming = (Timing) ofSafe.invoke(null, "Akarin - Callback");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return callbackTiming;
    }
}
