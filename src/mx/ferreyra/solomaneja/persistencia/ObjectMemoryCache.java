package mx.ferreyra.solomaneja.persistencia;

import java.lang.ref.SoftReference;
import java.util.HashMap;

class ObjectMemoryCache {

	private static HashMap<String, SoftReference<Object>> cache;

	static{
		cache=new HashMap<String, SoftReference<Object>>();
	}

	public static Object get(String id){
		if(!cache.containsKey(id))
			return null;
		SoftReference<Object> ref=cache.get(id);
		return ref.get();
	}

	public static void set(String id, Object obj){
		cache.put(id, new SoftReference<Object>(obj));
	}

	public static void clear() {
		cache.clear();
	}
}