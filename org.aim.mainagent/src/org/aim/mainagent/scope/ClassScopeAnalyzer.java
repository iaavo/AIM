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
package org.aim.mainagent.scope;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.aim.aiminterface.description.restriction.Restriction;
import org.aim.api.instrumentation.AbstractScopeAnalyzer;
import org.aim.api.instrumentation.description.internal.FlatScopeEntity;
import org.lpe.common.util.LpeStringUtils;

/**
 * Analyzes classes scope.
 * 
 * @author Alexander Wert
 * 
 */
public class ClassScopeAnalyzer extends AbstractScopeAnalyzer {

	private Restriction restriction;
	private final Set<String> classNames;

	/**
	 * Constructor.
	 * 
	 * @param classNames
	 *            a set of classes to instrument
	 */
	public ClassScopeAnalyzer(final Set<String> classNames) {
		this.classNames = classNames;
	}

	@Override
	public void visitClass(final Class<?> clazz, final Set<FlatScopeEntity> scopeEntities) {
		if (restriction.isExcluded(clazz.getName())) {
			return;
		}

		boolean found = false;
		for (final String cName : classNames) {
			if (LpeStringUtils.patternMatches(clazz.getName(), cName)) {
				found = true;
				break;
			}
		}

		if (!found) {
			return;
		}

		final Set<Method> methods = new HashSet<>();
		if (!restriction.hasModifierRestrictions() || !restriction.modifierSetExcluded(Modifier.PUBLIC)) {
			for (final Method m : clazz.getMethods()) {
				if (!restriction.isExcluded(m.getDeclaringClass().getName())) {
					methods.add(m);
				}

			}
		}
		for (final Method m : clazz.getDeclaredMethods()) {
			if (!restriction.modifierSetExcluded(m.getModifiers())) {
				methods.add(m);
			}
		}

		for (final Method method : methods) {
			scopeEntities.add(new FlatScopeEntity(method.getDeclaringClass(), getMethodSignature(method, true)));
		}
	}

	@Override
	public void setRestriction(final Restriction restriction) {
		this.restriction = restriction;

	}
}
