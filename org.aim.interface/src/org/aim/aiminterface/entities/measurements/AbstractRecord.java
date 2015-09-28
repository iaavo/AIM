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
package org.aim.aiminterface.entities.measurements;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link AbstractRecord} encapsulates the common functionality of all
 * record classes. A record class is created for each monitoring event.
 * 
 * @author Alexander Wert
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public abstract class AbstractRecord implements Serializable {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRecord.class);

	private static final long serialVersionUID = -4324134213075474222L;
	private static final Map<Class<? extends AbstractRecord>, Field[]> fields = new HashMap<Class<? extends AbstractRecord>, Field[]>();
	public static final String PAR_TIMESTAMP = "timeStamp";
	public static final String PAR_CALL_ID = "callId";
	public static final String PAR_PROCESS_ID = "processId";

	private static final String PROCESS_ID_CONSTANT = ManagementFactory.getRuntimeMXBean().getName();

	@RecordValue(metric = true, name = PAR_TIMESTAMP, isTimestamp = true)
	long timeStamp;

	@RecordValue(name = PAR_CALL_ID, metric = true)
	long callId;

	@RecordValue(name = PAR_PROCESS_ID, metric = false)
	String processId;

	/**
	 * Public default constructor required for json realization.
	 */
	public AbstractRecord() {
		setProcessId(PROCESS_ID_CONSTANT);
		// TODO: remove if possible due to overhead during monitoring
		for (final Field field : getAllParameterFields()) {
			field.setAccessible(true);
		}

	}

	/**
	 * Constructor.
	 * 
	 * @param timestamp
	 *            timestamp of the record.
	 */
	public AbstractRecord(final long timestamp) {
		this();
		setTimeStamp(timestamp);
	}

	/**
	 * 
	 * @return timestamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Sets the timestamp.
	 * 
	 * @param timeStamp
	 *            the timestamp
	 */
	public void setTimeStamp(final long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Converts the record to a string array.
	 * 
	 * @return string array representation of the record
	 */
	public String[] toStringArray() {
		final List<Object> list = toList();
		final String[] strArray = new String[list.size()];
		int i = 0;
		for (Object o : list) {
			if (o instanceof String) {
				o = ((String) o).replace(";", "#sc#");
			}
			strArray[i] = o.toString();
			i++;
		}
		return strArray;
	}

	/**
	 * Converts the record to an Object array.
	 * 
	 * @return Object array representation of the record
	 */
	public Object[] toArray() {
		return toList().toArray();
	}

	/**
	 * Converts the record to an Object list.
	 * 
	 * @return Object list representation of the record
	 */
	public List<Object> toList() {
		try {
			final List<Object> values = new ArrayList<Object>();
			for (final Field field : getAllParameterFields()) {
				field.setAccessible(true);
				values.add(field.get(this));
			}
			return values;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Fills the content of the record from the given string array.
	 * 
	 * @param array
	 *            array of strings containing the values for the record
	 */
	public void fromStringArray(final String[] array) {
		final Field[] fieldsArray = getAllParameterFields();
		if (array.length < fieldsArray.length) {
			throw new RuntimeException("Number of values does not match number of record fields!");
		}

		try {
			for (int i = 0; i < fieldsArray.length; i++) {
				final Field field = fieldsArray[i];
				field.setAccessible(true);
				field.set(this, stringToType(array[i], field.getType()));
			}
		} catch (final Exception e) {
			LOGGER.error("Monitoring error. Reason: {}", e);
		}
	}

	/**
	 * Fills the content of the record from the given String list.
	 * 
	 * @param list
	 *            list of string containing the values for the record
	 */
	public void fromStringList(final List<String> list) {
		final Field[] fieldsArray = getAllParameterFields();
		if (list.size() < fieldsArray.length) {
			throw new RuntimeException("Number of values does not match number of record fields!");
		}

		try {
			for (int i = 0; i < fieldsArray.length; i++) {
				final Field field = fieldsArray[i];
				field.setAccessible(true);
				field.set(this, stringToType(list.get(i), field.getType()));
			}
		} catch (final Exception e) {
			LOGGER.error("Monitoring error. Reason: {}", e);
		}
	}

	/**
	 * Fills the content of the record from the given Object array.
	 * 
	 * @param array
	 *            array of Objects containing the values for the record
	 */
	public void fromArray(final Object[] array) {

		final Field[] fieldsArray = getAllParameterFields();
		if (array.length < fieldsArray.length) {
			throw new RuntimeException("Number of values does not match number of record fields!");
		}

		try {
			for (int i = 0; i < fieldsArray.length; i++) {
				final Field field = fieldsArray[i];
				field.setAccessible(true);
				field.set(this, array[i]);
			}
		} catch (final Exception e) {
			LOGGER.error("Monitoring error. Reason: {}", e);
		}
	}

	/**
	 * Fills the content of the record from the given Object list.
	 * 
	 * @param list
	 *            list of Objects containing the values for the record
	 */
	public void fromList(final List<Object> list) {
		final Field[] fieldsArray = getAllParameterFields();
		if (list.size() < fields.size()) {
			throw new RuntimeException("Number of values does not match number of record fields!");
		}

		try {
			for (int i = 0; i < fields.size(); i++) {
				final Field field = fieldsArray[i];
				field.setAccessible(true);
				field.set(this, list.get(i));
			}
		} catch (final Exception e) {
			LOGGER.error("Monitoring error. Reason: {}", e);
		}
	}

	/**
	 * Returns the list of all record fields.
	 * 
	 * @return list of input/observation parameter fields
	 */
	@JsonIgnore
	public Field[] getAllParameterFields() {
		if (!fields.containsKey(this.getClass())) {
			final List<Field> fieldList = new ArrayList<Field>();

			Class<?> clazz = this.getClass();
			while (!clazz.equals(Object.class)) {
				for (final Field field : clazz.getDeclaredFields()) {
					if (field.isAnnotationPresent(RecordValue.class)) {
						fieldList.add(field);
					}
				}
				clazz = clazz.getSuperclass();
			}

			Collections.sort(fieldList, new Comparator<Field>() {

				@Override
				public int compare(final Field o1, final Field o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			fields.put(this.getClass(), fieldList.toArray(new Field[0]));
		}

		return fields.get(this.getClass());

	}

	/**
	 * Returns a list of all parameter names (record properties).
	 * 
	 * @return a list of all parameter names
	 */
	@JsonIgnore
	public List<String> getAllParameterNames() {
		final List<String> values = new ArrayList<String>();
		for (final Field field : getAllParameterFields()) {
			values.add(field.getName());
		}
		return values;
	}

	/**
	 * Returns a list of all parameter names (record properties) which are
	 * NON-metric properties (input parameters).
	 * 
	 * @return a list of all parameter names (record properties) which are
	 *         NON-metric properties (input parameters)
	 */
	@JsonIgnore
	public List<String> getNonMetricParameterNames() {
		final List<String> values = new ArrayList<String>();
		for (final Field field : getAllParameterFields()) {
			if (!field.getAnnotation(RecordValue.class).metric()) {
				try {
					values.add(field.getName());
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return values;
	}

	/**
	 * Returns a list of all parameter names (record properties) which are
	 * NON-metric properties (input parameters).
	 * 
	 * @return a list of all parameter names (record properties) which are
	 *         NON-metric properties (input parameters)
	 */
	@JsonIgnore
	public List<Integer> getNonMetricParameterIndeces() {
		final List<Integer> values = new ArrayList<Integer>();
		int i = 0;
		for (final Field field : getAllParameterFields()) {
			if (!field.getAnnotation(RecordValue.class).metric()) {
				try {
					values.add(i);
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
			i++;
		}
		return values;
	}

	/**
	 * Returns a list of all parameter names (record properties) which are
	 * metric properties (observation parameters).
	 * 
	 * @return a list of all parameter names (record properties) which are
	 *         metric properties (observation parameters)
	 */
	@JsonIgnore
	public Set<String> getMetricParameterNames() {
		final Set<String> values = new HashSet<String>();
		for (final Field field : getAllParameterFields()) {
			if (field.getAnnotation(RecordValue.class).metric()) {
				try {
					values.add(field.getName());
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return values;
	}

	/**
	 * Returns a list of all parameter values (record properties values) which
	 * are NON-metric properties (input parameters).
	 * 
	 * @return a list of all parameter values (record properties values) which
	 *         are NON-metric properties (input parameters)
	 */
	@JsonIgnore
	public List<Object> getNonMetricValues() {
		final List<Object> values = new ArrayList<Object>();
		for (final Field field : getAllParameterFields()) {
			if (!field.getAnnotation(RecordValue.class).metric()) {
				try {
					values.add(field.get(this));
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return values;
	}

	/**
	 * Returns a list of all parameter values (record properties values) which
	 * are metric properties (observation parameters).
	 * 
	 * @return a list of all parameter values (record properties values) which
	 *         are metric properties (observation parameters)
	 */
	@JsonIgnore
	public List<Object> getMetricValues() {
		final List<Object> values = new ArrayList<Object>();
		for (final Field field : getAllParameterFields()) {
			if (field.getAnnotation(RecordValue.class).metric()) {
				try {
					values.add(field.get(this));
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return values;
	}

	/**
	 * Returns the value for the given field name.
	 * 
	 * @param fieldName
	 *            name of the field
	 * @return value of the field or null if a field with that name could not be
	 *         found
	 */
	@JsonIgnore
	public Object getValue(final String fieldName) {
		try {
			for (final Field field : getAllParameterFields()) {
				if (field.getName().equals(fieldName)) {
					return field.get(this);
				}
			}
			return null;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the type for the given field name.
	 * 
	 * @param fieldName
	 *            name of the field
	 * @return type of the field or null if a field with that name could not be
	 *         found
	 */
	@JsonIgnore
	public Class<?> getType(final String fieldName) {
		try {
			for (final Field field : getAllParameterFields()) {
				if (field.getName().equals(fieldName)) {
					return field.getType();
				}
			}
			return null;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns a string representation of the record, where all values are
	 * represented as strings separated by a semicolon (;).
	 * 
	 * @return string representation of the record
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		try {
			for (final Field field : getAllParameterFields()) {
				if (field.getType().equals(String.class)) {
					builder.append((field.get(this) != null) ? (field.get(this).toString().replace(";", "#sc#")) : (""));
				} else {
					builder.append((field.get(this) != null) ? (field.get(this).toString()) : (""));
				}
				builder.append(';');
			}

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		builder.append(this.getClass().getCanonicalName());
		return builder.toString();
	}

	/**
	 * Creates a specific record object from a string representation.
	 * 
	 * @param stringRep
	 *            string representation of the record
	 * @return a specific record object or null if record could not be created
	 *         from string
	 */
	public static AbstractRecord fromString(final String stringRep) {
		try {
			final String className = stringRep.substring(stringRep.lastIndexOf(';') + 1, stringRep.length());
			final String[] values = stringRep.substring(0, stringRep.lastIndexOf(';')).split(";");
			final AbstractRecord record = (AbstractRecord) Class.forName(className).newInstance();
			record.fromStringArray(values);
			return record;
		} catch (final Exception e) {
			LOGGER.warn("Failed creating record from string, reason: {}", e);
			return null;
		}
	}

	/**
	 * Relativises the timestamp of this record against the passed
	 * relativiseAgainst timestamp.
	 * 
	 * @param relativiseAgainst
	 *            timestamp to relativise against
	 */
	public void relativiseTimestamps(final long relativiseAgainst) {
		try {
			for (final Field field : getAllParameterFields()) {
				if (field.getAnnotation(RecordValue.class).isTimestamp()) {
					final long newTimestamp = field.getLong(this) - relativiseAgainst;
					field.set(this, newTimestamp);
				}
			}
		} catch (final Exception e) {
			LOGGER.error("Monitoring error. Reason: {}", e);
		}
	}

	/**
	 * Creates an object of given type from a string
	 * 
	 * @param str
	 *            string to be parsed
	 * @param type
	 *            type of interest
	 * @return an object of the passed type for the passed string
	 */
	private Object stringToType(final String str, final Type type) {
		if (type instanceof Class) {
			final String typeName = ((Class<?>) type).getSimpleName();

			if (typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("integer")) {
				return Integer.parseInt(str);
			} else if (typeName.equalsIgnoreCase("long")) {
				return Long.parseLong(str);
			} else if (typeName.equalsIgnoreCase("short")) {
				return Short.parseShort(str);
			} else if (typeName.equalsIgnoreCase("byte")) {
				return Byte.parseByte(str);
			} else if (typeName.equalsIgnoreCase("char") || typeName.equalsIgnoreCase("character")) {
				return new Character(str.charAt(0));
			} else if (typeName.equalsIgnoreCase("double")) {
				return Double.parseDouble(str);
			} else if (typeName.equalsIgnoreCase("string")) {
				return str.replace("#sc#", ";");
			} else if (typeName.equalsIgnoreCase("boolean")) {
				return Boolean.parseBoolean(str);
			}
		}
		LOGGER.error("Monitoring error. Invalid value type of a record parameter!");
		return null;
	}

	/**
	 * @return the callId
	 */
	public long getCallId() {
		return callId;
	}

	/**
	 * @param callId
	 *            the callId to set
	 */
	public void setCallId(final long callId) {
		this.callId = callId;
	}

	/**
	 * @return the processId
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * @param processId
	 *            the processId to set
	 */
	public void setProcessId(final String processId) {
		this.processId = processId;
	}

}
