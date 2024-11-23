package ec.edu.espol;

import java.time.LocalDate;
import java.util.Comparator;

public class Comparadores {
     public static class ComparadorPorNombreCompleto implements Comparator<Contacto> {
    @Override
    public int compare(Contacto c1, Contacto c2) {
        return c1.nombre.compareToIgnoreCase(c2.nombre);
    }
}
    
    public static class ComparadorPorCantidadDeTelefonos implements Comparator<Contacto> {
    @Override
    public int compare(Contacto c1, Contacto c2) {
        return Integer.compare(c2.numeros_de_telefono.size(), c1.numeros_de_telefono.size()); // Más teléfonos primero
    }
} 

    
    public static class ComparadorPorIdentificadorRedesSociales implements Comparator<Contacto> {
        @Override
        public int compare(Contacto c1, Contacto c2) {
            return Integer.compare(c2.identificadores_de_redes_sociales.size(), c1.identificadores_de_redes_sociales.size());
        }
    }
    
    //Necesita de obtenerFechaNacimiento
     
    public static class ComparadorPorCumpleanos implements Comparator<Contacto> {
   
   
        @Override
    public int compare(Contacto c1, Contacto c2) {
        LocalDate fechaNacimiento1 = Contacto.obtenerFechaNacimiento(c1);
        LocalDate fechaNacimiento2 = Contacto.obtenerFechaNacimiento(c2);

        // Si ambos son nulos, son iguales
        if (fechaNacimiento1 == null && fechaNacimiento2 == null) {
            return 0;
        }
        // Si solo uno es nulo, el que es nulo va después
        if (fechaNacimiento1 == null) {
            return 1; // c1 va después
        }
        if (fechaNacimiento2 == null) {
            return -1; // c2 va después
        }

        // Comparar las fechas de nacimiento (LocalDate implementa Comparable)
        return fechaNacimiento1.compareTo(fechaNacimiento2);
    }
    }
    

    public static class ComparadorPorTipoDeContacto implements Comparator<Contacto> {
        @Override
        public int compare(Contacto c1, Contacto c2) {
            // Primero, comprobamos si ambos objetos son de la misma clase
            if (c1 instanceof ContactoEmpresa && c2 instanceof ContactoPersonal) {
                return -1; // Las empresas primero
            }
            if (c1 instanceof ContactoPersonal && c2 instanceof ContactoEmpresa) {
                return 1; // Las personas después
            }
            return 0;
        }
    }
}
