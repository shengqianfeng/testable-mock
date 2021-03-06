package com.alibaba.testable.agent.config;

import com.alibaba.testable.agent.util.GlobalConfig;
import com.alibaba.testable.agent.util.PathUtil;
import com.alibaba.testable.core.model.MockScope;
import com.alibaba.testable.core.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import static com.alibaba.testable.agent.constant.ConstPool.PROPERTY_USER_DIR;

public class PropertiesParser {

    private static final String DEFAULT_CONFIG_FILE = "src/test/resources/testable.properties";
    private static final String LOG_LEVEL = "log.level";
    private static final String LOG_FILE = "log.file";
    private static final String DUMP_PATH = "dump.path";
    private static final String PKG_PREFIX_WHITELIST = "custom.pkgPrefix.whiteList";
    private static final String DEFAULT_MOCK_SCOPE = "mock.scope.default";
    private static final String ENABLE_THREAD_POOL = "thread.pool.enhance.enable";
    private static final String ENABLE_OMNI_INJECT = "omni.constructor.enhance.enable";

    public void parseFile(String configFilePath) {
        String path = (configFilePath == null) ? DEFAULT_CONFIG_FILE : configFilePath;
        String fullPath = PathUtil.join(System.getProperty(PROPERTY_USER_DIR), path);
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(fullPath));
            pps.load(in);
            LogUtil.verbose("Loaded configure file %s", fullPath);
        } catch (IOException e) {
            if (configFilePath == null) {
                LogUtil.verbose("No configure file found, skip.");
            } else {
                LogUtil.warn("No configure file found at %s", fullPath);
            }
            return;
        }
        parsePropertiesContent(pps);
    }

    private void parsePropertiesContent(Properties pps) {
        Enumeration<?> en = pps.propertyNames();
        while(en.hasMoreElements()) {
            String k = (String)en.nextElement();
            String v = pps.getProperty(k);
            if (k.equals(LOG_LEVEL)) {
                GlobalConfig.setLogLevel(v);
            } else if (k.equals(LOG_FILE)) {
                GlobalConfig.setLogFile(v);
            } else if (k.equals(DUMP_PATH)) {
                GlobalConfig.setDumpPath(v);
            } else if (k.equals(PKG_PREFIX_WHITELIST)) {
                GlobalConfig.setPkgPrefix(v);
            } else if (k.equals(DEFAULT_MOCK_SCOPE)) {
                GlobalConfig.setDefaultMockScope(MockScope.of(v));
            } else if (k.equals(ENABLE_THREAD_POOL)) {
                GlobalConfig.setEnhanceThreadLocal(Boolean.parseBoolean(v));
            } else if (k.equals(ENABLE_OMNI_INJECT)) {
                GlobalConfig.setEnhanceOmniConstructor(Boolean.parseBoolean(v));
            }
        }
    }

}
