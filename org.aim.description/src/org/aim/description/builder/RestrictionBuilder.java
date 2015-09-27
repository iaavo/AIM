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
package org.aim.description.builder;

import org.aim.aiminterface.description.restriction.Restriction;

/**
 * Builder for a {@link Restriction}.
 * 
 * @author Henning Schulz, Steffen Becker
 * 
 */
public class RestrictionBuilder<B extends AbstractRestrictableBuilder> {

	private final B parentBuilder;

	private final Restriction restriction;

	/**
	 * Constructor.
	 * 
	 * @param parentBuilder
	 *            builder which called this constructor.
	 * @param restriction 
	 */
	public RestrictionBuilder(final B parentBuilder, final Restriction restriction) {
		super();
		this.parentBuilder = parentBuilder;
		this.restriction = restriction;
	}

	/**
	 * Includes the specified package.
	 * 
	 * @param packageName
	 *            package to be included
	 * @return this builder
	 */
	public RestrictionBuilder<B> includePackage(final String packageName) {
		restriction.addPackageInclude(packageName);
		return this;
	}

	/**
	 * Excludes the specified package.
	 * 
	 * @param packageName
	 *            package to be excluded
	 * @return this builder
	 */
	public RestrictionBuilder<B> excludePackage(final String packageName) {
		restriction.addPackageExclude(packageName);
		return this;
	}

	/**
	 * Includes all methods with the specified modifier.
	 * 
	 * @param modifier
	 *            modifier of the methods to be included
	 * @return this builder
	 */
	public RestrictionBuilder<B> includeModifier(final int modifier) {
		restriction.addModifierInclude(modifier);
		return this;
	}

	/**
	 * Excludes all methods with the specified modifier.
	 * 
	 * @param modifier
	 *            modifier of the methods to be excluded
	 * @return this builder
	 */
	public RestrictionBuilder<B> excludeModifier(final int modifier) {
		restriction.addModifierExclude(modifier);
		return this;
	}
	
	/**
	 * Sets the granularity of this {@code Restriction}. Note is has
	 * to be between 0 to 1.
	 * 
	 * @param granularity
	 *            granularity to be set
	 * @return this builder
	 */
	public RestrictionBuilder<B> setGranularity(final double granularity) {
		restriction.setGranularity(granularity);
		return this;
	}

	/**
	 * Finishes building of this restriction and returns to the parent builder.
	 * 
	 * @return the parent builder
	 */
	public B restrictionDone() {
		parentBuilder.setRestriction(restriction);
		return parentBuilder;
	}

}
