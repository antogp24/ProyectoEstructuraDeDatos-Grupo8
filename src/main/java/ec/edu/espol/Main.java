package ec.edu.espol;

import java.util.Scanner;

import java.util.Arrays;

public class Main {
    static MyList<Contacto> contactos = new MyList<>();
    static int indice_contacto_activo = 0;
    static boolean running_app = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ver_comandos();
        while (running_app){
            System.out.print("\nIngresa un comando: ");
            String comando = scanner.nextLine();
            process_command(comando, scanner);
        }
    }

    static final class Cmd_Desc_Pair {
        String command;
        String description;

        public Cmd_Desc_Pair(String command, String description) {
            this.command = command;
            this.description = description;
        }
    }

    static final Cmd_Desc_Pair[] comandos = {
        new Cmd_Desc_Pair("ayuda",              "Imprimir esta ayuda"),
        new Cmd_Desc_Pair("contacto nuevo",     "Crear un contacto nuevo"),
        new Cmd_Desc_Pair("contacto lista",     "Visualizar la lista de contactos"),
        new Cmd_Desc_Pair("contacto editar",    "Editar un contacto existente"),
        new Cmd_Desc_Pair("contacto eliminar",  "Eliminar un contacto existente"),
        new Cmd_Desc_Pair("contacto anterior",  "Mover el cursor al contacto anterior"),
        new Cmd_Desc_Pair("contacto siguiente", "Mover el cursor al contacto siguiente"),
        new Cmd_Desc_Pair("cerrar",             "Cerrar la aplicación"),
    };

    static void process_command(String command, Scanner scanner) {
        String[] parts = command.split(" ");
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        switch (parts[0]) {
            case "ayuda": command_ayuda(args); break;
            case "cerrar": command_cerrar(args); break;
            case "contacto": command_contacto(args, scanner); break;
            default: {
                System.out.println("Comando inválido: \"" + parts[0]+ '"');
                 System.out.println("    Escribe \"ayuda\" para ver los comandos válidos");
            }
        }
    }

    static void command_contacto(String[] args, Scanner scanner) {
        if (args.length != 1) {
            System.out.println("Se esperaba 1 argumento para este comando.");
            System.out.println("Intentalo de nuevo.");
            return;
        }
        switch (args[0]) {
            case "nuevo": {
                Contacto nuevo = Contacto.next(scanner);
                contactos.add(nuevo);
                try {
                    Contacto.guardarLista(contactos);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } break;

            case "lista": {
                visualize_commands();
                contactos=Contacto.cargarContactos();
                MyList< Contacto>.CustomMyListIterator iterator = contactos.iterator();
                while (iterator.hasNext()) {
                    System.out.println(iterator.next()+"\n");
                }

            } break;

            case "siguiente": {
                indice_contacto_activo = (indice_contacto_activo + 1) % contactos.size();
                visualize_commands();
            } break;

            case "anterior": {
                indice_contacto_activo = mod(indice_contacto_activo - 1, contactos.size());
                visualize_commands();
            } break;

            case "editar": {
                 editar_contacto(scanner);
            } break;

            case "eliminar": {
                eliminar_contacto(scanner);
            } break;

            default: {
                System.out.println("Argumento inválido: \"" + args[0] + '"');
                 System.out.println("    Escribe \"ayuda\" para ver los argumentos válidos");
            }
        }
    }

    static void command_cerrar(String[] args) {
        if (args.length != 0) {
            System.out.println("Se esperaba 0 argumentos para este comando.");
            System.out.println("Intentalo de nuevo.");
            return;
        }
        running_app = false;
    }

    static void command_ayuda(String[] args) {
        if (args.length != 0) {
            System.out.println("Se esperaba 0 argumentos para este comando.");
            System.out.println("Intentalo de nuevo.");
            return;
        }
        for (int i = 0; i < comandos.length; i++) {
            System.out.printf("| %-20s | %-45s |\n", comandos[i].command, comandos[i].description);
        }
    }

    static void ver_comandos(){
        System.out.println("-------------------------"+" Comandos Generales " + "-------------------------");
        for (int i = 0; i < comandos.length; i++) {
            System.out.printf("| %-20s | %-45s |\n", comandos[i].command, comandos[i].description);
        }
    }

    static void visualize_commands() { // cambiar el nombre a visualize_contacts
        for (int i = 0; i < contactos.size(); i++) {
            String nombre = contactos.get(i).nombre;
            String cursor = (i == indice_contacto_activo) ? "->" : "  ";

            System.out.printf("%s contacto #%d: '%s'\n", cursor, i+1, nombre);
        }
    }

    static int mod(int a, int b) {
        return (a % b + b) % b;
    }

    static void editar_contacto(Scanner scanner){
        System.out.println("Ingrese el teléfono del contacto a editar: ");
                String telefono_a_editar = scanner.nextLine();
                contactos=Contacto.cargarContactos();
                MyList< Contacto>.CustomMyListIterator iteratorContactos = contactos.iterator();
                // Recorrer todos los elementos
                while (iteratorContactos.hasNext()) {
                    Contacto actual = new Contacto();
                    actual = iteratorContactos.next();
                    MyList<String> telefonos = actual.numeros_de_telefono;
                    MyList< String>.CustomMyListIterator iteratorTelefonos = telefonos.iterator(); 
                    while (iteratorTelefonos.hasNext()) { // se itera para buscar un teléfono que coincida sin necesidad de que sea el primero
                        if (telefono_a_editar.equals(iteratorTelefonos.next())){
                            int indice= contactos.find(actual);
                            Contacto nuevo = Contacto.next(scanner);
                            contactos.set(indice, nuevo);
                            System.out.println("Contacto editado exitosamente");
                            try {
                                Contacto.guardarLista(contactos);
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }        
                }
                System.out.println("Contacto no encontrado");
               
    }

    static void eliminar_contacto(Scanner scanner){
        System.out.println("Ingrese el teléfono del contacto a eliminar: ");
                String telefono_a_editar = scanner.nextLine();
                contactos=Contacto.cargarContactos();
                MyList< Contacto>.CustomMyListIterator iteratorContactos = contactos.iterator();
                // Recorrer todos los elementos
                while (iteratorContactos.hasNext()) {
                    Contacto actual = new Contacto();
                    actual = iteratorContactos.next();
                    MyList<String> telefonos = actual.numeros_de_telefono;
                    MyList< String>.CustomMyListIterator iteratorTelefonos = telefonos.iterator(); 
                    while (iteratorTelefonos.hasNext()) { // se itera para buscar un teléfono que coincida sin necesidad de que sea el primero
                        if (telefono_a_editar.equals(iteratorTelefonos.next())){
                            iteratorContactos.remove();
                            System.out.println("Contacto eliminado exitosamente");
                            try {
                                Contacto.guardarLista(contactos);
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }        
                }
                System.out.println("Contacto no encontrado");
               
    }
}
