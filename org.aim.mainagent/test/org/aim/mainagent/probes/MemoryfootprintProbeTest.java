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
package org.aim.mainagent.probes;

import junit.framework.Assert;

import org.aim.aiminterface.entities.measurements.MeasurementData;
import org.aim.api.instrumentation.AbstractEnclosingProbe;
import org.aim.artifacts.probes.MemoryFootprintProbe;
import org.aim.artifacts.records.MemoryFootprintRecord;

public class MemoryfootprintProbeTest extends ProbeTest {

	@Override
	protected Class<? extends AbstractEnclosingProbe> getProbeType() {
		return MemoryFootprintProbe.class;
	}

	@Override
	protected void checkPreCall(MeasurementData mData) {
		Assert.assertTrue(mData.getRecords().isEmpty());

	}

	@Override
	protected void checkPostCall(MeasurementData mData) {
		Assert.assertFalse(mData.getRecords().isEmpty());
		Assert.assertTrue(mData.getRecords(MemoryFootprintRecord.class).get(0).getEdenSpaceUsedBefore() > 0);
		Assert.assertTrue(mData.getRecords(MemoryFootprintRecord.class).get(0).getEdenSpaceUsedAfter() >= mData
				.getRecords(MemoryFootprintRecord.class).get(0).getEdenSpaceUsedBefore());
	}

	@Override
	protected int getNumberOfMethodInvokations() {
		return 1;
	}

}
