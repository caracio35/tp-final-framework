package nocito.utilizacion;

import nocito.framework.Accion;

public class accionCuatro implements Accion {

    @Override
    public void ejecutar() {
        System.out.println("Acción cuatro");
    }

    @Override
    public String nombreItemMenu() {
        return "Acción 4";
    }

    @Override
    public String descripcionItemMenu() {
        return "esto es para traer a messi";
    }

}
