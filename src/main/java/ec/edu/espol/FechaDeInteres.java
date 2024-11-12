package ec.edu.espol;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class FechaDeInteres {
    String nombre;
    LocalDate fecha;
    
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    
    public FechaDeInteres(String nombre, LocalDate fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    
    public static FechaDeInteres next(Scanner scanner, String indentation) {
        System.out.print(indentation+"- nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print(indentation+"- fecha ("+DATE_FORMAT+"): ");
        String fecha_string = scanner.nextLine();
        

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fecha_string, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            fecha = LocalDate.now();
            System.out.println(indentation+indentation+"LA FECHA NO ESTÁ EN EL FORMATO CORRECTO. Se usó la fecha actual.");
        }
        
        return new FechaDeInteres(nombre, fecha);
    }
    
    public static FechaDeInteres next(Scanner scanner) {
        return next(scanner, "");
    }
}
