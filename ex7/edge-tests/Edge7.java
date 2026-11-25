import test.MyFuture;
/** Edge cases for Exercise 7. */
public class Edge7 { static int fails=0; static void check(boolean ok,String what){System.out.println((ok?"  OK    ":"  FAIL  ")+what); if(!ok)fails++;} static void done(){System.out.println(fails==0?"ALL EDGE CASES PASSED":fails+" FAILED");}
 public static void main(String[] a){
  boolean ok=true; try{ new MyFuture<String>().set("x"); }catch(Exception e){ ok=false; }
  check(ok, "set() with nothing chained does not crash");
  MyFuture<String> m=new MyFuture<>(); m.set("5"); String[] got={null};
  m.thenDo(s->s+"!").finallyDo(x->got[0]=x);
  check("5!".equals(got[0]), "a chain added after set() runs immediately");
  MyFuture<Integer> b=new MyFuture<>(); int[] r={0,0};
  MyFuture<Integer> t=b.thenDo(x->x+1); t.finallyDo(x->r[0]=x); t.thenDo(x->x*100).finallyDo(x->r[1]=x);
  b.set(1);
  check(r[0]==2 && r[1]==200, "one future feeding two separate chains");
  MyFuture<String> s=new MyFuture<>(); Boolean[] even={null};
  s.thenDo(String::length).thenDo(n->n%2==0).finallyDo(x->even[0]=x); s.set("abcd");
  check(Boolean.TRUE.equals(even[0]), "type changes along the chain: String -> Integer -> Boolean");
  done();
 }
}
