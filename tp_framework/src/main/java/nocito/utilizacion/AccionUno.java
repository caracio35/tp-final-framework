package nocito.utilizacion;

import nocito.framework.Accion;

public class AccionUno implements Accion {

    @Override
    public void ejecutar() {
        System.out.println("ejecutando accion uno");
    }

    @Override
    public String nombreItemMenu() {
        return "Accion 1";
    }

    @Override
    public String descripcionItemMenu() {
        return "esto es para traer los twtts de Maradona";
    }

}
