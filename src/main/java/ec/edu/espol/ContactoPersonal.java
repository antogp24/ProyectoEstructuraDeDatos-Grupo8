package ec.edu.espol;

public class ContactoPersonal extends Contacto {
    String apellido;


    @Override
    public String toString() {
        return "Contacto: \n" +
                "Nombre = " + nombre + '\n' +
                "Apellido = " + apellido + '\n' +
                "Foto = " + foto.path + '\n' +
                "Emails = " + emails+ '\n' +
                "Telefonos = " + numeros_de_telefono+ '\n' +
                "Identificadores = " + identificadores_de_redes_sociales+ '\n' +
                "Fechas de interes = " + fechas_de_interes+ '\n' + 
                "Contactos relacionados = " + contactos_relacionados
                ;
    }
}
