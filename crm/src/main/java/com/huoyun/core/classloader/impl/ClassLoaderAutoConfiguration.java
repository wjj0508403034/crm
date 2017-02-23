package com.huoyun.core.classloader.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.huoyun.core.classloader.CachedClassLoader;

@Configuration
public class ClassLoaderAutoConfiguration {

	@Bean
	public CachedClassLoader cachedClassLoader(){
		return CachedClassLoaderImpl.instance();
	}
}
