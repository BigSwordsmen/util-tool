/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.support;

import com.google.common.base.Throwables;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 *
 * @author zhaoj
 * @version ResourceSupport.java, v 0.1 2019-03-13 16:44
 */
public class ResourceSupport {
    private static final ResourcePatternResolver RP_RESOLVER = new PathMatchingResourcePatternResolver();

    /**
     * 获取资源
     */
    public static Resource[] getResources(String... patterns) {
        Resource[] result = new Resource[]{};
        for (String pattern : patterns) {
            Resource[] resources = new Resource[0];
            try {
                resources = RP_RESOLVER.getResources(pattern);
            } catch (IOException e) {
                Throwables.propagateIfPossible(e);
            }
            if (resources != null && resources.length != 0) {
                result = ArrayUtils.addAll(result, resources);
            }
        }
        return result;
    }


    public static Resource getResource(String pattern) {
        return RP_RESOLVER.getResource(pattern);
    }

    public static Resource[] getResources(String pattern) throws IOException {
        return RP_RESOLVER.getResources(pattern);
    }
}
