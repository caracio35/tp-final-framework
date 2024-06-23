package nocito.framework;

import java.util.concurrent.Callable;

public class AccionAdapter implements Callable<Void> {
    private Accion accion;

    public AccionAdapter(Accion accion) {
        this.accion = accion;
    }

    @Override
    public Void call() {
        accion.ejecutar();
        return null;
    }

    public void setAccion(Accion accion) {
        this.accion = accion;
    }

    public Accion getAccion() {
        return accion;
    }
}
