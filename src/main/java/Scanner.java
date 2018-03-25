import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class Scanner {

  /**
   * Check if the string is a numeric atom
   *
   * @return whether the string is a numeric atom
   */
  public boolean isNumericAtom(String s) {
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

  /**
   * Check if the string is a literal atom
   *
   * @return whether the string is a numeric atom
   */
  public boolean isLiteralAtom(String s) {
    if (s == null) {
      return false;
    }
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

  /**
   * Get the next token
   *
   * @return a Token object
   */
  public Token getNextToken(BufferedReader br) {
    char temp;
    Token token = new Token();

    while (true) {
      try {
        int index = br.read();
        temp = (char) index;
        //Encounter a space or a new line or reach the end of file
        if (temp == '\n' || temp == '\r' || temp == '\t' || temp == ' ' || index == -1) {
          if (token.getToken() == null) {
            if (index == -1) {
              break;
            } else {
              continue;
            }
          } else {
            if (isLiteralAtom(token.getToken())) {
              token.setType(TYPE.LITERAL_ATOM);
            } else if (isNumericAtom(token.getToken())) {
              token.setType(TYPE.NUMERIC_ATOM);
            } else {
              token.setType(TYPE.ERROR);
            }
          }
        }  //Open parenthesis
        else if (temp == '(') {
          if (token.getToken() != null) {
            if (isNumericAtom(token.getToken())) {
              token.setType(TYPE.NUMERIC_OPEN_PARENTHESIS);
            } else if (isLiteralAtom(token.getToken())) {
              token.setType(TYPE.LITERAL_OPEN_PARENTHESIS);
            } else {
              token.setType(TYPE.INVALID_PREFIX);
            }
          } else {
            token.setType(TYPE.OPEN_PARENTHESIS);
            token.setToken("(");
          }
        } //Closing parenthesis
        else if (temp == ')') {
          if (token.getToken() != null) {
            if (isNumericAtom(token.getToken())) {
              token.setType(TYPE.NUMERIC_CLOSING_PARENTHESIS);
            } else if (isLiteralAtom(token.getToken())) {
              token.setType(TYPE.LITERAL_CLOSING_PARENTHESIS);
            } else {
              token.setType(TYPE.INVALID_PREFIX);
            }
          } else {
            token.setType(TYPE.CLOSING_PARENTHESIS);
            token.setToken(")");
          }
        } //Character
        else {
          String cur = "";
          if (token.getToken() != null) {
            cur = token.getToken();
          }
          token.setToken(cur + temp);
          continue;
        }
        return token;
      } catch (IOException e) {
        System.out.println("Error with the buffered reader");
        e.printStackTrace();
      }
    }
    token.setType(TYPE.EOF);
    token.setToken("");
    return token;

  }

  /**
   * Read the input string and get the result for lexical analysis
   *
   * @return the lexical analysis results
   */
  public String read() {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    Token myToken;
    int num_atom = 0;
    int literal_atom = 0;
    int open_par = 0;
    int close_par = 0;

    Queue<String> str_queue = new LinkedList<>();
    Queue<Integer> num_queue = new LinkedList<>();
    while (true) {
      myToken = getNextToken(br);
      TYPE type = myToken.getType();
      if (type == TYPE.OPEN_PARENTHESIS || type == TYPE.LITERAL_OPEN_PARENTHESIS
          || type == TYPE.NUMERIC_OPEN_PARENTHESIS) {
        open_par++;
        if (type == TYPE.LITERAL_OPEN_PARENTHESIS) {
          str_queue.add(myToken.getToken());
          literal_atom++;
        } else if (type == TYPE.NUMERIC_OPEN_PARENTHESIS) {
          num_queue.add(Integer.parseInt(myToken.getToken()));
          num_atom++;
        }

      } else if (type == TYPE.CLOSING_PARENTHESIS || type == TYPE.LITERAL_CLOSING_PARENTHESIS
          || type == TYPE.NUMERIC_CLOSING_PARENTHESIS) {
        close_par++;
        if (type == TYPE.LITERAL_CLOSING_PARENTHESIS) {
          literal_atom++;
          str_queue.add(myToken.getToken());
        } else if (type == TYPE.NUMERIC_CLOSING_PARENTHESIS) {
          num_atom++;
          num_queue.add(Integer.parseInt(myToken.getToken()));
        }

      } else if (type == TYPE.LITERAL_ATOM) {
        str_queue.add(myToken.getToken());
        literal_atom++;
      } else if (type == TYPE.NUMERIC_ATOM) {
        num_atom++;
        num_queue.add(Integer.parseInt(myToken.getToken()));
      } else if (type == TYPE.ERROR || type == TYPE.INVALID_PREFIX) {
        System.out.println("ERROR: Invalid token " + myToken.getToken());
        System.exit(0);
      } else if (type == TYPE.EOF) {
        break;
      }

    }

    String res = "";
    res += "LITERAL ATOMS: " + literal_atom;
    if (literal_atom == 0) {
      res += "\n";
    } else {
      res += ", ";
    }
    while (str_queue.size() > 0) {
      res += str_queue.poll();
      if (str_queue.size() != 0) {
        res += ", ";
      } else {
        res += "\n";
      }
    }
    int sum = 0;
    while (num_queue.size() > 0) {
      sum += num_queue.poll();
    }
    res += "NUMERIC ATOMS: " + num_atom;
    if (num_atom > 0) {
      res += ", " + sum;
    }
    res += "\n";
    res += "OPEN PARENTHESES: " + open_par + "\n";
    res += "CLOSING PARENTHESES: " + close_par + "\n";
    return res;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner();
    String res = sc.read();
    System.out.println(res);
  }

}
