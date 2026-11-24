package mini_lab.day30;


public class JvmLifecycleDemo {

    static {
        System.out.println("1. Class initialized");
    }

    public static void main(String[] args) {

        System.out.println("2. main() started");

        printProcess();
        printMemory();
        printClassLoader();
        callMethod();

        System.out.println("6. main() finished");
    }

    static void printProcess() {
        long pid = ProcessHandle.current().pid();
        System.out.println("3. JVM process PID=" + pid);
    }

    static void printMemory() {
        Runtime runtime = Runtime.getRuntime();
        System.out.println("4. Max heap MB=" + toMb( runtime.maxMemory()));
    }

    static void printClassLoader() {
        System.out.println("5. ClassLoader=" + JvmLifecycleDemo.class .getClassLoader());
    }

    static void callMethod() {
        int localValue = 42;
        System.out.println("Method local value=" + localValue);
    }

    static long toMb( long bytes) {
        return bytes / 1024 / 1024;
    }
}