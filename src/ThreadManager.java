
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
    boolean result = false;

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

    //%70 i dolan sunucuları kontrol ediyoruz.Eğer Main sunucu değilse bölüyoruz.
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

    //Bir sunucuyu listeden silmedne önce çalışan threadi duruyoruz.
    public void stopResponseThread(int serverIndex) {
        try {
            System.out.println("---------------->Stop etti");
            threadResponse.get(serverIndex).stop();
            threadResponse.remove(serverIndex);
            System.out.println("---->DELETE" + Main.server.get(serverIndex).getServerName() + "    " + Main.server.get(serverIndex).responseData.size());
            Main.server.remove(serverIndex);
        } catch (Exception ex) {
            //System.out.println(ex);
        }

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
                        result = true;

                        System.out.println("-------------BÖLÜNME İŞLEMİNDEN ÖNCE-------------");

                        waitResponseThread(k);
                        divede.setPriority(Thread.MAX_PRIORITY);
                        divideServer(server);
                        startResponseThread(k);
                        System.out.println("-------------BÖLÜNME İŞLEMİNDEN SONRA-------------");
                        result = false;
                    }

                }
            } catch (Exception ex) {
                Logger.getLogger(ThreadManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    //Bölünme işleminin sonunda yeni sunucuyu(objemizi) burda oluşturuyoruz.
    public void createNewServer(ArrayList<RequestData> requestData) {
        try {
            System.out.println("------------------------------->CreateNewServer");
            int size = Main.server.size();
            String Stsize = String.valueOf(nameSize);
            Main.server.add(new SubServer("SubServer-" + Stsize, 5000, 300, 2500, (size + 1), false, 150, 0));
            Main.server.get(size).requestData.addAll(requestData);
            create(Main.server.get(size));
            nameSize++;

        } catch (Exception ex) {

        }

    }

    //Serverların bölünme sırasındaki sınırlarını berliyoruz.
    public void divideServer(Server server) {
        try {
            System.out.println("------------------------------->DivideServer");
            int startIndex = server.getRequestData().size() / 2;
            int finishIndex = server.getRequestData().size();
            ArrayList<RequestData> temp = divideRequest(server.getRequestData(), startIndex, finishIndex);

            server.requestData.removeAll(temp);
            createNewServer(temp);

        } catch (Exception ex) {
            //System.out.println(ex);
        }
    }

    //Belirlediğimiz sınırlara göre yeni değişkenimize verileri ekliyoruz
    public ArrayList<RequestData> divideRequest(ArrayList<RequestData> requestData, int startIndex, int finisIndex) {
        System.out.println("------------------------------->divideRequest");
        ArrayList<RequestData> temp;

        temp = new ArrayList<RequestData>(requestData.subList(startIndex, finisIndex));

        return temp;

    }

    //Default olarak tanımlanmayan alt sunucuların kontorlünü yapıyor.
    public void closeServer(ArrayList<Server> subServer) {
        Server server;
        while (true) {
            try {
                Thread.sleep(1000);
                for (int k = 3; k < subServer.size(); k++) {
                    server = subServer.get(k);
                    double percent = (server.getCapacity() * 0.7);
                    double currentPer = (server.getRequestData().size() * 0.7);
                    //Sunucu kapatma işlemini gerçekleştiricek
                    if (currentPer == 0) {
                        System.out.println(server.getServerName() + "Response Thread Stop");
                        stopResponseThread(k);

                    }

                }
            } catch (Exception ex) {
                //System.out.println(ex);
            }

        }

    }

    //Sunucuları listelerken yüzdelerini burda hesaplıyoruz
    public double percent(double size, double capatiy) {

        double result = (size * 100) / capatiy;

        return result;
    }

    //Main'in içinde oluşturduğumuz listemizde serverların özelliklerini basıyoruz.
    public void list() {
        Server server;
        try {
            System.out.println("************************************************************************\n");
            for (int i = 0; i < Main.server.size(); i++) {
                server = Main.server.get(i);

                System.out.println(server.getServerName() + "      " + server.getTotalRequest() + "      %"
                        + percent(server.getTotalRequest(), server.getCapacity()));

            }
            System.out.println("Çalışan Thread Sayısı: " + (defaultThreadCount + threadResponse.size()));

            System.out.println("Çalışan Sunucu Sayısı: " + Main.server.size());
            System.out.println("\n\n************************************************************************");

        } catch (Exception ex) {
            //System.out.println(ex);
        }

    }

    //Listeleme fonksiyonunu çağırdığımız kısım
    public void printList() {

        while (true) {

            try {
                Thread.sleep(100);

                while (result == true) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            list();
        }

    }

}
