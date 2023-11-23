// Importación de librerías necesarias
import java.util.Random;
import javax.swing.JOptionPane;
import java.util.Scanner;

// Definición de la clase principal
public class Bingo {

    // Constantes para el formato del texto
    public static final String ANSI_RESET = "\u001B[0m"; //Volver a la configuracion predeterminada luego de aplicar un color
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";// Cambia el fondo por verde en la consola

    // Scanner global para la entrada de datos
    private static final Scanner scanner = new Scanner(System.in);

    // matriz 2D para almacenar el historial de letras y números llamados
    private static String[][] historial;
    // Índice para seguir un registro del historial
    private static int indiceHistorial;

    private static int cantidad;

    // Método principal
    public static void main(String[] args) {

        // Declaración de variables
        int modoDeJuego = solicitarModoDeJuego();
        int cantidadCartones = solicitarCantidadCartones();

        cantidad = cantidadCartones;
        String[] nombresCartones = new String[cantidadCartones];
        int[][] cartones = new int[cantidadCartones][25]; // representa TODOS los cartones de BINGO (parte numerica)
        boolean[] cartonLleno = new boolean[cantidadCartones]; // representa el estado de los cartones TRUE para lleno y FALSE para no lleno

        // Inicialización del historial
        historial = new String[5 * 15][2];
        indiceHistorial = 0;

        // Generación e impresión de los cartones numerados
        for (int i = 0; i < cantidadCartones; i++) {
            nombresCartones[i] = "Cartón " + (i + 1);  // este arreglo almacena como nombres de cartones los numeros del 1 a cantidadCartones
            generarCarton(cartones[i]);
            cartonLleno[i] = false;
        }

        //Impresion de los cartones al inicio para que se puedan escoger
        imprimirNumerosCartones(cartones, nombresCartones);

        // Mensaje y asignación de nombres a los cartones
        asignarCartones();
        for (int i = 0; i < cantidadCartones; i++) {
            nombresCartones[i] = solicitarNombreCarton(i + 1);
        }

        // Generación e impresión de cartones con nombres asignados
        for (int i = 0; i < cantidadCartones; i++) {
            generarCarton(cartones[i]);
            cartonLleno[i] = false;
        }
        //Cartones completos con sus nombres asignados
        imprimirNumerosCartones(cartones, nombresCartones);

        // Mensaje de inicio del juego
        iniciarJuego();

        // Inicio del juego
        Random random = new Random();
        String[] letras = {"B", "I", "N", "G", "O"};
        boolean juegoTerminado = false;

        while (!juegoTerminado) {
            String letra = letras[random.nextInt(5)];
            int numero = generarNumeroPorColumna(letra);

            // Verificar si la combinación ya ha sido seleccionada
            while (combinacionRepetida(letra, numero)) {
                letra = letras[random.nextInt(5)];
                numero = generarNumeroPorColumna(letra);
            }

            // Agregar la combinación al historial
            agregarAlHistorial(letra, numero);

            imprimirNumerosCartones(cartones, nombresCartones);

            System.out.println("\nNúmero llamado: " + letra + " " + numero + "\n");

            for (int i = 0; i < cantidadCartones; i++) {
                if (!cartonLleno[i] && marcarCarton(cartones[i], letra, numero)) {
                    cartonLleno[i] = verificarBingo(modoDeJuego, cartones[i]);
                    if (cartonLleno[i]) {
                        imprimirNumerosCartones(cartones, nombresCartones); // Al hacer BINGO se termina el juego por lo que se tiene que imprimir desde aqui para que se muestren los cartones antes del mensaje del BINGO
                        JOptionPane.showMessageDialog(null, nombresCartones[i] + " hizo BINGO!!!");
                        imprimirHistorial();
                        juegoTerminado=true;
                    } else {
                        JOptionPane.showMessageDialog(null, nombresCartones[i] + " coincidió con " + letra + " " + numero);
                    }
                }
            }
            //juegoTerminado = verificarJuegoTerminado(cartonLleno); SI SE QUIERE SEGUIR JUGANDO LUEGO DE QUE ALGUIEN HIZO EL BINGO

            if (!juegoTerminado) {
                // Imprime los cartones mientras el juego no se haya terminado
                imprimirNumerosCartones(cartones, nombresCartones);
                imprimirHistorial();
                presionarEnter();
            }
        }
    }

    // Metodo para evitar que se llamen combinaciones de letras y números que ya han sido anunciadas en el juego.
    private static boolean combinacionRepetida(String letra, int numero) {
        for (int i = 0; i < indiceHistorial; i++) {
            if (letra.equals(historial[i][0]) && numero == Integer.parseInt(historial[i][1])) {
                return true;
            }
        }
        return false;
    }

    // Método para solicitar la cantidad de cartones
    private static int solicitarCantidadCartones() {
        int cantidadCartones = 0;

        // Bucle que solicita la cantidad de cartones hasta que se ingresa un valor válido (entre 1 y 12)
        while (cantidadCartones < 1 || cantidadCartones > 12) {
            // Solicitar al usuario que ingrese la cantidad de cartones
            String input = JOptionPane.showInputDialog("Ingrese la cantidad de cartones (1-12):");

            try {
                // Intentar convertir la entrada a un número entero
                cantidadCartones = Integer.parseInt(input);

                // Verificar si la cantidad de cartones está en el rango válido
                if (cantidadCartones < 1 || cantidadCartones > 12) {
                    // Mostrar mensaje de error si la cantidad de cartones no está en el rango válido
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido (1-12).");
                }
            } catch (NumberFormatException e) {
                // En caso de que no se ingrese un número válido.
                // Mostrar mensaje de error al usuario
                JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido.");
            }
        }

        // Devolver la cantidad de cartones válida
        return cantidadCartones;
    }

    private static Integer solicitarModoDeJuego() {
        Object[] opciones = {"Modo O", "Modo X", "Carton completo"};
        return JOptionPane.showOptionDialog(
                null,
                "Elige el modo de juego:",
                "Seleccionar modo de juego",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
    }

    // Método para solicitar el nombre de un cartón
    private static String solicitarNombreCarton(int numeroCarton) {
        return JOptionPane.showInputDialog("Nombre para el cartón " + numeroCarton + " (máximo 15 caracteres):");
    }

    private static void generarCarton(int[] carton) {
        Random random = new Random(); // Crear una instancia de la clase Random para generar números aleatorios

        for (int i = 0; i < 5; i++) { // Iterar sobre las filas del cartón
            for (int j = 0; j < 5; j++) { // Iterar sobre las columnas del cartón
                if (j == 2 && i == 2) {
                    // Si estamos en la posición central (fila 2, columna 2), asignar 1000 (espacio libre)
                    carton[i * 5 + j] = 1000;
                } else {
                    int nuevoNumero;
                    boolean numeroRepetido;

                    // Generar un nuevo número para la columna actual sin repetirlo en la misma columna
                    do {
                        numeroRepetido = false;
                        nuevoNumero = generarNumeroPorColumna(getColumnaLetra(j));

                        // Verificar si el nuevo número ya existe en la columna actual
                        for (int k = 0; k < i; k++) {
                            if (carton[k * 5 + j] == nuevoNumero) {
                                numeroRepetido = true;
                                break;
                            }
                        }
                    } while (numeroRepetido);

                    // Asignar el nuevo número al cartón
                    carton[i * 5 + j] = nuevoNumero;
                }
            }
        }
    }


    // Método para generar un número según la columna (letra) del cartón
    private static int generarNumeroPorColumna(String letra) {
        Random random = new Random();
        switch (letra) {
            case "B":
                return random.nextInt(15) + 1;
            case "I":
                return random.nextInt(15) + 16;
            case "N":
                return random.nextInt(15) + 31;
            case "G":
                return random.nextInt(15) + 46;
            case "O":
                return random.nextInt(15) + 61;
            default:
                return 0; // En caso de letra desconocida
        }
    }
    // Método para imprimir un cartón
    private static void imprimirCarton(int[] carton) {
        String[] columnas = {"\u001B[31mB", "\u001B[31mI", "\u001B[31mN", "\u001B[31mG", "\u001B[31mO"}; // Texto rojo para las letras BINGO
        int currentIndex = 0;

        for (int i = 0; i < columnas.length; i++) {
            System.out.print(columnas[i] + "\t" + "\u001B[0m"); // Restablecer color
        }
        System.out.println();


        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < columnas.length; j++) {
                int numero = carton[currentIndex];
                if (numero > 100||numero==0) {
                    System.out.print(ANSI_GREEN_BACKGROUND + (numero - 1000) + "\t"); // Fondo verde a los numeros marcados con +1000
                } else {
                    System.out.print(numero + "\t");
                }
                currentIndex++;
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void imprimirNumerosCartones(int[][] cartones, String [] nombresCartones) {

        String[] columnas = {"\u001B[31mB", "\u001B[31mI", "\u001B[31mN", "\u001B[31mG", "\u001B[31mO"}; // Texto rojo para las letras BINGO
        int currentIndex = 0;

        System.out.println();

        int p1 = cantidad / 4;
        int p2 = cantidad - p1 * 4;

        //Imprimir cartones de forma horizontal COMPLETOS
        for (int x = 0; x < p1; x++) {

            for (int i = 0; i < 4; i++) {
                System.out.print("+------------------------+\t\t");
            }
            System.out.println();

            currentIndex = 0;

            for (int i = 0; i < 4; i++) {
                String n = String.format("%-20s", (nombresCartones[i+x*4] + ":"));
                System.out.print("|\t" + "\u001B[34m" + n + "\u001B[0m" + " |\t\t");
            }
            System.out.println();


            for (int c = 0; c < 4; c++) {
                System.out.print("|\t");
                for (int i = 0; i < columnas.length; i++) {
                    System.out.print(columnas[i] + "\t" + "\u001B[0m");
                }
                System.out.print(" |\t\t");
            }

            System.out.println();

            for (int i = 0; i < 5; i++) {
                //System.out.println(i);
                for (int c = 0; c < 4; c++) {
                    System.out.print("|\t");
                    for (int j = 0; j < columnas.length; j++) {
                        int numero = cartones[c+4*x][currentIndex+j];
                        if (numero > 100 || numero == 0
                        ) {
                            System.out.print(ANSI_GREEN_BACKGROUND + (numero - 1000) +  ANSI_RESET+ "\t"); // Fondo verde
                        } else {
                            System.out.print(numero + "\t");
                        }
                    }
                    System.out.print(" |");

                    System.out.print("\t\t");
                }
                currentIndex+=5;
                System.out.println();
            }
            for (int i = 0; i < 4; i++) {
                System.out.print("+------------------------+\t\t");
            }
            System.out.println();
            System.out.println();
        }
// Imprimir los cartones de forma horizontal INCOMPLETOS

        if (p2 != 0) {

            for (int i = 0; i < p2; i++) {
                System.out.print("+------------------------+\t\t");
            }
            System.out.println();

            currentIndex = 0;
            for (int i = 0; i < p2; i++) {
                String n = String.format("%-20s", (nombresCartones[i+p1*4] + ":"));
                System.out.print("|\t" + "\u001B[34m" + n + "\u001B[0m" + " |\t\t");
            }
            System.out.println();

            for (int c = 0; c < p2; c++) {
                System.out.print("|\t");
                for (int i = 0; i < columnas.length; i++) {
                    System.out.print(columnas[i] + "\t" + "\u001B[0m");
                }
                System.out.print(" |\t\t");
            }

            System.out.println();

            for (int i = 0; i < 5; i++) {
                for (int c = 0; c < p2; c++) {
                    System.out.print("|\t");
                    for (int j = 0; j < columnas.length; j++) {
                        int numero = cartones[c+p1*4][currentIndex+j];
                        if (numero > 100||numero==0) {
                            System.out.print(ANSI_GREEN_BACKGROUND + (numero - 1000) + ANSI_RESET + "\t"); // Fondo verde
                        } else {
                            System.out.print(numero + "\t");
                        }
                    }
                    System.out.print(" |");
                    System.out.print("\t\t");
                }
                currentIndex+=5;
                System.out.println();
            }
            for (int i = 0; i < p2; i++) {
                System.out.print("+------------------------+\t\t");
            }
            System.out.println();
        }
    }
    // Método para marcar un número en el cartón
    private static boolean marcarCarton(int[] carton, String letra, int numero) {
        for (int i = 0; i < 25; i++) {
            if (carton[i] == numero && letra.equals(getColumnaLetra(i))) {
                carton[i] = carton[i] + 1000; // Marcar con +1000
                return true;
            }
        }
        return false;
    }

    // Método para obtener la letra de una columna en el cartón
    private static String getColumnaLetra(int index) {
        String[] columnas = {"B", "I", "N", "G", "O"};
        return columnas[index % 5];
    }

    // Método para verificar si hay un BINGO en el cartón
    private static boolean verificarBingo(int mdj, int[] carton) {
        // Dependiendo del modo de juego se verifica el tablero en busca del BINGO
        if (mdj == 0) { // Verificar la O
            System.out.println("MAMAAAAAAAAAAAAAAA SIRVO");
            boolean bingoO = true;

            // Verificar el borde superior
            for (int i = 0; i < 5; i++) {
                if (carton[i] < 100) {
                    bingoO = false;
                    break;
                }
            }

            // Verificar el borde inferior
            if (bingoO) {
                for (int i = 0; i < 5; i++) {
                    if (carton[4 * 5 + i] < 100) {
                        bingoO = false;
                        break;
                    }
                }
            }

            // Verificar el borde izquierdo
            if (bingoO) {
                for (int i = 1; i < 4; i++) {
                    if (carton[i * 5] < 100) {
                        bingoO = false;
                        break;
                    }
                }
            }

            // Verificar el borde derecho
            if (bingoO) {
                for (int i = 1; i < 4; i++) {
                    if (carton[i * 5 + 4] < 100) {
                        bingoO = false;
                        break;
                    }
                }
            }

            return bingoO;


        } else if (mdj == 1) { // Verficar la X
            boolean bingoX = true;
            // Verificar la diagonal principal de izquierda a derecha
            for (int i = 0; i < 5; i++) {
                if (carton[i * 5 + i] < 100) {
                    bingoX = false;
                    break;
                }
            }
            // Verificar la diagonal secundaria de derecha a izquierda
            if (bingoX) {
                for (int i = 0; i < 5; i++) {
                    if (carton[i * 5 + (4 - i)] < 100) {
                        bingoX = false;
                        break;
                    }
                }
            }
            return bingoX;

        } else if (mdj == 2) { // Verificar completo
            boolean tableroCompleto = true;
            // Verifica casilla por casilla
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (carton[i * 5 + j] < 100) { // Si el numero es menor que el numero maximo posible del tablero, significa que este no ha salido en el BINGO
                        tableroCompleto = false;
                        break;
                    }
                }
                if (tableroCompleto == false) {
                    break;
                }
            }
            if (tableroCompleto) {
                return true;
            }
        }
        return false;
    }
    // Método para esperar la presión de Enter
    private static void presionarEnter() {
        System.out.print("\u001B[33mPresiona Enter para sortear...\u001B[0m"); // Texto amarillo
        scanner.nextLine();
    }

    // Método para mostrar mensaje de inicio de juego
    private static void iniciarJuego() {
        System.out.print("\u001B[33mCartones Asignados !!!, pulsa ENTER para empezar\u001B[0m"); // Texto amarillo
        scanner.nextLine();
        scanner.nextLine();
    }

    // Método para mostrar mensaje de asignación de cartones
    private static void asignarCartones() {
        System.out.print("\u001B[33mAhora vamos a asignar los cartones, PRESIONA ENTER...\u001B[0m"); // Texto amarillo
        scanner.nextLine();
    }

    // Método para agregar la combinación al historial
    private static void agregarAlHistorial(String letra, int numero) {
        historial[indiceHistorial][0] = letra;
        historial[indiceHistorial][1] = Integer.toString(numero);
        indiceHistorial++;
    }
    // Método para imprimir el historial de números y letras

    // Método para imprimir el historial de números y letras
    private static void imprimirHistorial() {
        System.out.println("Historial de números y letras llamados:");
        System.out.println("-------------------------------------");

        // Obtener la lista única de letras en el historial
        String[] letrasUnicas = obtenerLetrasUnicas();
        String[] letrasOrdenadas = new String[5];

        for (String letra: letrasUnicas) {
            if (letra.equals("B")) {
                letrasOrdenadas[0] = letra;
            } else if (letra.equals("I")) {
                letrasOrdenadas[1] = letra;
            } else if (letra.equals("N")) {
                letrasOrdenadas[2] = letra;
            } else if (letra.equals("G")) {
                letrasOrdenadas[3] = letra;
            } else if (letra.equals("O")) {
                letrasOrdenadas[4] = letra;
            }
        }


        // Imprimir el historial agrupado por letra
        for (String letra : letrasOrdenadas) {
            if (letra != null) {
                System.out.print(letra + "   ");
                imprimirNumerosPorLetra(letra);
                System.out.println();
            }
        }

        System.out.println("-------------------------------------\n");
    }

    // Método para obtener la lista única de letras en el historial
    private static String[] obtenerLetrasUnicas() {
        String[] letrasUnicas = new String[indiceHistorial];
        int count = 0;

        for (int i = 0; i < indiceHistorial; i++) {
            String letra = historial[i][0];
            if (!contiene(letrasUnicas, letra, count)) {
                letrasUnicas[count++] = letra;
            }
        }

        // Ajustar el tamaño del arreglo resultante
        String[] resultado = new String[count];
        System.arraycopy(letrasUnicas, 0, resultado, 0, count);

        return resultado;
    }

    // Método para verificar si un arreglo contiene un elemento específico
    private static boolean contiene(String[] arreglo, String elemento, int count) {
        for (int i = 0; i < count; i++) {
            if (arreglo[i].equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    // Método para imprimir los números asociados a una letra en el historial
    private static void imprimirNumerosPorLetra(String letra) {
        for (int i = 0; i < indiceHistorial; i++) {
            if (historial[i][0].equals(letra)) {
                System.out.print(historial[i][1] + " ");
            }
        }
    }
}