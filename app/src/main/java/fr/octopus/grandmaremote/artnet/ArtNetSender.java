package fr.octopus.grandmaremote.artnet;

/**
 * Created by nico on 11/7/17.
 */

public class ArtNetSender {

    private ArtNetClient artnet;
    private int universe;
    private String address;
    public byte[] data = new byte[512];

    public ArtNetSender(int universe, String address) {
        this.universe = universe;
        this.address = address;
        setup();
    }


    void setup() {
        artnet = new ArtNetClient();
        artnet.open(null,address);

        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    artnet.send(universe, data);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void send(int canal, int value) {
        data[canal] = (byte)value;
        artnet.send(universe,data);
    }


    void stop()
    {
        artnet.close();
    }

}
