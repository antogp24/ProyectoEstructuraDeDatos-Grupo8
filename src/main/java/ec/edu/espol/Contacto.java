package ec.edu.espol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Contacto implements Serializable{
    String nombre;                                      // No tiene que ser único
    Foto foto;                                          // Foto de la persona
    String direccion;                                   // ejemplo: Ecuador, Guayaquil
    MyList<String> emails;                              // El primero es el principal
    MyList<String> numeros_de_telefono;                 // El primero es el principal
    MyList<String> identificadores_de_redes_sociales;   // ejemplo: #hola
    MyList<FechaDeInteres> fechas_de_interes;           // Puede estar vacío
    MyList<String> contactos_relacionados;              // Cada uno es un número de teléfono
    String pais;                                        // País de orígen o residencia.
    String ciudad;                                      // Ciudad de orígen o residencia.
    public static final String nomArchivo = "contactos.ser"; 

    private static int nextCount(Scanner scanner, String message) {
        int result = -1;
        do {
            try {
                System.out.print(message);
                result = scanner.nextInt();
                scanner.nextLine();
                if (result < 0)
                    System.out.println("Debe ser mayor o igual a cero. Intenta de nuevo.");
            } catch (InputMismatchException e) {
                System.out.println("Debe ser un entero. Intenta de nuevo.");
                scanner.nextLine();
            }
        } while(result < 0);
        return result;
    }
    
    public static Contacto next(Scanner scanner) {
        System.out.print("Tipo (Ingrese 'P' para persona o 'E' para empresa): ");
        String tipo = scanner.nextLine();
        while (!(tipo.equalsIgnoreCase("P") || tipo.equalsIgnoreCase("E") )){
            System.out.println("Ingrese un tipo de contacto válido: "); 
            System.out.print("Tipo (Ingrese 'P' para persona o 'E' para empresa): ");
            tipo = scanner.nextLine();
        }
        
        Contacto contacto;
        if (tipo.equalsIgnoreCase("E")) {
            contacto = new ContactoEmpresa();
        } else {
            contacto = new ContactoPersonal();
        }

        System.out.print("Nombre: ");
        contacto.nombre = scanner.nextLine();

        if (contacto instanceof ContactoPersonal contacto_personal) {
            System.out.print("Apellido: ");
            contacto_personal.apellido = scanner.nextLine();
        }
        
        System.out.print("Foto (path): ");
        contacto.foto = new Foto(scanner.nextLine());
        
        System.out.print("Dirección: ");
        contacto.direccion = scanner.nextLine();
        
        int emails_count = nextCount(scanner, "Cuantos emails?: ");
        contacto.emails = new MyList<>(emails_count, true);
        for (int i = 0; i < emails_count; i++) {
            System.out.printf("Email %d: ", i+1);
            String input = scanner.nextLine();
            contacto.emails.set(i, input);
        }
        
        int numeros_de_telefono_count = nextCount(scanner, "Cuantos numeros_de_telefono?: ");
        contacto.numeros_de_telefono = new MyList<>(numeros_de_telefono_count, true);
        for (int i = 0; i < numeros_de_telefono_count; i++) {
            System.out.printf("Numero de teléfono %d: ", i+1);
            String input = scanner.nextLine();
            contacto.numeros_de_telefono.set(i, input);
        }
        
        int identificadores_de_redes_sociales_count = nextCount(scanner, "Cuantos identificadores de redes sociales?: ");
        contacto.identificadores_de_redes_sociales = new MyList<>(identificadores_de_redes_sociales_count, true);
        for (int i = 0; i < identificadores_de_redes_sociales_count; i++) {
            System.out.printf("Identificador %d: ", i+1);
            String input = scanner.nextLine();
            contacto.identificadores_de_redes_sociales.set(i, input);
        }
        
        int fechas_de_interes_count = nextCount(scanner, "Cuantas fechas de interes?: ");
        contacto.fechas_de_interes = new MyList<>(fechas_de_interes_count, true);
        for (int i = 0; i < fechas_de_interes_count; i++) {
            System.out.printf("Fecha de interés %d:\n", i+1);
            FechaDeInteres input = FechaDeInteres.next(scanner, "\t");
            contacto.fechas_de_interes.set(i, input);
        }
        
        int contactos_relacionados_count = nextCount(scanner, "Cuantos contactos relacionados?: ");
        contacto.contactos_relacionados = new MyList<>(contactos_relacionados_count, true);
        for (int i = 0; i < contactos_relacionados_count; i++) {
            System.out.printf("Número de teléfono de contacto relacionado %d: ", i+1);
            String input = scanner.nextLine();
            contacto.contactos_relacionados.set(i, input);
        }

        if (contacto instanceof ContactoEmpresa contacto_empresa) {
            int sucursales_count = nextCount(scanner, "Cuantas sucursales?: ");
            contacto_empresa.sucursales = new MyList<>(sucursales_count, true);
            for (int i = 0; i < sucursales_count; i++) {
                System.out.printf("Nombre o ubicación de sucursal %d: ", i+1);
                String input = scanner.nextLine();
                contacto_empresa.sucursales.set(i, input);
            }
        }

        System.out.print("País: ");
        contacto.pais = scanner.nextLine();
        
        System.out.print("Ciudad: ");
        contacto.ciudad = scanner.nextLine();

        System.out.println("Contacto agregado exitosamente.");
        return contacto;
    }

    @Override
    public String toString() {
        return "    - Nombre: " + nombre + '\n' +
               "    - Foto: " + foto.path + '\n' +
               "    - Emails: " + emails+ '\n' +
               "    - Telefonos: " + numeros_de_telefono+ '\n' +
               "    - Identificadores: " + identificadores_de_redes_sociales+ '\n' +
               "    - Fechas de interes: " + fechas_de_interes+ '\n' + 
               "    - Contactos relacionados: " + contactos_relacionados + '\n' +
               "    - País: " + pais + '\n' +
               "    - Ciudad: " + ciudad + '\n';
    }

    // Metodos para serializar y deserializar

    public static void guardarContactos(CircularLinkedList<Contacto> contactos) throws Exception {
        File f = new File(nomArchivo);

        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f))) {
            os.writeObject(contactos);
        } catch (IOException e) {
            //quizas lanzar una excepcion personalizada
            throw new Exception(e.getMessage());
        }
    }

    // Lee el archivo donde se encuentran los datos
    @SuppressWarnings("unchecked")
    public static CircularLinkedList<Contacto> cargarContactos (){
        CircularLinkedList<Contacto> contactos = new CircularLinkedList<>();
        File f = new File(nomArchivo);

        // Se escribe la lista serializada
        if (f.exists()) {
            // Si no existe se crea la lista
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(f))) {
                contactos = (CircularLinkedList<Contacto>)is.readObject();
            } catch (Exception e) {
                // Quizas lanzar una excepcion personalizada
                new Exception(e.getMessage());
            }
        }
        return contactos;
    }

    public LocalDate obtenerFechaNacimiento() {
        for (FechaDeInteres fecha: fechas_de_interes) {
            if (fecha != null && "Nacimiento".equalsIgnoreCase(fecha.getNombre())) {
                return fecha.getFecha();
            }
        }
        return null; // Si no tiene fecha de "Nacimiento", devolvemos null
    }

}