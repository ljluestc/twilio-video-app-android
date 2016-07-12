package com.twilio.conversations;

import android.Manifest;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

import com.twilio.common.AccessManager;
import com.twilio.conversations.internal.Logger;
import com.twilio.conversations.internal.ReLinker;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The Client allows user to create or participate in Rooms.
 *
 */
public class Client {

    private static final String[] REQUIRED_PERMISSIONS = {
            // Required permissions granted upon install
            Manifest.permission.INTERNET,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    private static LogLevel level = LogLevel.OFF;
    private static Map<LogModule, LogLevel> moduleLogLevel = new EnumMap(LogModule.class);
    private static volatile boolean libraryIsLoaded = false;
    private static final Logger logger = Logger.getLogger(Client.class);

    private Handler handler;
    private final Context applicationContext;
    private Listener listener;
    private AccessManager accessManager;
    private Map<String, Room> rooms;

    public Client(Context context, AccessManager accessManager, Client.Listener listener) {
        if (context == null) {
            throw new NullPointerException("applicationContext must not be null");
        }
        if(accessManager == null) {
            throw new NullPointerException("accessManager must not be null");
        }

        this.applicationContext = context.getApplicationContext();
        this.accessManager = accessManager;
        this.listener = listener;

        handler = Util.createCallbackHandler();

        checkPermissions(context);

        if (!libraryIsLoaded) {
            ReLinker.loadLibrary(this.applicationContext, "jingle_peerconnection_so");
            libraryIsLoaded = true;
        }

        /*
         * The user may have set the log level prior to the native library being loaded.
         * Attempt to set the core log level now that the native library has loaded.
         */
        trySetCoreLogLevel(level.ordinal());

        /*
         * It is possible that the user has tried to set the log level for a specific module
         * before the library has loaded. Here we apply the log level for the module because we
         * know the native library is available
         */
        for (LogModule module : moduleLogLevel.keySet()) {
            trySetCoreModuleLogLevel(module.ordinal(), moduleLogLevel.get(module).ordinal());
        }

    }

    /**
     * Set a new {@link Listener} to respond to client events.
     *
     * @param listener A listener for client events.
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Get the {@link Listener}
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * Sets the audio output speaker for the device.
     *
     * Bluetooth headset is not supported.
     *
     * To use volume up/down keys call
     * 'setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);' in your Android Activity.
     *
     * @param audioOutput that should be used by the system
     */
    public void setAudioOutput(AudioOutput audioOutput) {
        logger.d("setAudioOutput");
        AudioManager audioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        if (audioOutput == AudioOutput.SPEAKERPHONE) {
            audioManager.setSpeakerphoneOn(true);
        } else {
            audioManager.setSpeakerphoneOn(false);
        }
    }

    /**
     * Audio output speaker for the current client device
     *
     * @return audio output speaker
     */
    public AudioOutput getAudioOutput() {
        logger.d("getAudioOutput");
        AudioManager audioManager = (AudioManager) applicationContext.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.isSpeakerphoneOn() ? AudioOutput.SPEAKERPHONE : AudioOutput.HEADSET;
    }

    public RoomFuture connect(Room.Listener listener) {
        // TODO: implement me
        return null;
    }

    public RoomFuture connect(ConnectOptions connectOptions, Room.Listener listener) {
        // TODO: implement me
        return null;
    }

    public void disconnect(Room room) {
        // TODO: implement me
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    /**
     * Listener interface defines a set of callbacks for events related to a
     * {@link Client}.
     *
     */
    public interface Listener {

        void onConnected(Room room);

        void onConnectFailure(RoomsException error);

        void onDisconnected(Room room, RoomsException error);

    }

    /**
     * Returns the version of the Rooms SDK.
     *
     * @return the version of the SDK
     */
    public static String getVersion() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * Gets the logging level for messages logged by the Rooms SDK.
     *
     * @return the logging level
     */
    public static LogLevel getLogLevel() {
        return LogLevel.values()[tryGetCoreLogLevel()];
    }

    /**
     * Sets the logging level for messages logged by the Rooms SDK.
     *
     * @param level The logging level
     */
    public static void setLogLevel(LogLevel level) {
        setSDKLogLevel(level);
        trySetCoreLogLevel(level.ordinal());
        // Save the log level
        Client.level = level;
    }

    /**
     * Sets the logging level for messages logged by a specific module.
     *
     * @param module The module for this log level
     * @param level The logging level
     */
    public static void setModuleLogLevel(LogModule module, LogLevel level) {
        if (module == LogModule.PLATFORM) {
            setSDKLogLevel(level);
        }
        trySetCoreModuleLogLevel(module.ordinal(), level.ordinal());
        //Save the module log level
        Client.moduleLogLevel.put(module, level);
    }

    private static void setSDKLogLevel(LogLevel level) {
         /*
         * The Log Levels are defined differently in the Logger
         * which is based off android.util.Log.
         */
        switch (level) {
            case OFF:
                Logger.setLogLevel(Log.ASSERT);
                break;
            case FATAL:
                Logger.setLogLevel(Log.ERROR);
                break;
            case ERROR:
                Logger.setLogLevel(Log.ERROR);
                break;
            case WARNING:
                Logger.setLogLevel(Log.WARN);
                break;
            case INFO:
                Logger.setLogLevel(Log.INFO);
                break;
            case DEBUG:
                Logger.setLogLevel(Log.DEBUG);
                break;
            case TRACE:
                Logger.setLogLevel(Log.VERBOSE);
                break;
            case ALL:
                Logger.setLogLevel(Log.VERBOSE);
                break;
            default:
                // Set the log level to assert/disabled if the value passed in is unknown
                Logger.setLogLevel(Log.ASSERT);
                break;
        }
    }

    /*
     * This is a convenience safety method in the event that the core log level is attempted before
     * initialization.
     *
     * @param level
     */
    private static void trySetCoreLogLevel(int level) {
        if (libraryIsLoaded) {
            nativeSetCoreLogLevel(level);
        }
    }

    /*
     * Convenience safety method for retrieving core log level.
     *
     * @return Core log level or current value that the user has set if the native library has not
     * been loaded
     */
    private static int tryGetCoreLogLevel() {
        return (libraryIsLoaded) ? (nativeGetCoreLogLevel()) : (level.ordinal());
    }

    private static void trySetCoreModuleLogLevel(int module, int level) {
        if (libraryIsLoaded) {
            nativeSetModuleLevel(module, level);
        }
    }

    private static void checkPermissions(Context context) {
        List<String> missingPermissions = new LinkedList<>();
        for (String permission : REQUIRED_PERMISSIONS) {
            if (!Util.permissionGranted(context, permission)) {
                missingPermissions.add(permission);
            }
        }

        if (!missingPermissions.isEmpty()) {
            StringBuilder builder = new StringBuilder(
                    "Your app is missing the following required permissions:");
            for (String permission : missingPermissions)
                builder.append(' ').append(permission);

            throw new RuntimeException(builder.toString());
        }
    }

    private native static void nativeSetCoreLogLevel(int level);
    private native static void nativeSetModuleLevel(int module, int level);
    private native static int nativeGetCoreLogLevel();

}
