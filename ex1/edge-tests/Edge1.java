import test.*;
import java.util.*;
/** Edge cases for Exercise 1 (run after compiling with the test package). */
public class Edge1 { static int fails=0; static void check(boolean ok,String what){System.out.println((ok?"  OK    ":"  FAIL  ")+what); if(!ok)fails++;} static void done(){System.out.println(fails==0?"ALL EDGE CASES PASSED":fails+" FAILED");}
 static String all(PlayList p){StringBuilder sb=new StringBuilder(); for(String s:p) sb.append('[').append(s).append(']'); return sb.toString();}
 public static void main(String[] a){
  PlayList p2=new PlayList("P2", new Item[]{new Song("s21"),new Song("s23")});
  PlayList p1=new PlayList("P1", new Item[]{new Song("s11"),p2,new Song("s13")});
  check(all(p1).equals("[s11][s21][s23][s13]"), "one song per next(), nested list expanded: "+all(p1));
  PlayList empty=new PlayList("E", new Item[]{});
  check(all(empty).equals(""), "empty play list gives nothing");
  PlayList withEmpty=new PlayList("W", new Item[]{empty,new Song("a"),new PlayList("X",new Item[]{empty}),new Song("b")});
  check(all(withEmpty).equals("[a][b]"), "empty sub-lists (also nested) are skipped: "+all(withEmpty));
  PlayList lastIsList=new PlayList("L", new Item[]{new Song("a"),p2});
  check(all(lastIsList).equals("[a][s21][s23]"), "play list as the last item");
  Iterator<String> it=p1.iterator();
  check(it.hasNext() && it.hasNext() && it.next().equals("s11"), "hasNext() twice does not skip a song");
  it.next(); it.next(); it.next();
  check(!it.hasNext(), "hasNext() false at the end");
  boolean thrown=false; try{it.next();}catch(NoSuchElementException e){thrown=true;}
  check(thrown, "next() after the end throws NoSuchElementException");
  PlayList deep=new PlayList("D0",new Item[]{new Song("x")});
  for(int i=0;i<10000;i++) deep=new PlayList("D",new Item[]{deep});
  check(all(deep).equals("[x]"), "10000 nested levels: no StackOverflowError (explicit stack)");
  done();
 }
}
