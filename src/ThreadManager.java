
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadManager {

    Object lock = new Object();
    final int MainServerIndex = 0;
    static List<Thread> threadResponse = Collections.synchronizedList(new ArrayList());
    static int i;
    static int nameSize = 3;
    final int defaultThreadCount = 6;
    static Thread mainThread, balance, t2, list, response, divede, close;
    //MainServerin request alma işi burda başlıyor.

    public void startMainRequest() {
        System.out.println("//////////////// Start Main Request Thread");
        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(Main.server.get(MainServerIndex).getRequestTime());
                    Main.server.get(MainServerIndex).request();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        mainThread.start();

    }
    //Default olarak kulladığımız serverları Main içindeki server listesinden
    //for yardımıyla dönüp create ediyoruz.

    public void startDefaultResponse() {
        for (int j = 1; j < 3; j++) {

            create(Main.server.get(j));

        }

    }

    //Ana sunucunun request almaya işlemini başlatıyoruz.
    public void startMainResponse() {

        response = new Thread(new Runnable() {
            @Override
            public void run() {

                Main.server.get(0).response();

            }
        });

        response.start();
        System.out.println("//////////////// Start " + Main.server.get(0).getServerName() + " Response Thread");
        threadResponse.add(t2);

    }
    //startDefaultResponse() fonkiyonu tarafından çağrılıyor.Gönderilen serverin
    //response fonksiyonu çağrılıyor.İleride bu server üzerinde herhangi bir işlem
    //yapılmak istenirse eklendiği liste yardımıyla yapılacak.

    public void create(Server server) {

        t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                server.response();

            }
        });

        t2.start();
        System.out.println("//////////////// Start " + server.getServerName() + " Response Thread");
        threadResponse.add(t2);

    }

}
