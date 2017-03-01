import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private List<String> fieldList;
    private HashMap<String, List<String>> c_interMap;//class map to implements interface
    private HashMap<String, List<String>> c_superMap;//class map to extends super class
    private HashMap<String, String> useMap;
    private HashMap<String, String> dependMap;
    private HashMap<String, String> multMap;
    private boolean class_checker = false;
    private static String class_name = "";
    private List<String> primitives = new ArrayList<String>(Arrays.asList("byte", "short", "int", "long", "float", "double", "boolean", "char","string", "Byte", "Short", "Integer", "Long", "Float", "Double", "Boolean", "Char", "String"));

    public umlParser(){
        this.filePath = "src/test/java/uml-parser-test-2";
        this.fileFolder = new File(filePath);
        this.fileList = fileFolder.listFiles();
        this.outputFile = "test1.png";
        this.cuList = new ArrayList<CompilationUnit>();
        this.classList = new ArrayList<String>();
        this.interfaceList = new ArrayList<String>();
        this.useMap = new HashMap<String, String>();
        this.multMap = new HashMap<String, String>();
        this.c_superMap = new HashMap<String, List<String>>();
        this.c_interMap = new HashMap<String, List<String>>();
    }
    public void parse(){
        try {
            for(File file:fileList){
                if(file.isFile()){
                    //add spliter',' to next class or interface
//                    if (umlURL.length() > 0 && (umlURL.charAt(umlURL.length() - 1) != ',')) {
//                        umlURL.append(",");
//                    }
                    umlURL.append("[");
                    //every cu has following list
                    methodList = new ArrayList<String>();
                    fieldList = new ArrayList<String>();
                    ///System.out.println(methodList);
                    // creates an input stream for the file to be parsed
                    FileInputStream in = new FileInputStream(filePath + "/" + file.getName());
                    CompilationUnit cu = JavaParser.parse(in);
                    in.close();
                    cuList.add(cu);
                    buildClassInterfaceList(cu);
                    buildContents(cu);

                    ////????
                    if (fieldList.size() > 0) {
                        umlURL.append("|");
                        for (int i = 0; i < fieldList.size(); i++) {
                            if (i != fieldList.size() - 1)
                                umlURL.append(fieldList.get(i) + ";");
                            else
                                umlURL.append(fieldList.get(i));
                        }
                    }

                    if (methodList.size() > 0) {
                        umlURL.append("|");
                        for (int i = 0; i < methodList.size(); i++) {
                            umlURL.append(methodList.get(i) + ";");
                        }
                    }
                    umlURL.append("]");
                    umlURL.append(",");
                }
            }
            buildUrl();
            System.out.println(umlURL);
            System.out.println(">>>>>>>>>>>");
            //Generating Diagram
            ImgGenerator outputImg = new ImgGenerator(umlURL.toString());
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
                    umlURL.append("<<Interface>>;");
                    umlURL.append(dec_checker.getName().toString());

                    class_checker = false;
                    interfaceList.add(dec_checker.getName().toString());
                }else{//build class list
                    umlURL.append(dec_checker.getName().toString());

                    class_checker = true;
                    class_name = dec_checker.getName().toString();
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
    }

    //Build class contents
    public void buildContents(Node child){
        //check looping
            if(child instanceof FieldDeclaration){
                buildField(child);
            }else if(child instanceof MethodDeclaration){
                if(class_checker) {
                    buildMethods(child);
                }
            }else if(child instanceof ConstructorDeclaration){

            }
            for (Node n : child.getChildNodes()){
                buildContents(n);
            }
    }

    //Build class contents - fields
    public void buildField(Node node){
        FieldDeclaration field = (FieldDeclaration) node;
        StringBuilder field_str = new StringBuilder();
        String field_type = "";
        String alia_type = "";//handle arrays case
        String field_name = "";
        String accessMod = "";
        boolean access_checker = false;//skip protected

        if(field.isPrivate()){
            accessMod = "-";
            access_checker = true;
        }else if(field.isPublic()){
            accessMod = "+";
            access_checker = true;
        }
        field_str.append(accessMod);

        if(access_checker){//if not protected type
            //System.out.println(field);
            field_type = field.getCommonType().toString();
            alia_type = field_type;
            if(field_type.contains("[]")){
                alia_type = field_type.replace("[]","");
                field_type = field_type.replace("[]","(*)");
            }
            for(VariableDeclarator var:field.getVariables()){
                field_name = var.toString();
                field_str.append(field_name);
                field_str.append(":" + field_type);
            }
            if(field_str.toString().contains("=")){//clear up variables with initialization
                field_str = new StringBuilder(field_str.substring(0, field_str.indexOf("=")).trim() + ";");
            }

            if(primitives.contains(alia_type)){
                fieldList.add(field_str.toString());
            }else{
               // System.out.println(field_type);//refClass
                //System.out.println(class_name);
                buildMultiplicity(field_type);
            }
        }
    }

    //Build class contents - methods
    public void buildMethods(Node node) {
        //System.out.println(node);
        MethodDeclaration method = (MethodDeclaration) node;
        StringBuilder method_str = new StringBuilder();
        String method_type = "";
        String method_name = "";
        String accessMod = "";
        boolean access_checker = false;

        if(method.isPrivate()){
            accessMod = "-";
        }else if(method.isPublic()){//static>??
            accessMod = "+";
            access_checker = true;
        }
        method_str.append(accessMod);

        if (access_checker) {//if is class
            method_type = method.getType().toString();
            method_name = method.getName().toString();
            method_str.append(method_name);
            method_str.append("(");
            for(Parameter para:method.getParameters()){
                //para is by pair like: A1 a1
                String[] tmp = para.toString().split(" ");
                if(tmp.length == 2) {
                    //tmp[0] is refType tmp[1] is para
                    method_str.append(tmp[1] + ":" + tmp[0]);
                    method_str.append(",");
                    if(interfaceList.contains(tmp[0])) {
                        if(!useMap.containsKey(tmp[0])) {
                            useMap.put(tmp[0], class_name);
                        }
                    }
                    ///System.out.println(useMap);
                }
            }
            if(method.getParameters().isEmpty()){
                method_str.append(")");
            } else{
                method_str.setLength(method_str.length() - 1);//remove last comma
                method_str.append(")");
            }
            method_str.append(":" + method_type);
            if (method_name.startsWith("get") || method_name.startsWith("set")) {
                //IGNORE
            } else {
                methodList.add(method_str.toString());
            }
        }
    }

    //Build field multiplicity
    public void buildMultiplicity(String field_type){
        String r_type = field_type;//relation string
        String r_key = "";
        String r_val = "";
        String r_revKey = "";

        if(r_type.contains("Collection")){
            r_val = "1-*";
            r_type = r_type.replace("Collection<","");
            r_type = r_type.replace(">","");
            r_key = class_name + "~" + r_type;
            r_revKey = r_type + "~" + class_name;
            if(!multMap.containsKey(r_key) && !multMap.containsKey(r_revKey)){
                multMap.put(r_key, r_val);
            }
        }else{
            r_val = "1-1";
            r_key = class_name + "~" + r_type;
            r_revKey = r_type + "~" + class_name;
            if(!multMap.containsKey(r_key) && !multMap.containsKey(r_revKey)){
                multMap.put(r_key, r_val);//<c-c, relation>
            }
        }
    }

    //Build umlUrl
    public void buildUrl(){
        //append uses part
        for(String key : useMap.keySet()) {
            umlURL.append("[" + useMap.get(key) + "]uses -.->[<<interface>>;" + key + "],");
        }
        //append depend part


        //append interfaces dependency
        for(String key : c_interMap.keySet()){
            List<String> list =c_interMap.get(key);
            for(String item : list){
                umlURL = umlURL.append("[<<interface>>;" + item + "]^-.-[" + key + "],");
            }
        }
        //append superclass dependency
        for(String key : c_superMap.keySet()){
            List<String> list =c_superMap.get(key);
            for(String item : list){
                umlURL = umlURL.append("[" + item + "]^-[" + key + "],");
            }
        }
        //append multiplicity relation
        for(String key: multMap.keySet()){
            //check if class/interface type
            String tmpClass = key.split("~")[0];
            String tmpType = key.split("~")[1];
            if(classList.contains(tmpType)){//??can also use interfacelist
                umlURL.append("[" + tmpClass + "]" + multMap.get(key) + "[" + tmpType + "],");
            }else{
                umlURL.append("[" + tmpClass + "]" + multMap.get(key) + "[<<interface>>;" + tmpType + "],");
            }
        }
        //remove last comma
        umlURL.setLength(umlURL.length() - 1);
    }

}
