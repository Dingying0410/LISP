public class FuncList {

  private static Node fList;

  public static void setfList(Node fList) {
    FuncList.fList = fList;
  }

  public Node getfList() {
    if (fList == null) {
      fList = new Node("NIL");
    }
    return fList;
  }

}
