package com.dwarfeng.projwiz.core.model.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

/**
 * 插件类加载器。
 * <p>
 * 该接口是有 {@link URLClassLoader} 抽象而来，所有的方法请参考 {@link URLClassLoader}。
 * <p>
 * TODO 待完善doc。
 * 
 * @see URLClassLoader
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface PluginClassLoader extends Closeable {

	/**
	 * 
	 * @param name
	 * @return
	 */
	public InputStream getResourceAsStream(String name);

	/**
	 * 
	 * @return
	 */
	public URL[] getURLs();

	/**
	 * 
	 * @param name
	 * @return
	 */
	public URL findResource(String name);

	/**
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public Enumeration<URL> findResources(String name) throws IOException;

	/**
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<?> loadClass(String name) throws ClassNotFoundException;

	/**
	 * 
	 * @param name
	 * @return
	 */
	public URL getResource(String name);

	/**
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public Enumeration<URL> getResources(String name) throws IOException;

	/**
	 * 
	 * @param enabled
	 */
	public void setDefaultAssertionStatus(boolean enabled);

	/**
	 * 
	 * @param packageName
	 * @param enabled
	 */
	public void setPackageAssertionStatus(String packageName, boolean enabled);

	/**
	 * Sets the desired assertion status for the named top-level class in this
	 * class loader and any nested classes contained therein. This setting takes
	 * precedence over the class loader's default assertion status, and over any
	 * applicable per-package default. This method has no effect if the named
	 * class has already been initialized. (Once a class is initialized, its
	 * assertion status cannot change.)
	 *
	 * <p>
	 * If the named class is not a top-level class, this invocation will have no
	 * effect on the actual assertion status of any class.
	 * </p>
	 *
	 * @param className
	 *            The fully qualified class name of the top-level class whose
	 *            assertion status is to be set.
	 *
	 * @param enabled
	 *            <tt>true</tt> if the named class is to have assertions enabled
	 *            when (and if) it is initialized, <tt>false</tt> if the class
	 *            is to have assertions disabled.
	 *
	 * @see ClassLoader#setClassAssertionStatus(String, boolean)
	 */
	public void setClassAssertionStatus(String className, boolean enabled);

	/**
	 * Sets the default assertion status for this class loader to <tt>false</tt>
	 * and discards any package defaults or class assertion status settings
	 * associated with the class loader. This method is provided so that class
	 * loaders can be made to ignore any command line or persistent assertion
	 * status settings and "start with a clean slate."
	 * 
	 * @see ClassLoader#clearAssertionStatus()
	 */
	public void clearAssertionStatus();

	/**
	 * Appends the specified URL to the list of URLs to search for classes and
	 * resources.
	 * <p>
	 * If the URL specified is {@code null} or is already in the list of URLs,
	 * or if this loader is closed, then invoking this method has no effect.
	 *
	 * @param url
	 *            the URL to be added to the search path of URLs
	 */
	public void addURL(URL url);

}