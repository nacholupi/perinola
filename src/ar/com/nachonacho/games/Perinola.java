/*
 * Nacho
 */
package ar.com.nachonacho.games;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class Perinola extends Canvas {

    // Constantes
    private static final int INITIALIZED = 0;
    private static final int LOADING = 1;
    private static final int PLAYING = 2;
    private static final String INIT_TEXTO_LINEA1 = "A la perinola!!";
    private static final String INIT_TEXTO_LINEA2 = "presione 5 para tirar";
    private static final String INIT_IMAGE = "/files/perino_image.png";
    private static final String FILE_ESTADOS = "/files/estados.txt";
    private static final String INIT_IMAGE_TEXT_ERROR = "Error: imagen";
    private static final String TEXTO_LOADING = "...tirando... ";
    private static final long SLEEP = 2000L;

    // Variables
    private int estadoJuego = INITIALIZED;
    private Vector vectorEstados;
    private Random random;
    private int backColor1 = 255;
    private int backColor2 = 100;
    private int backColor3 = 0;

    public Perinola(MIDlet midlet_) {
        initVectorEstados();
    }

    private void initVectorEstados() {
        try {
            Vector v = new Vector();
            InputStream in = this.getClass().getResourceAsStream(FILE_ESTADOS);
            StringBuffer buf = new StringBuffer();
            int chars = 0;

            while (chars != -1) {
                chars = in.read();
                char c = (char) chars;

                if (c == '\n' || chars == -1) {
                    //Agrego el buffer al vector
                    v.addElement(buf.toString().trim());
                    buf = new StringBuffer();
                } else {
                    // Agrego al buffer
                    buf.append(c);
                }
            }
            vectorEstados = v;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {

        if (estadoJuego == INITIALIZED) {
            // Dibuja la pantalla inicial
            drawInitScreen(g);
        }
        if (estadoJuego == LOADING) {
            // Nuevo seed
            changeRandom();
            // Setea nuevo color de fondo
            changeBackground();
            // Dibuja la pantalla de cargando
            drawLoading(g);
            // Lanza el thread - Espera y cambia estado a playing
            new ThreadWait(g).start();
        }
        if (estadoJuego == PLAYING) {
            drawResultado(g);
        }
    }

    private void drawInitScreen(Graphics g) {

        int width = getWidth();
        int height = getHeight();
        // Seteo fondo
        g.setColor(backColor1, backColor2, backColor3);
        g.fillRect(0, 0, width, height);

        // Seteo fuente
        g.setColor(255, 255, 255);
        g.setFont(Font.getDefaultFont());

        try {
            // Fotito perinola
            Image image = Image.createImage(INIT_IMAGE);
            g.drawImage(image, width / 2, height / 2 - 45,
                    Graphics.VCENTER | Graphics.HCENTER);
        } catch (IOException ex) {
            // Imprime error si existe
            ex.printStackTrace();
            g.drawString(INIT_IMAGE_TEXT_ERROR, width / 2, height / 2 + 30,
                    Graphics.BASELINE | Graphics.HCENTER);
        }

        // Imprime la primer linea
        g.drawString(INIT_TEXTO_LINEA1, width / 2, height / 2,
                Graphics.BASELINE | Graphics.HCENTER);
        // Imprime la segunda linea
        g.drawString(INIT_TEXTO_LINEA2, width / 2, height / 2 + 15,
                Graphics.BASELINE | Graphics.HCENTER);


    }

    private void drawLoading(Graphics g) {

        int width = getWidth();
        int height = getHeight();
        // Seteo fondo - Toma valores random
        g.setColor(backColor1, backColor2, backColor3);
        g.fillRect(0, 0, width, height);

        // Seteo fuente
        g.setColor(255, 255, 255);
        g.setFont(Font.getDefaultFont());

        // Imprime la primer linea
        g.drawString(TEXTO_LOADING, width / 2, height / 2,
                Graphics.BASELINE | Graphics.HCENTER);

    }

    private void drawResultado(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        // Seteo fondo - Toma valores random
        g.setColor(backColor1, backColor2, backColor3);
        g.fillRect(0, 0, width, height);

        // Seteo fuente
        g.setColor(255, 255, 255);
        g.setFont(Font.getDefaultFont());
        g.drawString(getTexto(), width / 2, height / 2,
                Graphics.BASELINE | Graphics.HCENTER);
    }

    private void changeRandom() {
        // Genera un random con la hora del sistema como seed
        random = new Random((int) System.currentTimeMillis());
    }

    private String getTexto() {
        // Cambio el texto con un random
        return (String) vectorEstados.elementAt(random.nextInt(vectorEstados.size()));
    }

    private void changeBackground() {
        backColor1 = random.nextInt(128);
        backColor2 =
                random.nextInt(128);
        backColor3 =
                random.nextInt(128);
    }

    protected void keyPressed(int keyCode) {
        int gameAction = getGameAction(keyCode);
        if (gameAction == FIRE || gameAction == KEY_NUM5) {
            // cambio el estado
            estadoJuego = LOADING;
            repaint();
        }
    }

    private class ThreadWait extends Thread {

        private Graphics g;

        private ThreadWait(Graphics g) {
            this.g = g;
        }

        public void run() {
            if (this.g != null) {
                try {
                    ThreadWait.sleep(SLEEP);
                    estadoJuego = PLAYING;
                    repaint();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
