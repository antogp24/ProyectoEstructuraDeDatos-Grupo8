package ec.edu.espol;

public class ContactoEmpresa extends Contacto {
    MyList<String> sucursales;

    @Override
    public String toString() {
        return "Contacto (Empresa):" + '\n' +
               super.toString() +
               "    - Sucursales: " + sucursales + '\n';
    }
}
