import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyArchetypeTest {

  @Test
  public void generatedProjectStructureValidTest() {
    String userDir = System.getProperty("user.home");
    File projectDir = new File(userDir + "/Documents/IDEAProjects/sample");

    assert projectDir.exists() && projectDir.isDirectory() : "Generated project directory exist";
  }

  @Test
  public void testClassNames() {

    assertTrue(validateClassName("MyArchetypeTest"));
  }

  @Test
  public void testMethodNames() {
    assertTrue(validateMethodName("validateMethodName"));
    assertTrue(validateMethodName("validateClassName"));
  }

  private boolean validateClassName(String className) {
    return className.matches("[A-Z][a-zA-Z0-9]*");
  }

  private boolean validateMethodName(String methodName) {
    return methodName.matches("[a-z][a-zA-Z0-9]*");
  }

  @Test
  public void pomXmlFileContentIsValidTest() throws IOException {
    String userDir = System.getProperty("user.home");
    String filePath = userDir + "/Documents/IDEAProjects/DDDProject/pom.xml";
    String fileContent = readFile(new File(filePath));

    String actualPomXml = "pom.xml";

    Diff diff = DiffBuilder.compare(Input.fromString(fileContent))
      .withTest(Input.fromFile(actualPomXml))
      .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
      .checkForSimilar()
      .build();

    assertFalse(" actual pom.xml matches the expected version", diff.hasDifferences());
  }

  private String readFile(File selectedFile) throws IOException {
    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line).append("\n");
      }
    }
    return content.toString();
  }

  @Test
  public void pomXmlFileContentIsNotValidTest() {

    String pomXmlContent = "<project></project>\n";
    String actualPomXml = "pom.xml";

    Diff diff = DiffBuilder.compare(Input.fromString(pomXmlContent))
      .withTest(Input.fromFile(actualPomXml))
      .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
      .checkForSimilar()
      .build();

    assertTrue(" actual pom.xml doesn't matches the expected version", diff.hasDifferences());
  }
}


