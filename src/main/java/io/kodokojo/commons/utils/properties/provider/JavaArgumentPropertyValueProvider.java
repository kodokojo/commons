package io.kodokojo.commons.utils.properties.provider;

/*
 * #%L
 * docker-commons
 * %%
 * Copyright (C) 2016 Kodo-kojo
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

public class JavaArgumentPropertyValueProvider extends AbstarctStringPropertyValueProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaArgumentPropertyValueProvider.class);

    public static final String DEFAULT_PREFIX = "--";

    private final HashMap<String, String> cache;

    private final String prefixe;

    public JavaArgumentPropertyValueProvider(String prefixe, String[] args) {
        if (isBlank(prefixe)) {
            this.prefixe = DEFAULT_PREFIX;
        } else {
            this.prefixe = prefixe;
        }
        if (args == null) {
            throw new IllegalArgumentException(" must be defined.");
        }
        cache = new HashMap<>();
        String currentKey = null;
        for(String arg : args) {
            if (arg.startsWith(prefixe)) {
                if (StringUtils.isNotBlank(currentKey)) {
                    cache.put(currentKey,Boolean.TRUE.toString());
                }
                currentKey = arg.substring(prefixe.length());
            } else {
                if (StringUtils.isNotBlank(currentKey)) {
                    cache.put(currentKey, arg);
                    currentKey = null;
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("The value '{}' has no key, ignore it", arg);
                }
            }
        }
    }

    public JavaArgumentPropertyValueProvider(String[] args) {
        this(DEFAULT_PREFIX, args);
    }

    @Override
    protected String provideValue(String key) {
        if (isBlank(key)) {
            throw new IllegalArgumentException("key must be defined.");
        }
        return cache.get(key);
    }

    protected List<String> values() {
        return new ArrayList<>(cache.values());
    }
}