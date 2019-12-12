
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubServer extends Server {

    //Main server için bir obje oluşturuyoruz.
    Server server = Main.server.get(MainServerIndex);

    public SubServer(String serverName, int capacity, int requestTime, int responseTime, int serverIP, boolean sleep, int maxRequestCount, int totalRequest
    ) {
        super(serverName, capacity, requestTime, responseTime, serverIP, sleep, maxRequestCount, totalRequest);
    }

    //Balance metodu tarafından yönllendirilen requestler ilgili sunucuya burda ekleniyor.
    public ArrayList<RequestData> request(ArrayList<RequestData> request, int serverIndex, int requestCount) {
        String r1 = "", r2 = "";
        //Birden fazla thread'in aynı işlemi yapmasını engellemeye yarıyor. Bekletiyor
        synchronized (lock) {
            //System.out.println(getServerName());
            ArrayList<RequestData> temp = null;
            //requestCount = requestCountControl(request.size(), requestCount);
            try {
                Thread.sleep(getRequestTime());

                //temp = new ArrayList<RequestData>(request.subList(startIndex, requestCount));
                //requestData.addAll(temp);
                for (int i = 0; i < requestCount; i++) {
                    try {
                        if (request.get(0) != null) {
                            r1 = request.get(0).getClientIP();
                            r2 = request.get(0).getRequest();
                            requestData.add(new RequestData(r1, r2));
                            server.requestData.remove(0);
                        }

                    } catch (Exception ex) {

                        //System.out.println("ex\n\n");
                    }

                }
                //SubServer'ın request sayısı güncelleiyor
                setTotalRequest(requestData.size());
                //System.out.println("Respoo" + getServerName() + "  " + requestData.size());
            } catch (Exception ex) {
                Logger.getLogger(SubServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return temp;
        }
    }

    public void response() {
        int requestCount;
        String r1, r2;
        while (true) {
            try {
                //Bölme veya request yetersiz ise işlemi sırasında response işlemini sleep yapıyoruz
                while (isSleep() == true || getTotalRequest() == 0) {
                    Thread.sleep(100);
                }

                Thread.sleep(getResponseTime());

                requestCount = random.nextInt(getMaxRequestCount());
                requestCount = requestCountControl(requestData.size(), requestCount);

                synchronized (lock) {
                    //System.out.println("REPSOSOSOSOOSOSOSOSO");
                    /*ArrayList<RequestData> temp;
                    temp = new ArrayList<RequestData>(requestData.subList(startIndex, requestCount));
                    requestData.removeAll(temp);*/
                    for (int j = 0; j < requestCount && requestData.size() != 0; j++) {
                        try {
                            if (requestData.get(0) != null) {
                                r1 = requestData.get(0).getClientIP();
                                r2 = requestData.get(0).getRequest();
                                requestData.remove(0);
                                server.responseData.add(new RequestData(r1, r2));
                            }
                        } catch (Exception ex) {
                            System.out.println("Response Ex:" + ex);
                        }

                    }

                    //server.responseData.addAll(temp);
                    //SubServer'ın request sayısı güncelleiyor
                    //System.out.println(getServerName() + "  " + requestData.size());
                    setTotalRequest(requestData.size());
                }
            } catch (Exception ex) {
                Logger.getLogger(SubServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
