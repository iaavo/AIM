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

import java.util.Collections;
import java.util.Set;

import org.aim.aiminterface.description.restriction.Restriction;
import org.aim.aiminterface.exceptions.InstrumentationException;
import org.aim.api.instrumentation.AbstractEnclosingProbe;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

public class InstrumentorAccessor {
	public static void instrumentBehaviour(final Set<Class<? extends AbstractEnclosingProbe>> probeTypes, final CtClass ctClass,
			final String behaviourSignature) throws InstrumentationException, CannotCompileException, NotFoundException {
		BCInjector.getInstance().instrumentBehaviour(probeTypes, ctClass, behaviourSignature, Collections.<Long> emptySet(),
				Restriction.EMPTY_RESTRICTION);
	}
}
