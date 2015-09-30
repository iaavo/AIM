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
package org.aim.api.instrumentation;

import java.util.Collections;
import java.util.Set;

import org.lpe.common.config.ConfigParameterDescription;
import org.lpe.common.extension.IExtension;

/**
 * Abstract extension provider for probes.
 * 
 * @author Alexander Wert
 * 
 */
public abstract class AbstractEnclosingProbeExtension implements IExtension {

	@Override
	public String getName() {
		return getProbeClass().getName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<ConfigParameterDescription> getConfigParameters() {
		return Collections.EMPTY_SET;
	}

	/**
	 * Returns the class of the probe.
	 * 
	 * @return class of the probe
	 */
	public abstract Class<? extends AbstractEnclosingProbe> getProbeClass();

	/**
	 * Returns the type of the scope this probe is applicable to.
	 * 
	 * @return class of the scope
	 */
	public abstract Set<Class<?>> getScopeDependencies();
}
