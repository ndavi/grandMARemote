package fr.octopus.grandmaremote.artnet;

/**
 * Created by nico on 11/7/17.
 */

public class ArtNetSender {

    ArtNetClient artnet;
    private int universe;
    private String address;
    public byte[] data = new byte[512];

    public ArtNetSender(int universe, String address) {
        this.universe = universe;
        this.address = address;
        setup();
    }


    void setup()
    {
            artnet = new ArtNetClient();
            artnet.open(null,address);
    }


    void stop()
    {
        artnet.close();
    }

}
