package ec.edu.espol;

public class ContactoPersonal extends Contacto {
    String apellido;

    @Override
    public String toString() {
        return "Contacto (Personal):" + '\n' +
               "    - Apellido: " + apellido + '\n' + 
               super.toString();
    }
}
