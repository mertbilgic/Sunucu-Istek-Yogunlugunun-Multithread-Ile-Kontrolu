
import java.util.ArrayList;

public class Main {

    //Static oluşturmamızın sebebi her yerden erişilebilir hale gelmesini sağlamak
    static ArrayList<Server> server = new ArrayList<Server>();

    public static void main(String[] args) {

        System.out.println("//////////////// Main Thread Başladı");

        //ThreadManeger sınıfındaki fonksiyonları kullanıp server yönetim işini
        //yapmak için oluşturduğumu obje
        ThreadManager tm = new ThreadManager();

        server.add(new MainServer("MainServer", 10000, 200, 2500, 1, false, 1000, 700));
        server.add(new SubServer("SubServer-1", 5000, 300, 3000, 2, false, 150));
        server.add(new SubServer("SubServer-2", 5000, 300, 3000, 3, false, 150));

    }
}
