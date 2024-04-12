package com.gsls.gt_databinding.route;

import com.google.auto.service.AutoService;
import com.gsls.gt_databinding.bean.BindingBean;
import com.gsls.gt_databinding.route.annotation.GT_Route;
import com.gsls.gt_databinding.utils.ClassType;
import com.gsls.gt_databinding.utils.DataBindingUtils;
import com.gsls.gt_databinding.utils.FileUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;


@AutoService(Processor.class)//编译时运行这个类
public class GT_Route_Main extends AbstractProcessor {

    private final List<BindingBean> bindingBeanList = new ArrayList<>();

    /**
     * 必须要的
     *
     * @return
     */
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(GT_Route.class.getCanonicalName());
        return types;
    }

    private Elements elementUtils;
    private ProcessingEnvironment processingEnv;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        this.processingEnv = processingEnv;
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(GT_Route.class);
        if (elementsAnnotatedWith.isEmpty()) return true;

        DataBindingUtils.log("GSLS_King:" + elementsAnnotatedWith.size());
        DataBindingUtils.log("roundEnv1" + roundEnv);
        DataBindingUtils.log("annotations:" + DataBindingUtils.toStrings(annotations));
        DataBindingUtils.log("roundEnv2:" + DataBindingUtils.toStrings(roundEnv));
        DataBindingUtils.init();

        int count = 0;
        bindingBeanList.clear();
        for (Element element : elementsAnnotatedWith) {
            count++;
            DataBindingUtils.log("element:" + element);
            DataBindingUtils.log("elementGet1:" + element.getEnclosedElements());
            DataBindingUtils.log("elementGet2:" + element.getSimpleName());
            DataBindingUtils.log("elementGet3:" + element.getKind());
            DataBindingUtils.log("elementGet4:" + element.getModifiers());
            DataBindingUtils.log("elementGet6:" + element.getEnclosingElement());
            DataBindingUtils.log("elementGet7:" + DataBindingUtils.toStrings(element.asType()));

            Element enclosingElement = element.getEnclosingElement();
            DataBindingUtils.log("elementGet8:" + DataBindingUtils.toStrings(enclosingElement.asType()));


            DataBindingUtils.log("elementGetAll:" + DataBindingUtils.toStrings(element));

            GT_Route annotation = element.getAnnotation(GT_Route.class);
            String path1 = annotation.value();
            String extras = annotation.extras();
            String[] interceptors = annotation.interceptors();

            DataBindingUtils.log("path1:" + path1);
            DataBindingUtils.log("extras:" + extras);

            BindingBean bindingBean = new BindingBean();
            bindingBean.setAnnotateValue(path1);
            DataBindingUtils.log("element.toString():" + element.toString());
            bindingBean.setPackClassPath(element.toString());
            bindingBean.setClassName(element.getSimpleName().toString());//获取类名
            bindingBean.setPackName(element.getEnclosingElement().toString());//设置包名
            bindingBean.setResourcePackName(DataBindingUtils.pageName(bindingBean.getPackName()));//设置包名

            //精准判断是 注解具体注解对象的 具体父类类型
            DataBindingUtils.log("start:" + element.asType());
            ClassType parse = ClassType.parse(element.asType().toString());
            DataBindingUtils.log("parse:" + parse);

            //判断对象类型
            if (elementUtils != null) {
                TypeMirror activityYM = elementUtils.getTypeElement(ClassType.ACTIVITY.getClassName()).asType();
                TypeMirror fragmentYM1 = elementUtils.getTypeElement(ClassType.FRAGMENT.getClassName()).asType();
                TypeMirror fragmentYM2 = elementUtils.getTypeElement(ClassType.FRAGMENT_X.getClassName()).asType();
                TypeMirror serviceYM = elementUtils.getTypeElement(ClassType.SERVICE.getClassName()).asType();
                TypeMirror providerYM = elementUtils.getTypeElement(ClassType.PROVIDER.getClassName()).asType();
                TypeMirror interceptor = elementUtils.getTypeElement(ClassType.INTERCEPTOR.getClassName()).asType();
//                TypeMirror content_providerYM = elementUtils.getTypeElement(ClassType.CONTENT_PROVIDER.getClassName()).asType();

                Types types = processingEnv.getTypeUtils();
                DataBindingUtils.log("getTypeUtils:" + types);
                TypeMirror typeMirror = element.asType();
                DataBindingUtils.log("typeMirror:" + typeMirror);
                if (types.isSubtype(typeMirror, activityYM)) {
                    bindingBean.setClassType(ClassType.ACTIVITY);
                } else if (types.isSubtype(typeMirror, fragmentYM1)) {
                    bindingBean.setClassType(ClassType.FRAGMENT);
                } else if (types.isSubtype(typeMirror, fragmentYM2)) {
                    bindingBean.setClassType(ClassType.FRAGMENT_X);
                } else if (types.isSubtype(typeMirror, serviceYM)) {
                    bindingBean.setClassType(ClassType.SERVICE);
                } else if (types.isSubtype(typeMirror, interceptor)) {
                    bindingBean.setClassType(ClassType.INTERCEPTOR);
                } else if (types.isSubtype(typeMirror, providerYM)) {
                    bindingBean.setClassType(ClassType.PROVIDER);
                } else {
                    bindingBean.setClassType(ClassType.UNKNOWN);
                }
            }


            //获取jar包完整路径
            String path = getClass().getResource("").getPath();
            DataBindingUtils.log("path1:" + path);

            //获取当前项目名称
            String projectName = System.getProperty("user.dir");
            DataBindingUtils.log("projectName:" + projectName);
            DataBindingUtils.androidBean.setProjectPath(projectName);

            //获取项目中所有模块
            List<String> filesAllName = FileUtils.getFilesAllName(DataBindingUtils.androidBean.getProjectPath());
            DataBindingUtils.log("filesAllName:" + filesAllName);


            assert filesAllName != null;
            for (String filePath : filesAllName) {
                String[] split = filePath.split("\\\\");
                String fileName = split[split.length - 1];
                if (FileUtils.fileIsDirectory(filePath) && DataBindingUtils.filtrationArray.contains(fileName)) {
                    DataBindingUtils.log("FileDir:" + filePath);
                    split = filePath.split("\\\\");
                    DataBindingUtils.androidBean.addJavaLibraryName(split[split.length - 1]);
                }
            }

            for (String libraryName : DataBindingUtils.androidBean.getJavaLibraryNames()) {
                DataBindingUtils.log("libraryName:" + libraryName);

                String classPath = DataBindingUtils.androidBean.getProjectPath() + "\\" + libraryName + "\\src\\main\\java\\" + bindingBean.getPackClassPath().replaceAll("\\.", "\\\\") + ".java";
                String classPath2 = DataBindingUtils.androidBean.getProjectPath() + "\\" + libraryName + "\\src\\main\\java\\" + bindingBean.getPackClassPath().replaceAll("\\.", "\\\\") + ".kt";

                //Java
                DataBindingUtils.log("classPath:" + classPath);
                //Kotlin
                DataBindingUtils.log("classPath2:" + classPath2);

                //Java
                if (FileUtils.fileExist(classPath)) {
                    DataBindingUtils.log("Yes1:" + classPath);
                    bindingBean.setJavaLibraryName(libraryName);
                    bindingBean.setClassPath(classPath);
                    String query = FileUtils.query(bindingBean.getClassPath());
                    DataBindingUtils.log("query1:" + query);
                    bindingBean.setClassCode(query);//设置源码
                    DataBindingUtils.log("query11:" + query);
                    break;
                }

                //Kotlin
                if (FileUtils.fileExist(classPath2)) {
                    DataBindingUtils.log("Yes2:" + classPath2);
                    bindingBean.setJavaLibraryName(libraryName);
                    bindingBean.setClassPath(classPath2);
                    String query = FileUtils.query(bindingBean.getClassPath());
                    DataBindingUtils.log("query2:" + query);
                    bindingBean.setClassCode(query);//设置包名
                    DataBindingUtils.log("query22:" + query);
                    break;
                }

            }

            DataBindingUtils.log("bindingBean1:" + bindingBean);
            DataBindingUtils.log("close tag");
            bindingBean.setExtras(extras + " ↓");
            bindingBean.setInterceptors(interceptors);
            bindingBeanList.add(bindingBean);

            if (count < elementsAnnotatedWith.size()) continue;//是解析最后一个 路由才进行生成数据

            //生成包名
            StringBuilder builder = new StringBuilder();
            builder.append("package " + bindingBean.getPackName() + ";\n\n");
            builder.append("import java.util.Map;\n");
            builder.append("import java.util.HashMap;\n");
            builder.append("import com.gsls.gt.GT.ARouter.IProvider;\n");
            builder.append("import com.gsls.gt_databinding.route.annotation.GT_ARouterName;\n");
            builder.append("import com.gsls.gt.GT;\n");

            //引入注解类路径 packClassPath
            for (BindingBean bean : bindingBeanList) {
                builder.append("import " + bean.getPackClassPath() + ";\n");
            }

            builder.append("import com.gsls.gt_databinding.route.GT_RouteMeta;\n");
            builder.append("import com.gsls.gt_databinding.utils.ClassType;\n");
            builder.append("\n");//导入的包与逻辑代码换行

            //添加文件注解
            builder.append("/**\n" +
                    " * This class is automatically generated and cannot be modified\n" +
                    " * GT-DataBinding class, inherited\n" +
                    " */");

            DataBindingUtils.log("start");

            StringBuilder aRouterName = new StringBuilder();
            //获取到所有 Library 的项目包名,并解析成为路由反射路径
            for (int arIndex = 0; arIndex < DataBindingUtils.androidBean.getJavaLibraryNames().size(); arIndex++) {
                String javaLibraryName = DataBindingUtils.androidBean.getJavaLibraryNames().get(arIndex);
                String libraryPath = DataBindingUtils.androidBean.getProjectPath() + "\\" + javaLibraryName + "\\build.gradle";
                String query = FileUtils.query(libraryPath);
                int namespaceIndex = query.indexOf("namespace");
                int comIndex = query.indexOf("com", namespaceIndex);
                int dotIndex = query.indexOf("'", comIndex);
                if (namespaceIndex != -1 && dotIndex != -1 && namespaceIndex < dotIndex) {
                    String modelPackName = query.substring(comIndex, dotIndex);
                    String className = modelPackName + ".ARouter$$" + javaLibraryName;//com.example.myapplication.ARouter$$app
                    if (arIndex == DataBindingUtils.androidBean.getJavaLibraryNames().size() - 1) {
                        aRouterName.append("\"" + className + "\"");
                    } else {
                        aRouterName.append("\"" + className + "\"" + ",");
                    }

                }
            }
            DataBindingUtils.log("ARouterName:" + aRouterName);


            //@GT_ARouterName("ARouter$$app")
            String className = "ARouter$$" + bindingBean.getJavaLibraryName();
            //根据不同的绑定类型 进行智能继承
            builder.append("\n@GT_ARouterName({" + aRouterName + "})");
            builder.append("\npublic class " + className + "{\n\n");
            builder.append("\tpublic Map<String, GT_RouteMeta> loadInto() {\n");

            builder.append("\t\tMap<String, GT_RouteMeta> atlas = new HashMap<>();\n");


            for (BindingBean bean : bindingBeanList) {
                String classType = bean.getClassType().toString();
                DataBindingUtils.log("classType:" + classType);
                switch (bean.getClassType()) {
                    case ACTIVITY:
                        classType = "ClassType.ACTIVITY";
                        break;
                    case FRAGMENT:
                        classType = "ClassType.FRAGMENT";
                        break;
                    case FRAGMENT_X:
                        classType = "ClassType.FRAGMENT_X";
                        break;
                    case SERVICE:
                        classType = "ClassType.SERVICE";
                        break;
                    case INTERCEPTOR:
                        classType = "ClassType.INTERCEPTOR";
                        break;
                    case PROVIDER:
                        classType = "ClassType.PROVIDER";
                        break;
                    case CONTENT_PROVIDER:
                        classType = "ClassType.CONTENT_PROVIDER";
                        break;
                    case UNKNOWN:
                        classType = "ClassType.UNKNOWN";
                        break;
                }

                builder.append("\t\t//" + bean.getExtras() + "\n");

                //解析拦截器
                StringBuilder interceptorStr = null;
                String[] interceptors1 = bean.getInterceptors();
                for(int interceptorsIndex = 0; interceptorsIndex < interceptors1.length; interceptorsIndex++){
                    if(interceptorStr == null){
                        interceptorStr = new StringBuilder("new String[]{");
                    }
                    interceptorStr.append("\"" + interceptors1[interceptorsIndex] + "\"");

                    if(interceptorsIndex != interceptors1.length - 1){
                        interceptorStr.append(", ");
                    }else{
                        interceptorStr.append("}");
                    }

                }

                //核心
                builder.append("\t\tatlas.put(\"" +
                        bean.getAnnotateValue() + "\", GT_RouteMeta.build(" +
                        classType + ", " +
                        bean.getClassName() + ".class, \"" +
                        bean.getAnnotateValue() + "\", \"" +
                        bean.getJavaLibraryName() + "\",  \"" +
                        bean.getPackClassPath() + "\",  " +
                        interceptorStr + ")" +
                        ");\n");

            }

            builder.append("\t\treturn atlas;\n");
            builder.append("\t}");

            builder.append("\n\n}\n"); // close class

            //生成最终添加好的代码
            try {
                JavaFileObject source = processingEnv.getFiler().createSourceFile(bindingBean.getPackName() + "." + className);
                Writer writer = source.openWriter();
                writer.write(builder.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                DataBindingUtils.log("Automatic code generation failed:" + e);
            }
        }
        return true;
    }

}