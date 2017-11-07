package fr.octopus.grandmaremote.Service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by nico on 11/7/17.
 */

public class ArtNetService extends IntentService {

    ArtNetClient artnet;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ArtNetService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        setup();
    }



    int i = 0;

    byte[] buffer = new byte[512];

    void setup()
    {
        // get all avaialable network interfaces
        listNetworkInterfaces();

        try
        {
            NetworkInterface ni = NetworkInterface.getByName("lo0");
            artnet = new ArtNetClient();
            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            addresses.nextElement();
            addresses.nextElement();
            artnet.open(addresses.nextElement(), null);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
    }


    void stop()
    {
        artnet.close();
    }

    void listNetworkInterfaces()
    {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface i = networkInterfaces.nextElement();
                Log.d("INTERFACE", i.getDisplayName() + ": ");

                Enumeration<InetAddress> addresses = i.getInetAddresses();
                while (addresses.hasMoreElements())
                {
                    InetAddress address = addresses.nextElement();
                    Log.d("INTERFACE",address.getHostAddress() + " / ");
                }
            }
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
