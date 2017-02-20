import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileInputStream;

public class App {
    public static void main(String[] args) throws Exception {
        ImgGenerator sample = new ImgGenerator();
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("src/test/java/Hello1.java");

        // parse the file
        CompilationUnit cu = JavaParser.parse(in);

        // prints the resulting compilation unit to default system output
        //System.out.println(cu.toString());

        // visit and print the methods names
        new MethodVisitor().visit(cu, null);
    }
    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
            System.out.println(n.getName());
            super.visit(n, arg);
        }
    }
}
