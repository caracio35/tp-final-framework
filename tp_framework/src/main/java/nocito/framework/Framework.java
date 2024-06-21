package nocito.framework;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Framework {
    private List<Accion> acciones = new ArrayList<>();
    private int maxThreads;
    private Map<Integer, Integer> indiceAccionMap = new HashMap<>();

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

            // Cargar acciones desde el archivo de configuración
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

            // Mapear índices del menú a índices de acciones
            for (int i = 0; i < acciones.size(); i++) {
                indiceAccionMap.put(i + 1, i); // Índice del menú (1-based) -> Índice de acción (0-based)
            }

        } catch (IOException e) {
            System.out.println("Error leyendo el archivo de configuración: " + configPath);
            e.printStackTrace();
        }
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n Seleccione una o más acciones separadas por espacio:");
            for (int indiceMenu : indiceAccionMap.keySet()) {
                int indiceAccion = indiceAccionMap.get(indiceMenu);
                System.out.println(indiceMenu + ": " + acciones.get(indiceAccion).getClass().getSimpleName());
            }
            System.out.println((acciones.size() + 1) + ": para terminar.");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase((acciones.size() + 1) + "")) {
                salir = true;
            } else {
                String[] seleccion = input.split(" ");
                List<Integer> indices = new ArrayList<>();
                boolean seleccionValida = true;

                for (String s : seleccion) {
                    try {
                        int indiceSeleccionado = Integer.parseInt(s);
                        if (indiceAccionMap.containsKey(indiceSeleccionado)) {
                            indices.add(indiceAccionMap.get(indiceSeleccionado));
                        } else {
                            System.out.println("Índice inválido: " + indiceSeleccionado);
                            seleccionValida = false;
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida: " + s);
                        seleccionValida = false;
                        break;
                    }
                }

                if (seleccionValida && !indices.isEmpty()) {
                    ejecutarAcciones(indices);
                }
            }
        }

        System.out.println("Saliendo del programa...");
    }

    private void ejecutarAcciones(List<Integer> indices) {
        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
        // executor.invokeAll(indices.stream().map(i -> new Callable<Void>() {
        // @Override
        // public Void call() {
        // acciones.get(i).ejecutar();
        // return null;
        // }
        // }).collect(Collectors.toList()));

        try {
            for (int index : indices) {
                if (index >= 0 && index < acciones.size()) {
                    Accion accion = acciones.get(index);
                    executor.submit(() -> {
                        accion.ejecutar();
                        return null;
                    });
                } else {
                    System.out.println("Índice de acción fuera de rango: " + index);
                }
            }
        } finally {
            executor.shutdown();
        }
    }
}
