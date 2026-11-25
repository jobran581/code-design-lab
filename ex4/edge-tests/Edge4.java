import test.ObservableFuture;
import java.util.concurrent.*;
/** Edge cases for Exercise 4. */
public class Edge4 { static int fails=0; static void check(boolean ok,String what){System.out.println((ok?"  OK    ":"  FAIL  ")+what); if(!ok)fails++;} static void done(){System.out.println(fails==0?"ALL EDGE CASES PASSED":fails+" FAILED");}
 public static void main(String[] a) throws Exception {
  ExecutorService es=Executors.newFixedThreadPool(2);
  ObservableFuture<String> of=new ObservableFuture<>(es.submit(()->{Thread.sleep(300);return "hi";}));
  int[] n={0}; Object[] arg={null};
  of.addObserver((o,x)->{n[0]++; arg[0]=x;});
  of.addObserver((o,x)->n[0]++);
  check(of.get()==null, "get() is null before the value arrives");
  Thread.sleep(600);
  check("hi".equals(of.get()), "get() returns the value after it arrives");
  check(n[0]==2, "both observers notified once");
  check("hi".equals(arg[0]), "the value is also passed to the observers");
  ObservableFuture<Integer> bad=new ObservableFuture<>(es.submit(()->{throw new RuntimeException();}));
  int[] m={0}; bad.addObserver((o,x)->m[0]++);
  Thread.sleep(200);
  check(bad.get()==null && m[0]==0, "failed task: no value, no notification, no crash");
  es.shutdown();
  done();
 }
}
