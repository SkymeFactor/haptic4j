// import java.lang.foreign.*;
// import java.lang.invoke.MethodHandle;
// import java.nio.file.Path;
import java.util.Scanner;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

// import static java.lang.foreign.ValueLayout.JAVA_INT;
import org.haptic.wrapper_h;
import org.haptic.HapticEvent;


class Rumbler {
    private final Scanner scanner = new Scanner(System.in);
    private int joyDevice = 0;
    private int strongMagnitude = 0x0000;
    private int weakMagnitude = 0x0000;
    private int durationMs = 0;

    // Keep strong references so GC cannot collect them
    private static final Arena CALLBACK_ARENA = Arena.ofShared();
    private static final java.util.List<Object> KEEP_ALIVE =
        new java.util.ArrayList<>();

    public static void main(String[] args) {
        new Rumbler().run();
    }

    static void callRumble(
            int joyDevice,
            int strongMagnitude,
            int weakMagnitude,
            int durationMs,
            HapticEvent.callback.Function callback
    ) {
        try (Arena arena = Arena.ofConfined()) {
            // Allocate the struct
            MemorySegment event = HapticEvent.allocate(arena);

            // Set fields
            HapticEvent.joyDevice(event, joyDevice);
            HapticEvent.strong(event, strongMagnitude);
            HapticEvent.weak(event, weakMagnitude);
            HapticEvent.duration(event, durationMs);

            // Create callback stub from lambda
            KEEP_ALIVE.add(callback);
            MemorySegment callbackStub = HapticEvent.callback.allocate(
                callback,
                CALLBACK_ARENA
            );
            KEEP_ALIVE.add(callbackStub);

            // Assign callback pointer into struct
            HapticEvent.callback(event, callbackStub);
            
            wrapper_h.rumble(event);
        }
    }

    private void run() {
        printHelp();
        boolean running = true;
        
        while (running) {
            System.out.print("\nEnter command: ");
            String input = scanner.nextLine();
            switch (input) {
                case "1" -> printHelp();
                case "2" -> selectJoyDevice();
                case "3" -> setStrongMagnitude();
                case "4" -> setWeakMagnitude();
                case "5" -> setDuration();
                case "6" -> callRumble(
                                joyDevice,
                                strongMagnitude,
                                weakMagnitude,
                                durationMs,
                                () -> System.out.println("Native code triggered callback")
                            );
                case "0" -> running = false;
                default -> System.out.println("Unknown command.");
            }
        }
        System.out.println("Exiting.");
    }

    private void printHelp() {
        System.out.println("==== Rumblin Commands ====");
        System.out.println("1 - Show help");
        System.out.println("2 - Select joy device");
        System.out.println("3 - Set strong magnitude");
        System.out.println("4 - Set weak magnitude");
        System.out.println("5 - Set duration");
        System.out.println("6 - Execute rumble");
        System.out.println("0 - Exit");

        System.out.println("\nCurrent configuration:");
        printCurrentConfig();
    }

    private void printCurrentConfig() {
        System.out.println("Device   : " + joyDevice);
        System.out.println("Strong   : " + strongMagnitude);
        System.out.println("Weak     : " + weakMagnitude);
        System.out.println("Duration : " + durationMs + " ms");
    }

    private void selectJoyDevice() {
        System.out.print("Enter joy device number: ");

        try {
            joyDevice = Integer.parseInt(scanner.nextLine());
            System.out.println("Device set to: " + joyDevice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private void setStrongMagnitude() {
        System.out.print("Enter strong magnitude (0-65535): ");

        try {
            strongMagnitude = Integer.parseInt(scanner.nextLine());
            System.out.println("Strong magnitude set to: " + strongMagnitude);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private void setWeakMagnitude() {
        System.out.print("Enter weak magnitude (0-65535): ");

        try {
            weakMagnitude = Integer.parseInt(scanner.nextLine());
            System.out.println("Weak magnitude set to: " + weakMagnitude);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private void setDuration() {
        System.out.print("Enter duration in milliseconds: ");

        try {
            durationMs = Integer.parseInt(scanner.nextLine());
            System.out.println("Duration set to: " + durationMs + " ms");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }
    
    // JNI way
    // Define native functions
    // static {
    //     System.loadLibrary("wrapper");
    // }

    // public static native void rumble(int joynum, int strong, int weak, int duration);

    // FFM way
    // static {
    //     SymbolLookup.libraryLookup(
    //         Path.of(System.getProperty("user.dir"), "libwrapper.so"),
    //         Arena.global()
    //     );
    // }
    // private static final Linker LINKER = Linker.nativeLinker();
    // private static final SymbolLookup LOOKUP = SymbolLookup.loaderLookup();
    // private static final MethodHandle RUMBLE;

    // static {
    //     try {
    //         MemorySegment symbol = LOOKUP.find("rumble").orElseThrow();

    //         RUMBLE = LINKER.downcallHandle(
    //                 symbol,
    //                 FunctionDescriptor.ofVoid(
    //                         JAVA_INT,
    //                         JAVA_INT,
    //                         JAVA_INT,
    //                         JAVA_INT
    //                 )
    //         );
    //     } catch (Throwable t) {
    //         throw new RuntimeException(t);
    //     }
    // }

    // public static void rumble(int joynum, int strong, int weak, int duration) {
    //     try {
    //         RUMBLE.invokeExact(joynum, strong, weak, duration );
    //     } catch (Throwable t) {
    //         throw new RuntimeException(t);
    //     }
    // }
}
