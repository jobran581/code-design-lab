import test.*;
import java.util.*;
/** Edge cases for Exercise 5. */
public class Edge5 { static int fails=0; static void check(boolean ok,String what){System.out.println((ok?"  OK    ":"  FAIL  ")+what); if(!ok)fails++;} static void done(){System.out.println(fails==0?"ALL EDGE CASES PASSED":fails+" FAILED");}
 public static void main(String[] a) throws Exception {
  int before=Thread.activeCount();
  Q1b q=new Q1b(); List<String> log=Collections.synchronizedList(new ArrayList<>());
  q.push(()->log.add("a")); q.push(()->{throw new RuntimeException("x");}); q.push(()->log.add("b"));
  for(int i=0;i<5;i++){int k=i; q.push(()->{try{Thread.sleep(50);}catch(Exception e){} log.add(""+k);});}
  q.close(); q.close(); q.push(()->log.add("LATE"));
  Thread.sleep(600);
  check(String.join("",log).equals("ab01234"), "tasks run in order; a failing task does not stop the others");
  check(!log.contains("LATE"), "push after close is ignored");
  check(Thread.activeCount()==before, "the thread ended after close (close twice is harmless)");
  Q1a qa=new Q1a();
  check(qa.threadIt(()->"text").get().equals("text"), "threadIt works with any type (String)");
  check(qa.threadIt(()->7).get()==7, "threadIt works with any type (Integer)");
  qa.close();
  done();
 }
}
