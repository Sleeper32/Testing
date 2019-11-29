import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        start(TestClass.class);
    }

    public static void start(Class testClass) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Object tests = testClass.newInstance();

        Method[] methods = testClass.getDeclaredMethods();

        Map<Integer, List<Method>> methodMap = new TreeMap();
        Method beforeSuit = null;
        Method afterSuit = null;

        for (Method m : methods) {

            Annotation[] annotations = m.getDeclaredAnnotations();
            for (Annotation a : annotations) {
                if (annotations.length == 0) {
                    continue;
                }

                int priority = 0;
                switch (a.annotationType().getName()) {
                    case "Test":
                        priority = ((Test) a).priority();
                        break;
                    case "BeforeSuite":
                        if (beforeSuit != null) {
                            throw new RuntimeException("Too much BeforeSuite");
                        }
                        beforeSuit = m;
                        continue;
                    case "AfterSuite":
                        if (afterSuit != null) {
                            throw new RuntimeException("Too much AfterSuite");
                        }
                        afterSuit = m;
                        continue;
                    default:
                        priority = 0;
                        break;
                }

                List<Method> methodList = methodMap.get(priority);
                if (methodList == null) {
                    methodList = new ArrayList<>();
                    methodList.add(m);
                    methodMap.put(priority, new ArrayList<Method>());
                }
                else {
                    methodList.add(m);
                }
                methodMap.put(priority, methodList);
            }
        }

        beforeSuit.invoke(tests);
        for (SortedMap.Entry<Integer, List<Method>> item  : methodMap.entrySet()) {
            for (Method method : item.getValue()) {
                method.invoke(tests);
            }
        }
        afterSuit.invoke(tests);
    }
}
