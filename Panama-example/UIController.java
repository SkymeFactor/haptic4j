import java.util.Scanner;

class UIController {
    private final Scanner scanner = new Scanner(System.in);
    private final Rumbler rumbler;

    public UIController(Rumbler rumbler) {
        this.rumbler = rumbler;
        rumbler.conf.durationMs = 5000;
        rumbler.conf.weakMagnitude = 65535;
    }

    public void run() {
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
                case "6" -> rumbler.callRumble();
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
        System.out.println("Device   : " + rumbler.conf.joyDevice);
        System.out.println("Strong   : " + rumbler.conf.strongMagnitude);
        System.out.println("Weak     : " + rumbler.conf.weakMagnitude);
        System.out.println("Duration : " + rumbler.conf.durationMs + " ms");
    }

    private void selectJoyDevice() {
        System.out.print("Enter joy device number: ");

        try {
            rumbler.conf.joyDevice = Integer.parseInt(scanner.nextLine());
            System.out.println("Device set to: " + rumbler.conf.joyDevice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private void setStrongMagnitude() {
        System.out.print("Enter strong magnitude (0-65535): ");

        try {
            rumbler.conf.strongMagnitude = Integer.parseInt(scanner.nextLine());
            System.out.println("Strong magnitude set to: " + rumbler.conf.strongMagnitude);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private void setWeakMagnitude() {
        System.out.print("Enter weak magnitude (0-65535): ");

        try {
            rumbler.conf.weakMagnitude = Integer.parseInt(scanner.nextLine());
            System.out.println("Weak magnitude set to: " +rumbler.conf.weakMagnitude);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private void setDuration() {
        System.out.print("Enter duration in milliseconds: ");

        try {
            rumbler.conf.durationMs = Integer.parseInt(scanner.nextLine());
            System.out.println("Duration set to: " + rumbler.conf.durationMs + " ms");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }
}
