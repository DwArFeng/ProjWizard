package com.dwarfeng.projwiz.core.model.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Enumeration;

public final class DefaultPluginClassLoader implements PluginClassLoader {

	private final InnerUrlClassLoader delegate;

	public DefaultPluginClassLoader() {
		delegate = new InnerUrlClassLoader(new URL[0], DefaultPluginClassLoader.class.getClassLoader());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getResourceAsStream(String name) {
		return delegate.getResourceAsStream(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return delegate.toString();
	}

	/**
	 * @throws IOException
	 * @see java.net.URLClassLoader#close()
	 */
	@Override
	public void close() throws IOException {
		delegate.close();
	}

	/**
	 * @return
	 * @see java.net.URLClassLoader#getURLs()
	 */
	@Override
	public URL[] getURLs() {
		return delegate.getURLs();
	}

	/**
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 * @see java.lang.ClassLoader#loadClass(java.lang.String)
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return delegate.loadClass(name);
	}

	/**
	 * @param name
	 * @return
	 * @see java.net.URLClassLoader#findResource(java.lang.String)
	 */
	@Override
	public URL findResource(String name) {
		return delegate.findResource(name);
	}

	/**
	 * @param name
	 * @return
	 * @throws IOException
	 * @see java.net.URLClassLoader#findResources(java.lang.String)
	 */
	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		return delegate.findResources(name);
	}

	/**
	 * @param name
	 * @return
	 * @see java.lang.ClassLoader#getResource(java.lang.String)
	 */
	@Override
	public URL getResource(String name) {
		return delegate.getResource(name);
	}

	/**
	 * @param name
	 * @return
	 * @throws IOException
	 * @see java.lang.ClassLoader#getResources(java.lang.String)
	 */
	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return delegate.getResources(name);
	}

	/**
	 * @param enabled
	 * @see java.lang.ClassLoader#setDefaultAssertionStatus(boolean)
	 */
	@Override
	public void setDefaultAssertionStatus(boolean enabled) {
		delegate.setDefaultAssertionStatus(enabled);
	}

	/**
	 * @param packageName
	 * @param enabled
	 * @see java.lang.ClassLoader#setPackageAssertionStatus(java.lang.String,
	 *      boolean)
	 */
	@Override
	public void setPackageAssertionStatus(String packageName, boolean enabled) {
		delegate.setPackageAssertionStatus(packageName, enabled);
	}

	/**
	 * @param className
	 * @param enabled
	 * @see java.lang.ClassLoader#setClassAssertionStatus(java.lang.String,
	 *      boolean)
	 */
	@Override
	public void setClassAssertionStatus(String className, boolean enabled) {
		delegate.setClassAssertionStatus(className, enabled);
	}

	/**
	 * 
	 * @see java.lang.ClassLoader#clearAssertionStatus()
	 */
	@Override
	public void clearAssertionStatus() {
		delegate.clearAssertionStatus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addURL(URL url) {
		delegate.addURL(url);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	private static class InnerUrlClassLoader extends URLClassLoader {

		public InnerUrlClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
			super(urls, parent, factory);
		}

		public InnerUrlClassLoader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		public InnerUrlClassLoader(URL[] urls) {
			super(urls);
		}

		@Override
		public void addURL(URL url) {
			super.addURL(url);
		}
	}

}
