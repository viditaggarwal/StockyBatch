package com.stocky.batch.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.stocky.batch.model.Column;
import com.stocky.batch.model.Entity;

public class ResultSetMapper<T> {
	
	public List<T> mapResultSetToObjects(ResultSet rs, Class outputClass) {
		List<T> outputList = null;
		try {
			// make sure resultset is not null
			if (rs != null) {
				// check if outputClass has 'Entity' annotation
				if (outputClass.isAnnotationPresent(Entity.class)) {
					// get the resultset metadata
					ResultSetMetaData rsmd = rs.getMetaData();
					// get all the attributes of outputClass
					Field[] fields = outputClass.getDeclaredFields();
					while (rs.next()) {
						T bean = (T) outputClass.newInstance();
						for (int _iterator = 0; _iterator < rsmd
								.getColumnCount(); _iterator++) {
							// getting the SQL column name
							String columnName = rsmd
									.getColumnName(_iterator + 1);
							// reading the value of the SQL column
							Object columnValue = rs.getObject(_iterator + 1);
							// iterating over outputClass attributes to check if any attribute has 'Column' annotation with matching 'name' value
							for (Field field : fields) {
								if (field.isAnnotationPresent(Column.class)) {
									Column column = field
											.getAnnotation(Column.class);
									if (column.name().equalsIgnoreCase(
											columnName)
											&& columnValue != null) {
										BeanUtils.setProperty(bean, field
												.getName(), columnValue);
										break;
									}
								}
							}
						}
						if (outputList == null) {
							outputList = new ArrayList<T>();
						}
						outputList.add(bean);
					}

				} else {
					// throw some error
				}
			} else {
				return null;
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return outputList;
	}
	
	public Map<String, Object> objectToMap(Object obj) {
		Map<String, Object> result = new HashMap<String, Object>();
	    BeanInfo info;
		try {
			info = Introspector.getBeanInfo(obj.getClass());
		    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
		        Method reader = pd.getReadMethod();
		        if (reader != null)
		            result.put(pd.getName(), reader.invoke(obj));
		    }
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	    return result;
	}
}