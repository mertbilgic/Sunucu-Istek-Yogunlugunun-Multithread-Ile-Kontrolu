
import java.util.ArrayList;

public class Main {

    //Static oluşturmamızın sebebi her yerden erişilebilir hale gelmesini sağlamak
    static ArrayList<Server> server = new ArrayList<Server>();

    public static void main(String[] args) {

        System.out.println("//////////////// Main Thread Başladı");

        server.add(new MainServer("MainServer", 10000, 200, 2500, 1, false, 1000, 700, 0));
        server.add(new SubServer("SubServer-1", 5000, 300, 3000, 2, false, 150, 0));
        server.add(new SubServer("SubServer-2", 5000, 300, 3000, 3, false, 150, 0));

        //ThreadManeger sınıfındaki fonksiyonları kullanıp server yönetim işini
        //yapmak için oluşturduğumu obje
        ThreadManager tm = new ThreadManager();
        //MainServer'in request alma işlemini thread yardımıyla başladıtıyoruz
        tm.startMainRequest();
        //MainServer'in yük dağıtım işlemini thread yardımıyla başladıtıyoruz
        tm.startServerBalance();
        //Başlangıçta elimizde olan üç server için response işlemlerini gerçekleştiriyoruz thread yardımıyla başladıtıyoruz
        tm.startMainResponse();
        tm.startDefaultResponse();
        //Şimdilik serverler'in durumunu kontrol etmek için oluştuduğumuz server listeleme fonskiyonu
        tm.startListThread();
        //tm.stopResponseThread();
        tm.capatityControlThread();
        tm.closeControlThread();

    }

}
