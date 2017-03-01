import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class umlParser {
    //setup the file path
    private static String filePath;
    private static File fileFolder;
    private static File[] fileList;
    private static StringBuilder umlURL = new StringBuilder();
    private static String outputFile;
    private List<CompilationUnit> cuList;
    private List<String> classList;
    private List<String> interfaceList;
    private List<String> methodList;
    private HashMap<String, List<String>> varList;

    private HashMap<String, List<String>> c_interMap;//class map to implements interface
    private HashMap<String, List<String>> c_superMap;//class map to extends super class
    private boolean class_checker = false;
    private static final String spliter = ",";//splitting one relationship
    private static final String c_ini = "[";//class ini
    private static final String c_end = "]";//class end
    private static final String c_interface = "<<interface>>";//interface

    public umlParser(){
        this.filePath = "src/test/java/uml-parser-test-2";
        this.fileFolder = new File(filePath);
        this.fileList = fileFolder.listFiles();
        this.outputFile = "test1.png";
        this.cuList = new ArrayList<CompilationUnit>();
        this.classList = new ArrayList<String>();
        this.interfaceList = new ArrayList<String>();
        this.c_superMap = new HashMap<String, List<String>>();
        this.c_interMap = new HashMap<String, List<String>>();

        //umlURL.append("\"[A%7C-x:int;-y:int(*)]1-0..*[B],[A]-1[C],[A]-*[D]\"");
        ImgGenerator sample = new ImgGenerator(umlURL.toString());
    }
    public void parse(){
        try {
            for(File file:fileList){
                if(file.isFile()){
                    methodList = new ArrayList<String>();
                    // creates an input stream for the file to be parsed
                    FileInputStream in = new FileInputStream(filePath + "/" + file.getName());
                    CompilationUnit cu = JavaParser.parse(in);
                    in.close();
                    cuList.add(cu);
                    buildClassInterfaceList(cu);
                    buildContents(cu);
                }
            }
        }catch(Exception e) {
            System.out.println(e);
        }
    }

    //Build class/interface map of each cu
    public void buildClassInterfaceList(CompilationUnit cu){
        List<Node> childrenNodes = cu.getChildNodes();
        for(Node c: childrenNodes){
            if(c instanceof ClassOrInterfaceDeclaration){
                ClassOrInterfaceDeclaration dec_checker = (ClassOrInterfaceDeclaration) c;
                //build interface list
                if(dec_checker.isInterface()){
                    class_checker = false;
                    interfaceList.add(dec_checker.getName().toString());
                }else{//build class list
                    class_checker = true;
                    classList.add(dec_checker.getName().toString());
                    //class extends superclass
                    List<String> extendsList = new ArrayList<String>();
                    for(Node n:dec_checker.getExtendedTypes()){
                        if(n != null)extendsList.add(n.toString());
                    }
                    if (!extendsList.isEmpty()) {
                        c_superMap.put(dec_checker.getName().toString(), extendsList);
                    }
                    //class implements interface
                    List<String> implementsList = new ArrayList<String>();
                    for(Node n:dec_checker.getImplementedTypes()){
                        if(n != null)implementsList.add(n.toString());
                    }
                    if (!implementsList.isEmpty()) {
                        c_interMap.put(dec_checker.getName().toString(), implementsList);
                    }
                }
            }
        }
        //System.out.println(interfaceList);
        //System.out.println(">>>>>>>>>>");
    }

    //Build class contents
    public void buildContents(Node child){
        //check looping
            if(child instanceof FieldDeclaration){
                FieldDeclaration field = (FieldDeclaration) child;
                String accessMod = "";
                if(field.isPrivate()){
                    accessMod = "-";
                }else if(field.isPublic()){
                    accessMod = "+";
                }
            }else if(child instanceof MethodDeclaration){
                if(class_checker) {
                    buildMethods(child);
                }
            }else if(child instanceof ConstructorDeclaration){

            }
            for (Node n : child.getChildNodes()){
                //System.out.println(n);
                buildContents(n);
            }
    }

    public void buildMethods(Node node) {
        MethodDeclaration method = (MethodDeclaration)node;
        StringBuilder method_str = new StringBuilder();
        String method_type = "";
        String method_name = "";
        String accessMod = "";

        boolean access_checker = false;
        if(method.isPrivate()){
            accessMod = "-";
        }else if(method.isPublic()){//static>?
            accessMod = "+";
            access_checker = true;
        }
        method_str.append(accessMod);

        if (access_checker) {//if is class
            //System.out.println(node);
            method_type = method.getType().toString();
            method_name = method.getName().toString();
            method_str.append(method_name);

            method_str.append("(");
            for(Parameter para:method.getParameters()){
                //pare is by pair like: A1 a1
                String[] tmp = para.toString().split(" ");
                if(tmp.length == 2) {
                    method_str.append(tmp[1] + ":" + tmp[0]);
                    method_str.append(",");
                }
            }
            if(method.getParameters().isEmpty()){
                method_str.append(")");
            } else{
                method_str.setLength(method_str.length() - 1);//remove last comma
                method_str.append(")");
            }
            method_str.append(":" + method_type);
            System.out.println(method_str);
            if (method_name.startsWith("get") || method_name.startsWith("set")) {
                //IGNORE
            } else {
                methodList.add(method_str.toString());
            }
        }
    }
}
