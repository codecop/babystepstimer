package net.davidtanzer.babysteps;

import org.approvaltests.writers.ApprovalTextWriter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JavaBeanApprovalWriter extends ApprovalTextWriter {

    public JavaBeanApprovalWriter(Object c) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        super(extractData(c), ".txt");
    }

    private static String extractData(Object c) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (c == null) {
            return "null";
        }

        BeanInfo beanInfo = Introspector.getBeanInfo(c.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        if (propertyDescriptors.length == 0) {
            return c.toString();
        }

        StringBuilder data = new StringBuilder();
        data.append("{");
        data.append(c.getClass().getName());
        data.append("\n");

        for (PropertyDescriptor p : propertyDescriptors) {
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
