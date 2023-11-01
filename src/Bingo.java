import java.util.Random;
import javax.swing.JOptionPane;

public class Bingo {
    public static void main(String[] args) {
        // Solicitar la cantidad de cartones
      //  int cantidadCartones = solicitarCantidadCartones();
        String entrada = JOptionPane.showInputDialog("Ingrese la cantidad de cartones (1-12):");
        int cantidadCartones = Integer.parseInt(entrada);

        if (cantidadCartones <= 0 || cantidadCartones > 12) {
            JOptionPane.showMessageDialog(null, "La cantidad de cartones debe estar entre 1 y 12.");
            System.out.println("El número de cartones debe estar entre 1 y 12...VUELVE A INTENTARLO");
            return;
        }

        String[] nombresCartones = new String[cantidadCartones];
        int[][] cartones = new int[cantidadCartones][25];

        // Generar los cartones y solicitar nombres
        for (int i = 0; i < cantidadCartones; i++) {
            generarCarton(cartones[i]);
            String nombre = solicitarNombreCarton(i + 1);
            nombresCartones[i] = nombre;
        }

        // Mostrar los cartones por la consola
        for (int i = 0; i < cantidadCartones; i++) {
            System.out.println("Cartón " + nombresCartones[i] + ":");
            mostrarCarton(cartones[i]);
        }
    }
/*
    private static int solicitarCantidadCartones() {
        String input = JOptionPane.showInputDialog("Ingrese la cantidad de cartones (1-12):");
        return Integer.parseInt(input);
    }
*/
    private static String solicitarNombreCarton(int numeroCarton) {
        return JOptionPane.showInputDialog("Nombre para el cartón " + numeroCarton + " (máximo 15 caracteres):");
    }

    private static void generarCarton(int[] carton) {
        Random random = new Random();
        int inicio = 1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (j == 2 && i == 2) {
                    carton[i * 5 + j] = 0; // El espacio central es un espacio libre
                } else {
                    carton[i * 5 + j] = random.nextInt(15) + inicio;
                }
            }
            inicio += 15;
        }
    }

    private static void mostrarCarton(int[] carton) {
        String[] columnas = {"B", "I", "N", "G", "O"};
        int currentIndex = 0;

        for (String columna : columnas) {
            System.out.print(columna + "\t");
        }
        System.out.println();

        for (int i = 0; i < 5; i++) {
            for (String columna : columnas) {
                System.out.print(carton[currentIndex] + "\t");
                currentIndex++;
            }
            System.out.println();
        }
    }
}
