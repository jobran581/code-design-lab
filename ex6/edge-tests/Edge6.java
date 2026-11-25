import test.*;
import java.util.concurrent.ForkJoinPool;
/** Edge cases for Exercise 6: compare with a plain recursive maximum for many tree sizes. */
public class Edge6 { static int fails=0; static void check(boolean ok,String what){System.out.println((ok?"  OK    ":"  FAIL  ")+what); if(!ok)fails++;} static void done(){System.out.println(fails==0?"ALL EDGE CASES PASSED":fails+" FAILED");}
 static int seqMax(BinTree t){ if(t==null) return Integer.MIN_VALUE; return Math.max(t.get(),Math.max(seqMax(t.getLeft()),seqMax(t.getRight()))); }
 public static void main(String[] a){
  ForkJoinPool pool=new ForkJoinPool();
  for(int depth=0; depth<=14; depth++){
   BinTree t=BinTree.generateRandomTree(depth);
   check(pool.invoke(new ParMaxSearcher(t))==seqMax(t), "depth "+depth+" ("+((1<<(depth+1))-1)+" nodes): same max as a sequential search");
  }
  pool.shutdown();
  done();
 }
}
