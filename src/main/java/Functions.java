import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Functions {

  public static boolean isNumericAtom(String s) {
    if (s == null || s.equals("") || s.length() == 0) {
      return false;
    }
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) < '0' || s.charAt(i) > '9') {
        if (i == 0 && s.charAt(i) == '-') {
          continue;
        } else {
          return false;
        }
      } else if (s.charAt(i) == '0') {
        if (i == 0 && s.length() > 1) {
          return false;
        }
      }
    }
    return true;
  }

  public static boolean isLiteralAtom(String s) {
    if (s == null || s.equals("") || s.length() == 0) {
      return false;
    }
    //System.out.println(s);
    if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
      return false;
    }
    for (int i = 0; i < s.length(); i++) {
      if ((s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') ||
          (s.charAt(i) >= '0' && s.charAt(i) <= '9')) {
        continue;
      } else {
        return false;
      }

    }
    return true;
  }

  public static boolean isAtom(String s) {
    return isNumericAtom(s) || isLiteralAtom(s);
  }
  
  public static Node car(Node node) {
    if (node == null || isAtom(node.data)) {
      System.out.println("THE VARIABLE IS NULL OR NOT INITIALIZED");
      System.exit(0);
    }
    return node.left;
  }

  public static Node cdr(Node node) {
    if (node == null || isAtom(node.data)) {
      System.out.println("THE VARIABLE IS NULL OR NOT INITIALIZED");
      System.exit(0);
    }
    return node.right;
  }

  public static Node cons(Node node1, Node node2) {
    Node res = new Node("");
    if (node1 == null && node2 == null) {
      System.out.println("INVALID OPERAND");
      System.exit(0);
    }
    if (node1 == null) {
      return node2;
    }
    if (node2 == null) {
      return node1;
    }
    res.left = node1;
    res.right = node2;
    return res;
  }

  public static Node evlist(Node x, Node aList, Node dList) {
    Node res = new Node("");
    if (x == null || x.data.equals("NIL")) {
      res.setData("NIL");
    } else {
      Node node1 = Interpreter.eval(Functions.car(x), aList, dList);
      res = Functions.cons(node1, evlist(Functions.cdr(x), aList, dList));
    }
    return res;
  }

  public static Node addPairs(Node xList, Node yList, Node z) {
    if (xList == null || xList.data.equals("NIL")) {
      return z;
    }
    Node node1 = Functions.cons(Functions.car(xList), Functions.car(yList));
    return Functions.cons(node1, addPairs(Functions.cdr(xList), Functions.cdr(yList), z));
  }

  public static boolean equal(Node a, Node b) {
    if (a != null && b != null) {
      if (isAtom(a.data) && isAtom(b.data)) {
        if (isNumericAtom(a.data) && isNumericAtom(b.data)) {
          int av = Integer.parseInt(a.data);
          int bv = Integer.parseInt(b.data);
          return av == bv;
        } else if (a.data.equals(b.data)) {
          return true;
        }
      }
    }
    return false;

  }

  public static Node getVal(Node x, Node z) {
    Node res = null;
    if (isAtom(x.data)) {
      if (equal(x, car(car(z)))) {
        return cdr(car(z));
      } else {
        return getVal(x, cdr(z));
      }
    } else {
      System.out.println("Invalid operand for GetVal");
      System.exit(0);
    }
    return res;
  }

  public static boolean bound(Node x, Node z) {
    boolean bound = false;
    if (x == null || x.data.equals("NIL")) {
      return false;
    }
    if (isAtom(x.data)) {
      if (z == null) {
        return false;
      } else if (equal(x, car(car(z)))) {
        return true;
      } else {
        return bound(x, cdr(z));
      }
    } else {
      System.out.println("Invalid operand for bound");
      System.exit(0);
    }
    return bound;
  }

  public static boolean isFunctionParamDefined(Node node) {
    String[] defined = {"DEFUN", "PLUS", "MINUS", "TIMES", "LESS", "GREATER", "EQ", "ATOM",
        "INT", "NULL", "CAR", "CDR", "CONS", "QUOTE", "COND"};
    if (Arrays.asList(defined).contains(node.data)) {
      return true;
    } else {
      return false;
    }
  }

  public static List<String> printAtoms(Node node) {
    List<String> res = new ArrayList<>();
    if (node == null) {
      return res;
    }
    if (node.left == null && node.right == null && !node.data.equals("")) {
      res.add(node.data);
    }
    res.addAll(printAtoms(node.left));
    res.addAll(printAtoms(node.right));
    return res;

  }

  public static boolean checkFunctionType(Node node) {

    String str = Functions.car(node).data;
    if (Functions.isAtom(str)) {
      if (Functions.isNumericAtom(str) || str.equals("T") || str.equals("NIL")) {
        System.out.println("ERROR: INVALID FUNCTION NAME");
        System.exit(0);
      }
      if (isFunctionParamDefined(car(node))) {
        System.out.println("ERROR: THE FUNCTION NAME IS PREDEFINED");
        System.exit(0);
      }

      Node params = car(cdr(node));
      Node tempParams = params;
      HashSet<String> paramList = new HashSet<>();
      while (tempParams.right != null) {
        Node par = car(tempParams);
        if (isAtom(par.data)) {
          if (isNumericAtom(par.data)) {
            System.out.println("ERROR: PARAMETERS CANNOT BE INTEGERS");
            System.exit(0);
          }
          if (par.data.equals("T") || par.data.equals("NIL")) {
            System.out.println("ERROR: PARAMETERS CANNOT BE T OR NIL");
            System.exit(0);
          }
          if (!paramList.add(par.data)) {
            System.out.println("ERROR: THE PARAMETERS HAVE DUPLICATES");
            System.exit(0);
          }
          if (isFunctionParamDefined(par)) {
            System.out.println("ERROR: THE PARAMETERS CANNOT BE PREDEFINED");
            System.exit(0);
          }
        } else {
          System.out.println("ERROR: PARAMETER IS A LIST");
          System.exit(0);
        }
        tempParams = cdr(tempParams);
      }

    } else {
      System.out.println("ERROR: FUNCTION NAME CANNOT BE LIST");
      System.exit(0);
    }
    return true;
  }

}
