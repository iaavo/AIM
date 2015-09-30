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
package org.aim.artifacts.scopes;

import org.aim.api.instrumentation.AbstractInstAPIScope;
import org.lpe.common.extension.IExtension;

/**
 * The {@link EntryPointScope} comprises all doXXX methods of classes derived
 * from javax.servlet.http.HttpServlet.
 * 
 * @author Alexander Wert
 * 
 */
public class EntryPointScope extends AbstractInstAPIScope {
	/**
	 * Constructor.
	 * 
	 * @param provider
	 *            extension provider
	 */
	public EntryPointScope(final IExtension provider) {
		super(provider);
	}

	@Override
	protected void init() {
		final String httpServletClassName = "javax.servlet.http.HttpServlet";
		for (final String action : new String[]{"Delete","Get","Head","Options","Post","Put","Trace"}) {
			addMethod(httpServletClassName,
					"do" + action + "(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)");			
		}
		addMethod(httpServletClassName,
				"service(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)");
		addMethod(httpServletClassName,
				"service(javax.servlet.ServletRequest,javax.servlet.ServletResponse)");
		addMethodAnnotationToMatch("javax.ws.rs.Path");
	}
	
	@Override
	public long getId() {
		return 0;
	}

}
