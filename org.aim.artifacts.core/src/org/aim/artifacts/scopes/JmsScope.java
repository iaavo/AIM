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
 * Scope for JMS.
 * 
 * @author Alexander Wert
 * 
 */
public class JmsScope extends AbstractInstAPIScope {
	/**
	 * Constructor.
	 * 
	 * @param provider
	 *            extension provider
	 */
	public JmsScope(final IExtension provider) {
		super(provider);
	}

	@Override
	protected void init() {
		addMethod("javax.jms.MessageProducer", "send(javax.jms.Message)");
		addMethod("javax.jms.MessageConsumer", "receive()");
		addMethod("javax.jms.MessageListener", "onMessage(javax.jms.Message)");
	}

	@Override
	public long getId() {
		return 0;
	}

}
