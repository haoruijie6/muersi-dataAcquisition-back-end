package com.data_conllection.common.opc.opc_da;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class TestConnect {
    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("demo.py");
        // 调用demo.py中的method1方法
        PyFunction func = interpreter.get("method1",PyFunction.class);
        Integer a = 10;
        Integer b = 10;
        PyObject pyobj = func.__call__(new PyInteger(a), new PyInteger(b));
        System.out.println("获得方法的返回值 = " + pyobj.toString());
    }
}

