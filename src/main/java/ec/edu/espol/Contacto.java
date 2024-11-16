package ec.edu.espol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.channels.Pipe.SourceChannel;
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
        Contacto contacto = new Contacto();
        
        System.out.print("Nombre: ");
        contacto.nombre = scanner.nextLine();
        
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
        
        return contacto;
    }

    @Override
    public String toString() {
        return "Contacto: \n" +
                "Nombre = " + nombre + '\n' +
                "Foto = " + foto.path + '\n' +
                "Emails = " + emails+ '\n' +
                "Telefonos = " + numeros_de_telefono+ '\n' +
                "Identificadores = " + identificadores_de_redes_sociales+ '\n' +
                "Fechas de interes = " + fechas_de_interes+ '\n' + 
                "Contactos relacionados = " + contactos_relacionados
                ;
    }

    

    // Metodos para serializar y deserializar

    public static boolean guardarLista(MyList<Contacto> contactos) throws Exception{
        boolean guardado = false;
        File f = new File(nomArchivo);
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f))) {
            os.writeObject(contactos);
            guardado = true;
        } catch (IOException e) {

            //quizas lanzar una excepcion personalizada
            throw new Exception(e.getMessage());
        }
        return guardado;
    }

    //lee el archivo donde se encuentran los datos
    public static MyList<Contacto> cargarContactos (){
        MyList<Contacto> contactos = new MyList<>();
        File f = new File(nomArchivo);
        //se escribe la lista serializada
        if ( f.exists()) { //si no existe se crea la lista
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(f))) {
                contactos = (MyList<Contacto>) is.readObject();

            } catch (Exception e) {
                //quizas lanzar una excepcion personalizada
                new Exception(e.getMessage());
            }
        }
        return contactos;
    }

}