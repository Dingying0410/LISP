import java.util.ArrayList;
import java.util.List;

public class Interpreter extends Parser {

  private static List<String> func_names_list = new ArrayList<>();
  private static FuncList funcList = new FuncList();

  public void evalStart() {
    Node root = new Node("");
    Node aList = new Node("NIL");
    while (getCurrent().getType() != TYPE.EOF) {
      parseExpr(root);
      Node res = eval(root, aList, funcList.getfList());
      if (res.left != null) {
        printInterpreteRes(res);
      } else {
        System.out.print(res.data);
      }
      root = new Node("");
      System.out.println();
    }
  }

  public static Node eval(Node oldNode, Node aList, Node dList) {

    Parser p = new Parser();
    Node result = new Node("");
    if (oldNode.left == null)  //atom
    {
      if (Functions.isNumericAtom(oldNode.data) || oldNode.data.equals("T") || oldNode.data
          .equals("NIL")) {
        result.setData(oldNode.data);
        return result;
      } else if (Functions.bound(oldNode, aList)) {
        return Functions.getVal(oldNode, aList);
      } else {
        System.out.println("ERROR: UNDEFINED FOR LITERAL ATOM " + oldNode.data);
        System.exit(0);
      }
    } else {

      Node TL = oldNode.left;
      String str = TL.data;
      Node TR = oldNode.right;
      if (TL.data == null) {
        System.out.println("Evaluate to NULL");
        System.exit(0);
      }
      if (str.equals("PLUS") || str.equals("MINUS") || str.equals("TIMES") || str.equals("LESS")
          || str.equals("GREATER")) {
        if (TR.left == null) {
          System.out.println("ERROR: OPERAND FOR " + str + " IS REQUIRED!");
          System.exit(0);
        } else if (TR.right.left == null) {
          System.out
              .println("ERROR: OPERAND FOR " + TR.left.data + " AND " + str + " IS REQUIRED!");
          System.exit(0);
        } else if (TR.right.right != null && !(TR.right.right.data.equals("NIL")) &&
            !TR.right.right.left.data.equals("")) {
          System.out.println("ERROR: TOO MANY OPERANDS ON " + str);
          System.exit(0);
        } else {
          Node p1 = eval(TR.left, aList, dList);
          Node p2 = eval(TR.right.left, aList, dList);
          if (Functions.isNumericAtom(p1.data) && Functions.isNumericAtom(p2.data)) {
            int a1, a2;
            a1 = Integer.parseInt(p1.data);
            a2 = Integer.parseInt(p2.data);
            if (str.equals("PLUS")) {
              int res = a1 + a2;
              result.setData(String.valueOf(res));
            } else if (str.equals("MINUS")) {
              int res = a1 - a2;
              result.setData(String.valueOf(res));
            } else if (str.equals("TIMES")) {
              int res = a1 * a2;
              result.setData(String.valueOf(res));
            } else if (str.equals("LESS")) {
              if (a1 < a2) {
                result.setData("T");
              } else {
                result.setData("NIL");
              }
            } else if (str.equals("GREATER")) {
              if (a1 > a2) {
                result.setData("T");
              } else {
                result.setData("NIL");
              }
            }
            return result;
          } else {
            System.out.println("ERROR: INVALID OPERANDS: " + p1.data + " , " + p2.data);
            System.exit(0);
          }
        }
      } else if (str.equals("EQ")) {
        if (TR.left == null) {
          System.out.println("ERROR: OPERAND REUIRED FOR " + str);
          System.exit(0);
        } else if (TR.right.left == null) {
          System.out.println("ERROR: OPERAND REUIRED FOR " + str + " and " + TR.left.data);
          System.exit(0);
        } else if (TR.right.right != null && !TR.right.right.data.equals("NIL") &&
            TR.right.right.left.data != "") {
          System.out.println("ERROR: TOO MANY OPERANDS");
          System.exit(0);
        } else {
          Node p1 = eval(TR.left, aList, dList);
          Node p2 = eval(TR.right.left, aList, dList);
          if (!(p1.data.equals("") || p2.data.equals(""))) {
            if (p1.data.equals(p2.data)) {
              result.setData("T");
              return result;
            } else {
              result.setData("NIL");
              return result;
            }
          } else {
            System.out.println("ERROR: INVALID OPERAND FOR " + str);
            System.exit(0);
          }

        }
      } else if (str.equals("INT") || str.equals("ATOM") || str.equals("NULL")) {
        if (TR.left != null && TR.right != null && TR.right.data.equals("NIL")) {
          if (str.equals("ATOM")) {

            Node p1 = eval(TR.left, aList, dList);
            if (!p1.data.equals("")) {
              result.setData("T");
            } else {
              result.setData("NIL");
            }
          } else if (str.equals("INT")) {
            Node p1 = eval(TR.left, aList, dList);
            if (Functions.isNumericAtom(p1.data) == true) {
              System.out.println(p1.data);
              result.setData("T");
            } else {
              result.setData("NIL");
            }
          } else {

            Node p1 = eval(TR.left, aList, dList);
            if (p1.data.equals("NIL")) {
              result.setData("T");
            } else {
              result.setData("NIL");
            }
          }
          return result;

        } else {
          System.out.println("ERROR: THE OPERANDS ARE INVALID FOR " + str);
          System.exit(0);
        }
      } else if (str.equals("CAR") || str.equals("CDR")) {
        if (TR.left != null && TR.right != null && TR.right.data.equals("NIL")) {
          Node p1 = eval(TR.left, aList, dList);
          if (p1.left == null) {
            System.out.println("ERROR: INVALID OPEREAND FOR " + str);
            System.exit(0);
          }
          if (str.equals("CAR")) {
            result = p1.left;
          } else {
            result = p1.right;
          }
          return result;
        } else {
          System.out.println("ERROR: INVALID OPEREAND FOR " + str);
          System.exit(0);
        }
      } else if (str.equals("CONS")) {
        if (TR.left != null) {
          if (TR.right != null && TR.left != null && TR.right.right != null &&
              TR.right.right.data.equals("NIL")) {
            result.left = new Node("");
            result.right = new Node("");
            Node p1 = eval(TR.left, aList, dList);
            Node p2 = eval(TR.right.left, aList, dList);
            result.left = p1;
            result.right = p2;
            return result;
          } else {
            System.out.println("ERROR: INVALID OPERANDS FOR CONS");
            System.exit(0);
          }
        } else {
          System.out.println("ERROR: OPEERNADS ARE REQUIRED FOR CONS");
          System.exit(0);
        }
      } else if (str.equals("QUOTE")) {
        if (TR.left != null && TR.right != null && TR.right.data.equals("NIL")) {
          result = TR.left;
          return result;
        } else {
          System.out.println("ERROR: INVALID OPERAND FOR " + str);
          System.exit(0);
        }
      } else if (str.equals("COND")) {

        Node p1 = TR;
        while (p1.right != null) {
          if (p1.left != null && p1.left.left != null &&
              p1.left.right.left != null && p1.left.right.right.data.equals("NIL")) {
            p1 = p1.right;
          } else {
            System.out.println("ERROR: INVALID OPEERAND FOR COND");
            System.exit(0);
          }
        }
        p1 = TR;
        while (p1.right != null) {
          Node cond = eval(p1.left.left, aList, dList);
          //System.out.println("conditon "+ b2.data);
          if (!cond.data.equals("NIL")) {
            result = eval(p1.left.right.left, aList, dList);
            return result;
          } else {
            p1 = p1.right;
          }
        }
        if (p1.right == null) {
          System.out.println("ERROR: NO CONDITIONS ARE SATISFIED IN COND");
          System.exit(0);
        }

      } else if (str.equals("DEFUN")) {
        int len = getLength(TR);
        func_names_list.add(TR.left.data);
        if (len != 3) {
          System.out.println("INVALID OPERAND FOR DEFUN");
          System.exit(0);
        }
        if (Functions.checkFunctionType(Functions.cdr(oldNode))) {
          Node node1 = Functions.cons(Functions.car(Functions.cdr(Functions.cdr(oldNode))),
              Functions.car(Functions.cdr(Functions.cdr(Functions.cdr(oldNode)))));
          Node node2 = Functions.cons(Functions.car(Functions.cdr(oldNode)), node1);
          funcList.setfList(Functions.cons(node2, funcList.getfList()));
          result = Functions.car(Functions.cdr(oldNode));
        }
      } else if (func_names_list.contains(str)) {
        result = apply(Functions.car(oldNode),
            Functions.evlist(Functions.cdr(oldNode), aList, dList), aList, dList);
      } else {
        System.out.println("ERROR: EXPRESSION " + str + " IS UNDEFINED");
        System.exit(0);
      }
    }
    return result;
  }


  public static int getLength(Node node) {
    int length = 0;
    while (node != null) {
      if (node.left != null) {
        length++;
      }
      node = node.right;
    }
    return length;
  }


  public static Node apply(Node f, Node params, Node aList, Node dList) {
    int length = getLength(params);
    int paramNum = getLength(Functions.car(Functions.getVal(f, dList)));
    if (length != paramNum) {
      System.out.println("The number of parameters do not match!");
      System.exit(0);
    }
    Node res = eval(Functions.cdr(Functions.getVal(f, dList)),
        Functions.addPairs(Functions.car(Functions.getVal(f, dList)), params, aList), dList);

    return res;
  }

  public static void printInterpreteRes(Node root) {
    if (root != null) {
      if (root.left == null && root.right == null) {
        System.out.print(root.data);
      } else {
        System.out.print("(");
        Node temp = root;
        while (temp.right != null) {
          printInterpreteRes(temp.left);
          if (temp.right.right != null) {
            System.out.print(" ");
          }
          temp = temp.right;
        }
        if (temp.data.equals("NIL")) {
          System.out.print(")");
        } else {
          System.out.print(" . " + temp.data + ")");
        }

      }
    } else {
      System.out.println("Empty");
    }
  }

  public static void main(String[] args) {
    Interpreter interpreter = new Interpreter();
    interpreter.init();
    interpreter.evalStart();
  }
}
