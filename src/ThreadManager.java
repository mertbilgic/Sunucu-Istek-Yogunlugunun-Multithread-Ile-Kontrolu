
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadManager {
        Object lock = new Object();
    final int MainServerIndex = 0;
    static List<Thread> threadResponse = Collections.synchronizedList(new ArrayList());
    static int i;
    static int nameSize = 3;
    final int defaultThreadCount = 6;
    static Thread mainThread, balance, t2, list, response, divede, close;
    
}
