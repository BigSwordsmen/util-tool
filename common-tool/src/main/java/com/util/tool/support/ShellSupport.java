/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.support;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zhaoj
 * @version ShellSupport.java, v 0.1 2019-03-13 16:46
 */
public class ShellSupport {
    static Logger logger = LoggerFactory.getLogger(ShellSupport.class);
    private static Joiner joiner = Joiner.on(" ").skipNulls();

    /**
     *
     * @param cwd shell工作目录
     * @param args 输入参数
     * @return 命令的 stdout
     * @throws Exception 如果命令 exit code不为0, 则抛出异常,错误信息是stderr或stdout
     */
    public static String shell(File cwd, int timeout, String cmd, String... args) throws Exception {
        String line = joiner.join(cmd, null, args);
        logger.info("shell cwd:{}, cmd:{}", cwd, line);
        Process process = Runtime.getRuntime().exec(line, null, cwd);
        if (process.waitFor(timeout, TimeUnit.SECONDS)) {
            String output;
            try (Reader reader = new InputStreamReader(process.getInputStream())) {
                output = CharStreams.toString(reader);
            }
            int code = process.exitValue();
            if (code != 0) {
                try (Reader reader = new InputStreamReader(process.getErrorStream())) {
                    String error = CharStreams.toString(reader);
                    if (Strings.isNullOrEmpty(error)) {
                        error = output;
                    }
                    throw new RuntimeException("exit code:" + code + "\nerror:" + error);
                }
            }
            return output;
        } else {
            process.destroy();
            throw new RuntimeException("`" + line + "` timeout seconds:" + timeout);
        }
    }


    public static void main(String... args) throws Exception {
        Stopwatch sw = Stopwatch.createStarted();
        // String code = shell("zip -x *.git* -qr", "/Users/scutfish/temp/test.zip",
        // "/Users/scutfish/workspace/alibaba/staragent/staragent-portal-api");
        File cwd = new File("/Users/scutfish/Desktop");
        String code = shell(cwd, 60, "zip -x *.git* -qr", "/Users/scutfish/temp/test.zip", ".");
        long elapsed = sw.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(code);
        System.out.println(elapsed);

    }
}
