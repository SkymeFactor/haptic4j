import java.util.Scanner;

class Rumbler {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("exit") || input.equals("quit") || input.equals("q")) {
                System.exit(0);
            }
            rumble(0, 0xffff, 0, 5000);
        }
    }

    static {
        System.loadLibrary("haptic4j");
    }

    public static void callback(String message) {
        System.out.println(message);
    }

    public static native void rumble(int joynum, int strong, int weak, int duration);
}
