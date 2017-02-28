import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;

public class App {
    //setup the file path
    private static String filePath;
    private static File fileFolder;
    private static File[] fileList;
    private static StringBuilder umlURL = new StringBuilder();
    private static String outputFile;
    public static void main(String[] args) throws Exception {

        filePath = "src/test/java/uml-parser-test-1";
        fileFolder = new File(filePath);
        fileList = fileFolder.listFiles();
        outputFile = "test1.png";
        App obj = new App();
        obj.parse();
        /*
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("src/test/java/Hello1.java");

        // parse the file
        CompilationUnit cu = JavaParser.parse(in);

        // prints the resulting compilation unit to default system output
        System.out.println(cu.toString());

        // visit and print the methods names
        new MethodVisitor().visit(cu, null);
        */
        //Diagram generator
        umlURL.append("\"[A%7C-x:int;-y:int(*)]1-0..*[B],[A]-1[C],[A]-*[D]\"");
        ImgGenerator sample = new ImgGenerator(umlURL.toString());
        System.exit(1);
    }
    public void parse(){
        for(File file:fileList){
            if(file.isFile()){
                System.out.println(file.getName());
            }
        }
    };
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
