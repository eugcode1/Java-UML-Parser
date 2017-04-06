import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String origin = scanner.next();
        if (origin.length() == 0)
        {
            System.out.println("Invalid input, re-enter the testcase");
            origin = scanner.next();
        }
        umlParser obj = new umlParser("src/test/java/uml-parser-test-3", "src/test/output3.png");
        obj.parse();
        System.exit(1);
    }
}
