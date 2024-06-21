package nocito.framework;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Framework {
    private List<Accion> acciones = new ArrayList<>();
    private int maxThreads;

    public Framework(String configPath) {
        cargarConfiguraciones(configPath);

    }

    private void cargarConfiguraciones(String configPath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(new File(configPath));
            JsonNode accionesNode = root.path("acciones");
            JsonNode maxThreadsNode = root.path("max-threads");

            maxThreads = maxThreadsNode.asInt();

            for (JsonNode accionNode : accionesNode) {
                String accionClassName = accionNode.asText();
                try {
                    Class<?> accionClass = Class.forName(accionClassName);
                    Accion accion = (Accion) accionClass.getDeclaredConstructor().newInstance();
                    acciones.add(accion);
                } catch (Exception e) {
                    System.out.println("Error cargando la clase: " + accionClassName);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo el archivo de configuración: " + configPath);
            e.printStackTrace();
        }
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione una o más acciones separadas por espacios:");
        for (int i = 0; i < acciones.size(); i++) {
            System.out.println(i + ": " + acciones.get(i).getClass().getSimpleName());
        }

        String input = scanner.nextLine();
        String[] seleccion = input.split(" ");
        List<Integer> indices = new ArrayList<>();

        try {
            for (String s : seleccion) {
                indices.add(Integer.parseInt(s));
            }
            ejecutarAcciones(indices);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese números válidos.");
        }

    }

    public void ejecutarAcciones(List<Integer> seleccion) {
        List<Callable<Void>> tareas = new ArrayList<>();
        for (Integer index : seleccion) {
            Accion accion = acciones.get(index);
            tareas.add(new AccionAdapter(accion));
        }

        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
        try {
            executor.invokeAll(tareas);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}