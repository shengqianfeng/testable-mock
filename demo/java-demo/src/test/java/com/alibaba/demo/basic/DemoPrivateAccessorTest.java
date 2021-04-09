package com.alibaba.demo.basic;

import com.alibaba.testable.core.tool.PrivateAccessor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 演示使用`PrivateAccessor`工具类访问私有成员
 * Demonstrate access private member via `PrivateAccessor` class
 */
class DemoPrivateAccessorTest {

    private DemoPrivateAccess demoPrivateAccess = new DemoPrivateAccess();

    @Test
    void should_access_private_method() {
        List<String> list = new ArrayList<String>() {{
            add("a");
            add("b");
            add("c");
        }};
        assertEquals("member", PrivateAccessor.invoke(demoPrivateAccess, "privateFunc"));
        assertEquals("abc + hello + 1", PrivateAccessor.invoke(demoPrivateAccess, "privateFuncWithArgs", list, "hello", 1));
    }

    @Test
    void should_access_private_field() {
        PrivateAccessor.set(demoPrivateAccess, "count", 3);
        assertEquals(Integer.valueOf(3), PrivateAccessor.get(demoPrivateAccess, "count"));
    }

    /**
     * 访问私有的(带参数的)静态方法
     */
    @Test
    void should_access_private_static_method() {
        assertEquals("static", PrivateAccessor.invokeStatic(DemoPrivateAccess.class, "privateStaticFunc"));
        assertEquals("hello + 1", PrivateAccessor.invokeStatic(DemoPrivateAccess.class, "privateStaticFuncWithArgs", "hello", 1));
    }

    /**
     * 给静态的私有属性赋值
     */
    @Test
    void should_access_private_static_field() {
        PrivateAccessor.setStatic(DemoPrivateAccess.class, "staticCount", 3);
        assertEquals(Integer.valueOf(3), PrivateAccessor.getStatic(DemoPrivateAccess.class, "staticCount"));
    }

    /**
     * 给final类型的变量赋值
     */
    @Test
    void should_update_final_field() {
        PrivateAccessor.set(demoPrivateAccess, "pi", 3.14);
        assertEquals(Double.valueOf(3.14), PrivateAccessor.get(demoPrivateAccess, "pi"));
    }

    @Test
    void should_use_null_parameter() {
        PrivateAccessor.set(demoPrivateAccess, "pi", null);
        assertNull(PrivateAccessor.get(demoPrivateAccess, "pi"));
        assertEquals("null + 1", PrivateAccessor.invokeStatic(DemoPrivateAccess.class, "privateStaticFuncWithArgs", null, 1));
    }

}
