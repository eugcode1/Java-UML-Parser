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
        if (args.length != 0)
        {
            System.out.println("Invalid input, re-run the command");
            System.exit(1);
        }
        String filePath = "src/test/java/uml-parser-test-" + 5;
        String outputPath = "src/test/output.png";
        umlParser obj = new umlParser(filePath, outputPath);
        obj.parse();
        System.exit(1);

    }
}
