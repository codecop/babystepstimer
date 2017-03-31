package net.davidtanzer.babysteps;

import org.approvaltests.Approvals;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class JavaBeanApprovalWriterTest {

    String extractNoExceptions(Object o) {
        try {
            return JavaBeanApprovalWriter.extractData(o);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Test
    public void should_report_primitives() throws Exception {
        Approvals.verifyAll("primitive", Arrays.<Object>asList(3, "3", 1.0), this::extractNoExceptions);
    }

    @Test
    public void should_report_arrays() throws Exception {
        Approvals.verify(this.extractNoExceptions(new String[]{"xax", "xbx"}));
    }

}