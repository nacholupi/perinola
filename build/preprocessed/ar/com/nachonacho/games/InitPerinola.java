/**
 * Nacho
 */
package ar.com.nachonacho.games;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class InitPerinola extends MIDlet {
    Perinola peri;

    public InitPerinola () {
        peri = new Perinola(this);
    }

    public void startApp () {
        Display.getDisplay(this).setCurrent(peri);
    }

    public void pauseApp () {
    }

    public void destroyApp (boolean unc) {
    }
}
