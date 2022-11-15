package com.xjhqre.search.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * SearchConfig
 * </p>
 *
 * @author xjhqre
 * @since 11月 14, 2022
 */
@Configuration
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class SearchConfig {}
