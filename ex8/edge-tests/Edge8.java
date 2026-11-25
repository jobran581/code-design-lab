import test.Pipe;
import java.util.concurrent.atomic.*;
/** Edge cases for Exercise 8. */
public class Edge8 { static int fails=0; static void check(boolean ok,String what){System.out.println((ok?"  OK    ":"  FAIL  ")+what); if(!ok)fails++;} static void done(){System.out.println(fails==0?"ALL EDGE CASES PASSED":fails+" FAILED");}
 public static void main(String[] a) throws Exception {
  int tx=Thread.activeCount();
  Pipe<Integer> p=new Pipe<>(); AtomicLong sum=new AtomicLong(); AtomicInteger cnt=new AtomicInteger();
  p.map(x->x*2).filter(x->x%3==0).map(x->"v"+x).map(String::length).forEach(x->{sum.addAndGet(x);cnt.incrementAndGet();});
  check(Thread.activeCount()-tx==5, "5 pipes -> exactly 5 threads");
  long exp=0; int ec=0; for(int i=0;i<1000;i++){p.add(i); int y=i*2; if(y%3==0){exp+=("v"+y).length(); ec++;}}
  Thread.sleep(300);
  check(sum.get()==exp && cnt.get()==ec, "1000 items (more than the queue of 100): correct result "+sum+"/"+exp);
  p.stop(); Thread.sleep(100);
  check(Thread.activeCount()==tx, "stop() on the first pipe closes all 5 threads");
  Pipe<String> q=new Pipe<>(); q.add("x"); q.add("yy"); int[] s={0};
  Thread.sleep(50); q.map(String::length).forEach(x->s[0]+=x); Thread.sleep(50);
  check(s[0]==3, "items added before the chain was defined are not lost");
  q.stop();
  done();
 }
}
