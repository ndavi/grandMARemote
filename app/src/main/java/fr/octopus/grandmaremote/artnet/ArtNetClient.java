package fr.octopus.grandmaremote.artnet;

/**
 * Created by nico on 11/7/17.
 */


import java.net.SocketException;
import java.net.InetAddress;

import fr.octopus.artnet.ArtNet;
import fr.octopus.artnet.ArtNetException;
import fr.octopus.artnet.ArtNetNode;
import fr.octopus.artnet.packets.ArtDmxPacket;


public class ArtNetClient {
    private int sequenceId;
    private ArtNet artnet;
    private ArtNetNode receiver;

    public ArtNetClient()
    {
        artnet = new ArtNet();
    }

    public void open()
    {
        open(null, null);
    }

    public void open(InetAddress in, String address)
    {
        try
        {
            // sender
            if(in == null) {
                artnet.start();
            } else {
                artnet.start(in);
            }
            setReceiver(address);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (ArtNetException e) {
            e.printStackTrace();
        }
    }

    public void setReceiver(String address)
    {
        if (null == address)
            receiver = null;

        try
        {
            receiver = new ArtNetNode();
            receiver.setIPAddress(InetAddress.getByName(address));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        artnet.stop();
    }

    public void send(int universe, byte[] data)
    {
        send(receiver, universe, data);
    }

    public void send(ArtNetNode node, int universe, byte[] data)
    {
        ArtDmxPacket dmx = new ArtDmxPacket();

        dmx.setUniverse(0, universe);
        dmx.setSequenceID(sequenceId % 256);
        dmx.setDMX(data, data.length);

        if (receiver != null)
            artnet.unicastPacket(dmx, node);
        else
            artnet.broadcastPacket(dmx);

        sequenceId++;
    }
}
