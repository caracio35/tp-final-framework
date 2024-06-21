package nocito.utilizacion;

import nocito.framework.Accion;

public class AccionDos implements Accion {

    @Override
    public void ejecutar() {
        System.out.println("ejecutando acciondos");
    }

    @Override
    public String nombreItemMenu() {
        return "Accion 2";
    }

    @Override
    public String descripcionItemMenu() {
        return "esto trae las primeras diez personas de la BD...";
    }

}
