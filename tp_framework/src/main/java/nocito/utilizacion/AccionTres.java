package nocito.utilizacion;

import nocito.framework.Accion;

public class AccionTres implements Accion {
    @Override
    public void ejecutar() {
        System.out.println("ejecutando accion tres");
    }

    @Override
    public String nombreItemMenu() {
        return "Accion 3";
    }

    @Override
    public String descripcionItemMenu() {
        return "esto es para traer a messi";
    }

}
