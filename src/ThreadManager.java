
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
    //Main serverdaki serverbalance fonksiyonu ile yük dağıtımı başlıyor

    public void startServerBalance() {
        System.out.println("//////////////// Start Server Balance Thread");
        balance = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(Main.server.get(MainServerIndex).getRequestTime());

                    Main.server.get(0).serverBalance();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        balance.start();
        balance.setPriority(Thread.MAX_PRIORITY - 2);

    }

    //Serverların durumunu basmak için printlist() fonskiyonunu aktifleştiriyoruz.
    public void startListThread() {

        list = new Thread(new Runnable() {
            @Override
            public void run() {

                printList();

            }
        });
        list.start();
        System.out.println("//////////////// Start List Thread");
    }

    public void capatityControlThread() {

        divede = new Thread(new Runnable() {
            @Override
            public void run() {

                capacityControl(Main.server);

            }
        });

        divede.start();
        System.out.println("//////////////// Start capatityControlThread Thread");
    }

    //Request sayısı 0 a düşen fonksiyonlaru burda kontrol ediyoruz.
    public void closeControlThread() {

        close = new Thread(new Runnable() {
            @Override
            public void run() {

                closeServer(Main.server);

            }
        });
        System.out.println("//////////////// Start closeControlThread Thread");
        close.start();

    }

    //Bölme işlemi yapılırken problem çıkmasını engellemek için request ve response işlerini sleep metodu ile bekletiyoruz.
    public void waitResponseThread(int serverIndex) {

        Main.server.get(serverIndex).setSleep(true);

    }

    //Beklettiğimiz request ve response işlemlerini tekrara başlatıyoruz
    public void startResponseThread(int serverIndex) {

        Main.server.get(serverIndex).setSleep(false);

    }

    //Sunucuların kapasitesini kontrol ederek bölünmesi gerektiğinde bölüyoruz.
    public void capacityControl(ArrayList<Server> subServer) {
        Server server;
        while (true) {
            try {
                Thread.sleep(1000);
                for (int k = 1; k < subServer.size(); k++) {
                    server = subServer.get(k);
                    double percent = (server.getCapacity() * 0.7);

                    //System.out.println("currentPer:"+currentPer);
                    if (server.getRequestData().size() >= percent) {
                        System.out.println("-------------BÖLÜNME İŞLEMİNDEN ÖNCE-------------");
                        waitResponseThread(k);
                        divede.setPriority(Thread.MAX_PRIORITY);
                        divideServer(server);
                        startResponseThread(k);
                        System.out.println("-------------BÖLÜNME İŞLEMİNDEN SONRA-------------");
                    }

                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    public void divideServer(Server server) {
        
    }

    public void closeServer(ArrayList<Server> subServer) {

    }

    public void printList() {

    }

}
