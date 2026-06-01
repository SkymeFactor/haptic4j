import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import org.haptic4j.HapticEvent;
import org.haptic4j.Rumbler_h;


class Rumbler {
    public class RumblerConfig {
        public int joyDevice = 0;
        public int strongMagnitude = 0x0000;
        public int weakMagnitude = 0x0000;
        public int durationMs = 0;
    }
    public RumblerConfig conf = new RumblerConfig();

    // Keep strong references so GC cannot collect them
    private static final Arena CALLBACK_ARENA = Arena.ofShared();
    private static final java.util.List<Object> KEEP_ALIVE =
        new java.util.ArrayList<>();

    public static void main(String[] args) {
        new UIController(new Rumbler()).run();
    }

    public void callRumble() {
        try (Arena arena = Arena.ofConfined()) {
            // Allocate the struct
            MemorySegment event = HapticEvent.allocate(arena);

            // Set fields
            HapticEvent.joyDevice(event, conf.joyDevice);
            HapticEvent.strong(event, conf.strongMagnitude);
            HapticEvent.weak(event, conf.weakMagnitude);
            HapticEvent.duration(event, conf.durationMs);

            // Create callback stub from lambda
            HapticEvent.callback.Function callback =
                () -> System.out.println("Native code triggered callback");
            KEEP_ALIVE.add(callback);
            MemorySegment callbackStub = HapticEvent.callback.allocate(
                callback,
                CALLBACK_ARENA
            );
            KEEP_ALIVE.add(callbackStub);

            // Assign callback pointer into struct
            HapticEvent.callback(event, callbackStub);
            
            Rumbler_h.rumble(event);
        }
    }
}
