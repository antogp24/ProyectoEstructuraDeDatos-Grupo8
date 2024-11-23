package ec.edu.espol;

import java.util.Scanner;

import java.util.Arrays;

public class Main {
    static CircularLinkedList<Contacto> contactos;
    static CircularLinkedListIterator<Contacto> cursor_contactos;
    static boolean running_app = true;

    static final boolean AUTOGUARDADO = true;

    public static void main(String[] args) {
        contactos = Contacto.cargarContactos();
        cursor_contactos = contactos.iterator();

        Scanner scanner = new Scanner(System.in);
        ver_comandos();
        while (running_app) {
            System.out.print("\nIngresa un comando: ");
            String comando = scanner.nextLine();
            process_command(comando, scanner);
        }
        guardarProgreso();
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
        new Cmd_Desc_Pair("contacto editar",    "Editar el contacto del cursor"),
        new Cmd_Desc_Pair("contacto #editar",   "Editar un contacto a partir de su número"),
        new Cmd_Desc_Pair("contacto eliminar",  "Eliminar el contacto del cursor"),
        new Cmd_Desc_Pair("contacto #eliminar", "Eliminar un contacto a partir de su número"),
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
                cursor_contactos.reset();
                if (AUTOGUARDADO) guardarProgreso();
            } break;

            case "lista": {
                ver_contactos();
            } break;

            case "siguiente": {
                cursor_contactos.next();
                ver_contactos();
            } break;

            case "anterior": {
                cursor_contactos.prev();
                ver_contactos();
            } break;

            case "editar": {
                editar_contacto_del_cursor(scanner);
            } break;

            case "#editar": {
                editar_contacto_por_numero(scanner);
            } break;

            case "eliminar": {
                eliminar_contacto_del_cursor();
            } break;

            case "#eliminar": {
                eliminar_contacto_por_numero(scanner);
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
        ver_comandos();
    }

    static void ver_comandos() {
        System.out.printf("+----------------------------------------------------------------------+\n");
        System.out.printf("|                          Comandos Generales                          |\n");
        System.out.printf("+----------------------+-----------------------------------------------+\n");
        for (int i = 0; i < comandos.length; i++) {
            System.out.printf("| %-20s | %-45s |\n", comandos[i].command, comandos[i].description);
        }
        System.out.printf("+----------------------+-----------------------------------------------+\n");
    }

    static void ver_contactos() {
        int i = 0;
        CircularLinkedListIterator<Contacto> it = contactos.iterator();
        while (it.hasNext() && !it.hasLooped()) {
            Contacto contacto = it.next();
            String cursor = (it.getCurrentNode() == cursor_contactos.getCurrentNode()) ? "->" : "  ";
            char tipo = (contacto instanceof ContactoPersonal) ? 'P' : 'E';
            System.out.printf("%s [%c] contacto #%d: '%s'\n", cursor, tipo, i+1, contacto.nombre);
            i++;
        }
    }

    static void editar_contacto_del_cursor(Scanner scanner) {
        if (contactos.isEmpty()) {
            System.out.println("No hay contactos en la lista de contactos.");
            return;
        }
        Contacto nuevo = Contacto.next(scanner);
        cursor_contactos.changeCurrentNodeValue(nuevo);
        System.out.println("Contacto editado exitosamente");
        if (AUTOGUARDADO) guardarProgreso();
    }

    static void editar_contacto_por_numero(Scanner scanner) {
        System.out.print("Ingrese el teléfono del contacto a editar: ");
        String telefono_a_editar = scanner.nextLine();

        // Recorrer todos los contactos
        CircularLinkedListIterator<Contacto> it = contactos.iterator();
        while (it.hasNext() && !it.hasLooped()) {
            Contacto actual = it.next();

            // Buscar el que se va a editar
            for (String telefono: actual.numeros_de_telefono) {
                if (telefono_a_editar.equals(telefono)) {
                    Contacto nuevo = Contacto.next(scanner);
                    it.changeConsumedNodeValue(nuevo);
                    System.out.println("Contacto editado exitosamente");
                    if (AUTOGUARDADO) guardarProgreso();
                    return;
                }
            }        
        }
        System.out.println("Contacto no encontrado");
    }

    static void eliminar_contacto_del_cursor() {
        cursor_contactos.remove();
        System.out.println("Contacto eliminado exitosamente");
        if (AUTOGUARDADO) guardarProgreso();
    }

    static void eliminar_contacto_por_numero(Scanner scanner) {
        System.out.print("Ingrese el teléfono del contacto a eliminar: ");
        String telefono_a_eliminar = scanner.nextLine();

        // Recorrer todos los contactos
        CircularLinkedListIterator<Contacto> it = contactos.iterator();
        while (it.hasNext() && !it.hasLooped()) {
            Contacto actual = it.next();

            // Buscar el que se va a editar
            for (String telefono: actual.numeros_de_telefono) {
                if (telefono_a_eliminar.equals(telefono)) {
                    it.removeConsumed();
                    System.out.println("Contacto eliminado exitosamente");
                    if (AUTOGUARDADO) guardarProgreso();
                    cursor_contactos.reset();
                    return;
                }
            }        
        }
        System.out.println("Contacto no encontrado");
    }

    static void guardarProgreso() {
        try {
            Contacto.guardarContactos(contactos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
