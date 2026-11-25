import test.*;
/** Edge cases for Exercise 3. */
public class Edge3 { static int fails=0; static void check(boolean ok,String what){System.out.println((ok?"  OK    ":"  FAIL  ")+what); if(!ok)fails++;} static void done(){System.out.println(fails==0?"ALL EDGE CASES PASSED":fails+" FAILED");}
 public static void main(String[] a){
  check(Q3.calc("6-2-1")==3, "minus is left associative: 6-2-1 = 3");
  check(Q3.calc("8/4/2")==1, "division is left associative: 8/4/2 = 1");
  check(Q3.calc("2-3*4+5")==-5, "precedence: 2-3*4+5 = -5");
  check(Q3.calc("((2))")==2, "redundant brackets");
  check(Q3.calc("10 - 4 / 2")==8, "spaces are ignored");
  check(Q3.calc("100/(2+3)/4")==5, "brackets in the middle");
  check(Q3.calc("42")==42, "a single number");
  check(Q3.calc("0.1+0.2")==0.3, "floating-point noise removed: 0.1+0.2 = 0.3");
  check(Double.isInfinite(Q3.calc("1/0")), "division by zero gives Infinity (no crash)");
  Expression e=new Plus(new test.Number(3),new Mul(new Div(new test.Number(4),new test.Number(2)),new test.Number(5)));
  check(e.calculate()==13, "the tree from the booklet: 3+(4/2)*5 = 13");
  done();
 }
}
