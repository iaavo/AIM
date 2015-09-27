/**
 * Copyright 2014 SAP AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aim.mainagent.instrumentor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.aim.aiminterface.exceptions.InstrumentationException;

/**
 * Singleton wrapper around Java instrumentation instance.
 * 
 * @author Alexander Wert
 * 
 */
public final class JInstrumentation {
	public static final String J_INSTRUMENTATION_KEY = "jinstrumentation";
	private static JInstrumentation instance;

	/**
	 * Returns singleton instance.
	 * 
	 * @return singleton
	 */
	public static synchronized JInstrumentation getInstance() {
		if (instance == null) {
			instance = new JInstrumentation();
		}
		return instance;
	}

	private Instrumentation jInstrumentation;

	private JInstrumentation() {

	}

	/**
	 * @return the jInstrumentation
	 * @throws InstrumentationException
	 *             if jInstrumentation has not been set yet
	 */
	public Instrumentation getjInstrumentation() throws InstrumentationException {
		if (jInstrumentation == null) {
			throw new InstrumentationException("Java instrumentation instance has not been set, yet!");
		}
		return jInstrumentation;
	}

	/**
	 * @param jInstrumentation
	 *            the jInstrumentation to set
	 */
	public void setjInstrumentation(final Instrumentation jInstrumentation) {
		this.jInstrumentation = jInstrumentation;
		System.getProperties().put(J_INSTRUMENTATION_KEY, jInstrumentation);
		jInstrumentation.addTransformer(new ClassFileTransformer() {
			
			@Override
			public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined,
					final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {
				System.out.println(formatCL(loader) + " loaded/modified "+className.replace("/","."));
				return null;
			}

			private String formatCL(ClassLoader loader) {
				String result = "";
				while (loader != null) {
					result = loader.getClass().getSimpleName() + ":" + result;
					loader = loader.getParent();
				}
				return "SystemLoader:" + result.substring(0, result.length()-1);
			}
		}, true);
	}

	/**
	 * Returns classes by their name.
	 * 
	 * @param className
	 *            name of the classes to search for
	 * @return list of classes matching the name
	 * @throws InstrumentationException
	 *             if the instrumentation agent is null
	 */
	public List<Class<?>> getClassesByName(final String className) throws InstrumentationException {
		if (jInstrumentation == null) {
			throw new InstrumentationException("Java instrumentation instance has not been set, yet!");
		}

		// TODO: maybe a cache is useful / reasonable to reduce instrumentation
		// time???
		final List<Class<?>> classes = new ArrayList<>();
		for (final Class<?> clazz : jInstrumentation.getAllLoadedClasses()) {
			if (clazz.getName().equals(className)) {
				classes.add(clazz);
			}
		}
		return classes;

	}
}
