package mini_lab.day30;


public class JvmMemoryDemo {

    public static void main( String[] args) {

        Runtime runtime = Runtime.getRuntime();

        long maxMemory = runtime.maxMemory();

        long totalMemory = runtime.totalMemory();

        long freeMemory = runtime.freeMemory();

        System.out.println("Max memory: " + toMb(maxMemory));
        System.out.println("Total memory: " + toMb(totalMemory));
        System.out.println("Free memory: " + toMb(freeMemory));

        System.out.println(JvmMemoryDemo.class.getClassLoader() );
        System.out.println(String.class.getClassLoader());
    }

    static long toMb(long bytes) {
        return bytes / 1024 / 1024;
    }
}