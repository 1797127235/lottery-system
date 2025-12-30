package org.lotterysystem.common.utils;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

public class LogTest {
    private final static Logger LOGGER = Logger.getLogger(LogTest.class.getName());

    @Test
    void LogTest(){
        System.out.println("hello world");
        LOGGER.info("LogTest");
    }

}
