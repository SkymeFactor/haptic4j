// file is never used, subject for future improvements

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.List;
import org.haptic4j.HapticEvent;

public final class CallbackRegistry {

    private static final Arena CALLBACK_ARENA = Arena.ofShared();

    // Prevent GC of callbacks + stubs
    private static final List<Object> KEEP_ALIVE = new ArrayList<>();

    private CallbackRegistry() {
    }

    public static MemorySegment register(
            HapticEvent.callback.Function callback
    ) {
        KEEP_ALIVE.add(callback);

        MemorySegment stub = HapticEvent.callback.allocate(
            callback,
            CALLBACK_ARENA
        );

        KEEP_ALIVE.add(stub);

        return stub;
    }
}
