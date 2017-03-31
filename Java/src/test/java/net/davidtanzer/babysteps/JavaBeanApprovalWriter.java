package net.davidtanzer.babysteps;

import org.approvaltests.writers.ApprovalTextWriter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JavaBeanApprovalWriter extends ApprovalTextWriter {

    public JavaBeanApprovalWriter(Object c) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        super(extractData(c), "txt");
        visited.clear();
    }

    private static final Set<Object> visited = new HashSet<>();

    static String extractData(Object c) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (c == null) {
            return "null";
        }
        if (c.getClass().isPrimitive()) {
            return c.toString();
        }

        BeanInfo beanInfo = Introspector.getBeanInfo(c.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        StringBuilder data = new StringBuilder();
        data.append("{");
        data.append(c.getClass().getName());

        if (c.getClass().isArray()) {
            if (!c.getClass().getComponentType().isPrimitive()) {

                if (visited.contains(c)) {
                    data.append("<already visited>");
                    data.append("}");
                    return data.toString();
                }
                visited.add(c);

                Object[] ca = (Object[]) c;
                for (int i = 0; i < ca.length; i++) {
                    data.append(i);
                    data.append(" : ");
                    Object invoke = ca[i];
                    data.append(extractData(invoke));
                    data.append("\n");
                }
            }
            // ignore primitive arrays
        }
        else {
            data.append(" : ");
            data.append(c.toString());
        }
        data.append("\n");

        // cycles and non java core classes
        if (visited.contains(c) || !c.getClass().getName().startsWith("java") ) {
            //data.append(System.identityHashCode(c));
            data.append("}");
            return data.toString();
        }
        visited.add(c);

        for (PropertyDescriptor p : propertyDescriptors) {
            if (Arrays.asList("class", "bytes").contains(p.getName())) {
                // do not go to class
                continue;
            }
            Method readMethod = p.getReadMethod();
            if (readMethod == null)
                continue;
            data.append(p.getName());
            data.append(" : ");
            Object invoke = readMethod.invoke(c);
            data.append(extractData(invoke));
            data.append("\n");
        }


        data.append("}");
        return data.toString();
    }

}
