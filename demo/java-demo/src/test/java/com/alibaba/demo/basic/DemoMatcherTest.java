package com.alibaba.demo.basic;

import com.alibaba.demo.basic.model.mock.BlackBox;
import com.alibaba.testable.core.annotation.MockMethod;
import com.alibaba.testable.core.error.VerifyFailedError;
import org.junit.jupiter.api.Test;

import static com.alibaba.testable.core.matcher.InvokeMatcher.*;
import static com.alibaba.testable.core.matcher.InvokeVerifier.verify;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * 演示Mock方法调用校验器
 * Demonstrate mock method invocation verifier
 */
class DemoMatcherTest {

    private DemoMatcher demoMatcher = new DemoMatcher();

    public static class Mock {
        @MockMethod(targetMethod = "methodToBeMocked")
        private void methodWithoutArgument(DemoMatcher self) {
        }

        @MockMethod(targetMethod = "methodToBeMocked")
        private void methodWithArguments(DemoMatcher self, Object a1, Object a2) {
        }

        @MockMethod(targetMethod = "methodToBeMocked")
        private void methodWithArrayArgument(DemoMatcher self, Object[] a) {
        }
    }

    /**
     * 校验目标Mock被执行的次数
     */
    @Test
    void should_match_no_argument() {
        demoMatcher.callMethodWithoutArgument();
        verify("methodWithoutArgument").withTimes(1);
        demoMatcher.callMethodWithoutArgument();
        verify("methodWithoutArgument").withTimes(2);
    }

    /**
     * with(Object... args) → 验证方法是否被指定参数调用过
     * without(Object... args) → 验证方法从未被使用指定参数调用过
     * withInOrder(Object... args) → 如果指定方法被调用了多次，依据实际调用顺序依次匹配
     */
    @Test
    void should_match_number_arguments() {
        demoMatcher.callMethodWithNumberArguments();
        //从未被(String,2)入参调用过
        verify("methodWithArguments").without(anyString(), 2);
        verify("methodWithArguments").withInOrder(anyInt(), 2);
        verify("methodWithArguments").withInOrder(anyLong(), anyNumber());
        //验证被入参<1.0,Map<String,Float>>调用过
        verify("methodWithArguments").with(1.0, anyMapOf(Integer.class, Float.class));
        verify("methodWithArguments").with(anyList(), anySetOf(Float.class));
        verify("methodWithArguments").with(anyList(), anyListOf(Float.class));
        verify("methodWithArrayArgument").with(anyArrayOf(Long.class));
        verify("methodWithArrayArgument").with(anyArray());
    }

    @Test
    void should_match_string_arguments() {
        demoMatcher.callMethodWithStringArgument();
        verify("methodWithArguments").with(startsWith("he"), endsWith("ld"));
        verify("methodWithArguments").with(contains("stab"), matches("m.[cd]k"));
        verify("methodWithArrayArgument").with(anyArrayOf(String.class));
    }

    @Test
    void should_match_object_arguments() {
        demoMatcher.callMethodWithObjectArgument();
        verify("methodWithArguments").withInOrder(any(BlackBox.class), any(BlackBox.class));
        verify("methodWithArguments").withInOrder(nullable(BlackBox.class), nullable(BlackBox.class));
        verify("methodWithArguments").withInOrder(isNull(), notNull());
    }

    @Test
    void should_match_with_times() {
        demoMatcher.callMethodWithNumberArguments();
        verify("methodWithArguments").with(anyNumber(), any()).times(3);

        demoMatcher.callMethodWithNumberArguments();
        boolean gotError = false;
        try {
            verify("methodWithArguments").with(anyNumber(), any()).times(4);
        } catch (VerifyFailedError e) {
            gotError = true;
        }
        if (!gotError) {
            fail();
        }
    }

}
