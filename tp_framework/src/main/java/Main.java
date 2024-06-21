import nocito.framework.Framework;

public class Main {
    public static void main(String[] args) {
        Framework framework = new Framework(
                "/home/jose/Escritorio/tp final framework/tp_framework/src/main/resources/config.properties");
        framework.mostrarMenu();
    }
}