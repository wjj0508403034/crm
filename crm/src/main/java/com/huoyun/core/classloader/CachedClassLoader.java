package com.huoyun.core.classloader;

import java.util.Map;

import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.ExtensibleBusinessObject;

public interface CachedClassLoader {

	Map<String, Class<? extends BusinessObject>> getBoClassCache();
	
	Map<String, Class<? extends ExtensibleBusinessObject>> getExtBoClassCache();
}
