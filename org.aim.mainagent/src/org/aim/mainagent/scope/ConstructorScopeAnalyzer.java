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

import java.lang.reflect.Constructor;
import java.util.Set;

import org.aim.aiminterface.description.restriction.Restriction;
import org.aim.api.instrumentation.AbstractScopeAnalyzer;
import org.aim.api.instrumentation.description.internal.FlatScopeEntity;
import org.aim.mainagent.utils.Utils;
import org.lpe.common.util.LpeStringUtils;

/**
 * Scope analyzer for the constructors scope.
 * 
 * @author Alexander Wert
 * 
 */
public class ConstructorScopeAnalyzer extends AbstractScopeAnalyzer {

	private Restriction restriction;
	private final String[] classNames;

	/**
	 * Constructor.
	 * 
	 * @param classNames
	 *            a set of classes whose constructors to instrument
	 */
	public ConstructorScopeAnalyzer(String[] classNames) {
		this.classNames = classNames;
	}

	@Override
	public void visitClass(Class<?> clazz, Set<FlatScopeEntity> scopeEntities) {
		if (restriction.isExcluded(clazz.getName())) {
			return;
		}

		boolean found = false;
		for (String cName : classNames) {
			if (LpeStringUtils.patternMatches(clazz.getName(), cName)) {
				found = true;
				break;
			}
		}

		if (!found) {
			return;
		}

		for (Constructor<?> con : clazz.getDeclaredConstructors()) {
			if (!restriction.modifierSetExcluded(con.getModifiers())) {
				scopeEntities.add(new FlatScopeEntity(clazz, Utils.getMethodSignature(con, true)));
			}
		}

	}

	@Override
	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;

	}

}
