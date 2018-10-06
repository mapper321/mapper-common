import com.mapper.core.util.BeanUtils;

/**
 * Ognl工具类，主要是为了在ognl表达式访问静态方法时可以减少长长的类名称编写 Ognl访问静态方法的表达式为:
 * @class@method(args) 示例使用:
 * 
 * <pre>
 * 	&lt;if test=&quot;@Ognl@isNotEmpty(userId)&quot;&gt;
 * 	and user_id = #{userId}
 * &lt;/if&gt;
 * </pre>
 * 
 * @author badqiu
 */
public class Ognl
{
	/**
	 * 可以用于判断 Map,Collection,String,Array,Long是否为空
	 * @param o java.lang.Object.
	 * @return boolean.
	 */
	public static boolean isEmpty(Object o) throws IllegalArgumentException
	{
		return BeanUtils.isEmpty(o);
	}

	public static boolean isEqual(Object o1,Object o2)
	{
		if(o1 == null)
			return false;
		else
			return o1.toString().equals(o2.toString());
	}
	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object o)
	{
		return !isEmpty(o);
	}
	
	public static boolean isNotEmpty(Long o)
	{
		return !isEmpty(o);
	}
	

	
	/**
	 * 判断是否为数字
	 * @param o
	 * @return
	 */
	public static boolean isNumber(Object o)
	{
		return BeanUtils.isNumber(o);
	}

	
}
