/*
 * Copyright 2013, The Sporting Exchange Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.betfair.cougar.core.impl.logging;

import com.betfair.cougar.logging.CougarLoggingUtils;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.xpath.internal.jaxp.JAXPVariableStack;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.impl.Log4jLoggerAdapter;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;


/**
 * Log4j file to programmatically set log levels for log4j. Most useful feature is that this is exposed via MBEAN
 */
// todo: logging: perhaps we should stop using jdk levels as our interface?
@ManagedResource
public class Log4jLoggingControl extends AbstractLoggingControl {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractLoggingControl.class);

    public void setLogLevel(String loggerName, String level, boolean recursive) {
        //This implementation does not support recursive loglevel changes
        setLogLevel(loggerName, level);
    }

    @ManagedOperation(description = "Sets the level of logger (p1) to level (p2)")
    public void setLogLevel(String loggerName, String level) {
        Logger l = loggerName == null? Logger.getRootLogger() : Logger.getLogger(loggerName);

        logger.info("Logger %s: level customised to %s", l.getName(), level);

        l.setLevel(convertJdkLevelToLog4jLevel(level));
    }

    @ManagedOperation
    public String getLogLevel(String loggerName) {
        Logger l = loggerName == null? Logger.getRootLogger() : Logger.getLogger(loggerName);

        return convertLog4jLevelToJdkLevel(l.getLevel());
    }

    public static Level convertJdkLevelToLog4jLevel(String inputLevel) {
        java.util.logging.Level jdkLogLevel = java.util.logging.Level.parse(inputLevel);

        if (java.util.logging.Level.ALL.equals(jdkLogLevel)) {
            return Level.ALL;
        } else if (java.util.logging.Level.FINE.equals(jdkLogLevel) || java.util.logging.Level.FINER.equals(jdkLogLevel)) {
            //No log4j level exists to differentiate between these levels
            return Level.DEBUG;
        } else if (java.util.logging.Level.FINEST.equals(jdkLogLevel)) {
            return Level.TRACE;
        } else if (java.util.logging.Level.INFO.equals(jdkLogLevel) || java.util.logging.Level.CONFIG.equals(jdkLogLevel)) {
            //Ditto for config
            return Level.INFO;
        } else if (java.util.logging.Level.WARNING.equals(jdkLogLevel)) {
            return Level.WARN;
        } else if (java.util.logging.Level.SEVERE.equals(jdkLogLevel)) {
            return Level.ERROR;
        } else if (java.util.logging.Level.OFF.equals(jdkLogLevel)) {
            return Level.OFF;
        }
        throw new IllegalArgumentException("Unable to find a match for level: " + inputLevel);
    }

    public static String convertLog4jLevelToJdkLevel(Level inputLevel) {

        switch (inputLevel.toInt()) {
            case Level.ALL_INT:
                return java.util.logging.Level.ALL.getName();
            case Level.DEBUG_INT:
                return java.util.logging.Level.FINE.getName();
            case Level.TRACE_INT:
                return java.util.logging.Level.FINEST.getName();
            case Level.INFO_INT:
                return java.util.logging.Level.INFO.getName();
            case Level.WARN_INT:
                return java.util.logging.Level.WARNING.getName();
            case Level.ERROR_INT:
                return java.util.logging.Level.SEVERE.getName();
            case Level.OFF_INT:
                return java.util.logging.Level.OFF.getName();
        }
        throw new IllegalArgumentException("Unable to find a match for level: " + inputLevel);
    }


}
