package mini_lab.day27;

public class ModulesClasspathJarDemo {

    public static void main(String[] args) {

        Module module = ModulesClasspathJarDemo.class.getModule();

        System.out.println("Module: " + module);
        System.out.println( "Module name: " + module.getName());
        System.out.println("Classpath:");
        System.out.println(System.getProperty("java.class.path" ));
    }
}