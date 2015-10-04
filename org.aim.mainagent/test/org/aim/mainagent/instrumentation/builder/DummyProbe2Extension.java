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
package org.aim.mainagent.instrumentation.builder;

import org.aim.api.instrumentation.AbstractEnclosingProbeExtension;
import org.lpe.common.extension.IExtensionArtifact;

public class DummyProbe2Extension extends AbstractEnclosingProbeExtension {

	protected DummyProbe2Extension(final Class<? extends IExtensionArtifact> extensionArtifactClass) {
		super(DummyProbe2.class);
	}
	
}
