import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class App {
    //setup the file path
    private static String filePath;
    private static File fileFolder;
    private static File[] fileList;
    private static StringBuilder umlURL = new StringBuilder();
    private static String outputFile;
    private List<String> varList;
    private List<String> funcList;
    private static final String spliter = ",";//splitting one relationship
    private static final String c_ini = "[";//class ini
    private static final String c_end = "]";//class end
    private static final String c_interface = "<<interface>>";//interface
    public static void main(String[] args) throws Exception {

        filePath = "src/test/java/uml-parser-test-1";
        fileFolder = new File(filePath);
        fileList = fileFolder.listFiles();
        outputFile = "test1.png";
        App obj = new App();
        obj.parse();
/*
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("src/test/java/uml-parser-test-1/A.java");

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

    private void parse(){
        try {
            for(File file:fileList){
                if(file.isFile()){
                    if(umlURL.length() > 0){
                        umlURL.append(spliter);
                    }
                    varList = new ArrayList<String>();
                    funcList = new ArrayList<String>();
                    // creates an input stream for the file to be parsed
                    FileInputStream in = new FileInputStream(filePath + "/" + file.getName());
                    // parse the file
                    CompilationUnit cu = JavaParser.parse(in);
                    System.out.println(cu.toString());

                }
            }
        }catch(Exception e) {
            System.out.println(e);
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
