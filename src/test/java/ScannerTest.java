import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;

/**
 * Test the functions of the scanner
 */
public class ScannerTest {

  @Test
  public void test_isNumericAtom() {
    Scanner scanner = new Scanner();
    assertTrue(scanner.isNumericAtom("123"));
    assertFalse(scanner.isNumericAtom("AB123"));
    assertTrue(scanner.isNumericAtom("0"));
    assertFalse(scanner.isNumericAtom("001"));
  }

  @Test
  public void test_isLiteralAtom() {
    Scanner scanner = new Scanner();
    assertTrue(scanner.isLiteralAtom("AB123"));
    assertTrue(scanner.isLiteralAtom("ABC"));
    assertFalse(scanner.isLiteralAtom("12ABC"));
  }

}
