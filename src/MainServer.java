
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServer extends Server {

    private int controlTime;
    private final int minCount = 0;
    static int Balence = 0, Reponse = 0;

    public MainServer(String serverName, int capacity, int requestTime, int responseTime, int serverIP,
            boolean sleep, int controlTime, int maxRequestCount, int totalRequest) {
        super(serverName, capacity, requestTime, responseTime, serverIP, sleep, maxRequestCount, totalRequest);
        this.controlTime = controlTime;
    }

    public int getControlTime() {
        return controlTime;
    }

    public void setControlTime(int controlTime) {
        this.controlTime = controlTime;
    }

    //Listemize fake verilerimizden yararlanarak request ekliyoruz.
    public void request() {

        int value, requestCount;
        String ip;
        while (true) {
            try {
                Thread.sleep(getRequestTime());

                requestCount = random.nextInt(getMaxRequestCount());
                //System.out.println(getRequestData().size()+"    "+getCapacity());
                if (getRequestData().size() + requestCount <= getCapacity()) {
                    add(requestCount);
                    //System.out.println(requestData.size());
                } else {
                    requestCount = getCapacity() - getRequestData().size();
                    add(requestCount);

                }
                //System.out.println(getServerName() + "  " + requestData.size());
                //MainServer'ın request sayısı güncelleiyor
                setTotalRequest(requestData.size());
            } catch (InterruptedException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void add(int requestCount) {
        String ip;

        for (int i = 0; i < requestCount; i++) {

            ip = "11";

            requestData.add(new RequestData(ip, ip));

        }
    }

    //Listedimizdeki verileri yanıt veriyormuş gibi belli aralıklarla temizliyoruz.
    public void response() {
        int requestCount = 0;
        long start, end;
        while (true) {
            //start = System.nanoTime();

            while (getTotalRequest() == 0) {
                System.out.println("Request Bekliyor");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            /*end = (getResponseTime() - (System.nanoTime() - start));
            if ((end < 0)) {
                end = 0;
            }
            System.out.println("--------------------->end:  " + end);*/
            try {
                synchronized (lock) {
                    Thread.sleep(getResponseTime());

                    requestCount = random.nextInt(getMaxRequestCount());

                    if (requestCount > requestData.size()) {

                        Thread.sleep(100);
                        requestCount = (requestData.size());

                    }

                    for (int i = 0; i < (requestCount) && requestCount < responseData.size(); i++) {
                        requestData.remove(0);
                    }

                    if (Reponse < 2) {
                        Reponse++;
                    } else {
                        Reponse = 0;
                        ThreadManager.response.setPriority(Thread.NORM_PRIORITY);
                    }
                    //System.out.println("RESpoo" + getServerName() + "  " + requestData.size());
                    //MainServer'ın request sayısı güncelleiyor
                    setTotalRequest(requestData.size());
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(SubServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Yük dağıtım işlemini MainServer üzerinden yapıyoruz.
    public void serverBalance() {
        int requestCount, serverIndex;
        Server server;
        while (true) {
            while (getTotalRequest() == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Hangi servere istek atıcaksak onu belirliyoruz.
            serverIndex = random.nextInt(Main.server.size() - 1) + 1;
            //System.out.println("ServerIndex" + serverIndex);
            //Random request sayınını beliyoruz.
            requestCount = random.nextInt(Main.server.get(serverIndex).getMaxRequestCount());
            requestCount = requestCountControl(getRequestData().size(), requestCount);
            //bu objeyi şimdilik girdi çıktı gibi işlemleri bana göstersin diye kullanıyourm
            server = Main.server.get(serverIndex);

            //Seçilen alt server kendine yollanan requestleri kabul ediyor
            Main.server.get(serverIndex).request(Main.server.get(0).requestData, serverIndex, requestCount);

            /* for (int i = 0; i < requestCount && server.getRequestData().size() > requestCount; i++) {
                requestData.remove(0);
            }*/
            //requestData.removeAll(temp);
            if (Balence < 2) {
                Balence++;
            } else {
                Balence = 0;
                ThreadManager.balance.setPriority(Thread.NORM_PRIORITY);
            }

        }

    }

}
