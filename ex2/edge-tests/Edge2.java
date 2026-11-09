import test.MyStringBuilder;
/** Edge cases for Exercise 2. */
public class Edge2 { static int fails=0; static void check(boolean ok,String what){System.out.println((ok?"  OK    ":"  FAIL  ")+what); if(!ok)fails++;} static void done(){System.out.println(fails==0?"ALL EDGE CASES PASSED":fails+" FAILED");}
 public static void main(String[] a){
  check(new MyStringBuilder().toString().equals(""), "empty builder");
  check(new MyStringBuilder().reverse().toString().equals(""), "reverse of empty");
  check(new MyStringBuilder().append("").toString().equals(""), "append empty string");
  check(new MyStringBuilder().append("abcd").reverse().toString().equals("dcba"), "reverse even length");
  check(new MyStringBuilder().append("abcde").reverse().toString().equals("edcba"), "reverse odd length");
  check(new MyStringBuilder('x').append("yz").reverse().toString().equals("zyx"), "char constructor + append + reverse");
  check(new MyStringBuilder().append("hello").apply(c->c=='l'?'L':c).append("!").apply(c->(char)(c+1)).toString().equals("ifMMp\""), "two different apply functions mixed with append");
  MyStringBuilder sb=new MyStringBuilder().append("ab"); sb.append("cd");
  check(sb.toString().equals("abcd"), "non-chained calls change the same builder");
  check(sb.append("x")==sb && sb.reverse()==sb && sb.apply(c->c)==sb, "every method returns this (fluent)");
  done();
 }
}
