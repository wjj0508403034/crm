package com.huoyun.upgrade.core.builder;

import com.huoyun.upgrade.core.TableDefinition;
import com.huoyun.upgrade.core.UniqueDefinition;

public interface ScriptBuilder {

	String dropTable(TableDefinition table);
	
	String createTable(TableDefinition table);
	
	String tableExists(TableDefinition table);

	String addUnique(UniqueDefinition unique);
}
