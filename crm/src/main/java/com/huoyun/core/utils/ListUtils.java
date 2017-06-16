package com.huoyun.core.utils;

import java.util.List;

public class ListUtils {

	public static <T> void removeDup(List<T> list) {
		int index = 0;
		while (index < list.size()) {
			T item = list.get(index);
			if (list.lastIndexOf(item) != list.indexOf(item)) {
				list.remove(index);
				continue;
			}

			index++;
		}
	}
}
