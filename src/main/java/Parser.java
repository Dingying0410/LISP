import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parser {

  private Token current;
  private static Scanner sc;
  private BufferedReader br;

  public Scanner getScanner() {
    if (sc == null) {
      sc = new Scanner();
    }
    return sc;
  }

  public void init() {
    br = new BufferedReader(new InputStreamReader(System.in));
    current = getScanner().getNextToken(br);
  }

  public Token getCurrent() {
    return current;
  }

  private void moveToNext() {
    current = sc.getNextToken(br);
  }

  public void parseStart() {
    init();
    while (current.getType() != TYPE.EOF) {
      Node root = new Node("");
      parseExpr(root);
      if (root.left == null) {
        if (root.data != "") {
          System.out.println(root.data);
        } else {
          System.out.print("NIL");
        }
      } else {
        print(root);
        System.out.println();
      }
    }
  }


  public void parseExpr(Node root) {
    if (current.getType() == TYPE.LITERAL_ATOM
        || current.getType() == TYPE.NUMERIC_ATOM) {
      root.setData(current.getToken());
      root.left = null;
      root.right = null;
      moveToNext();
    } else if (current.getType() == TYPE.LITERAL_OPEN_PARENTHESIS
        || current.getType() == TYPE.NUMERIC_OPEN_PARENTHESIS) {
      root.setData(current.getToken());
      current.setToken("(");
      current.setType(TYPE.OPEN_PARENTHESIS);
    } else if (current.getType() == TYPE.OPEN_PARENTHESIS) {
      moveToNext();
      if (current.getType() != TYPE.CLOSING_PARENTHESIS) {
        Node left = new Node("");
        root.left = left;
        Node right = new Node("");
        root.right = right;
      }
      while (current.getType() != TYPE.CLOSING_PARENTHESIS
          && current.getType() != TYPE.LITERAL_CLOSING_PARENTHESIS &&
          current.getType() != TYPE.NUMERIC_CLOSING_PARENTHESIS) {
        parseExpr(root.left);  // build the left tree recursively
        root = root.right;  //build the right tree
        root.setData("");
        if (current.getType() != TYPE.CLOSING_PARENTHESIS) {
          Node left = new Node("");
          root.left = left;
          Node right = new Node("");
          root.right = right;
        }
      }
      if (current.getType() == TYPE.CLOSING_PARENTHESIS) {
        if (root.left != null) {
          Parser p = new Parser();
          Node right = new Node("NIL");
          root.right = right;
        } else {
          root.setData("NIL");
        }
      } else if (current.getType() == TYPE.LITERAL_CLOSING_PARENTHESIS
          || current.getType() == TYPE.NUMERIC_CLOSING_PARENTHESIS) {
        if (root.left != null) {
          root.left.setData(current.getToken());
          Node right = new Node("NIL");
          root.right = right;
        } else {
          root.setData(current.getToken());
          ;
        }
      }
      moveToNext();
    } else {
      if (current.getType() == TYPE.CLOSING_PARENTHESIS
          || current.getType() == TYPE.LITERAL_CLOSING_PARENTHESIS ||
          current.getType() == TYPE.NUMERIC_CLOSING_PARENTHESIS) {
        try {
          Runtime.getRuntime().exec("clear");
        } catch (IOException e) {
          e.printStackTrace();
        }
        System.out.println("ERROR: Wrong grammar");
        System.exit(0);
      } else {
        System.out.println("ERROR: Invalid token");
        System.exit(0);
      }
    }
  }

  public static void print(Node root) {
    if (root != null) {
      if (root.data.equals("")) {
        System.out.print("(");
        print(root.left);
        System.out.print(".");
        print(root.right);
        System.out.print(")");
      } else {
        System.out.print(root.data);
      }
    }
  }


  public static void main(String[] args) {
    Parser p = new Parser();
    p.parseStart();
  }

}
