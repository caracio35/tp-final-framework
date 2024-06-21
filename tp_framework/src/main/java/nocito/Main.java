package nocito;

import nocito.framework.Framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Usa el ClassLoader para obtener el archivo de configuración desde resources
        String configFilePath = Main.class.getClassLoader()
                .getResource(
                        "\\home\\jose\\Escritorio\\tp_final_framework\\tp_framework\\src\\main\\resources\\config.json")
                .getPath();

        if (configFilePath == null) {
            System.out.println("No se pudo encontrar el archivo de configuración config.json en el classpath.");
            return;
        }

        Framework framework = new Framework(configFilePath);

        // Simular selección de acciones desde el menú
        Scanner scanner = new Scanner(System.in);
        List<Integer> seleccion = new ArrayList<>();
        System.out.println("Ingrese los números de las acciones que desea ejecutar, separados por espacios:");
        String input = scanner.nextLine();
        String[] indices = input.split(" ");
        for (String index : indices) {
            seleccion.add(Integer.parseInt(index));
        }

        framework.ejecutarAcciones(seleccion);
    }
}
