package procontroll;

import java.lang.reflect.Method;

/**
* A Plug is the invocation of a method to handle incoming Events.
* These methods are plugged by reflection, so a plug needs the name 
* of this method and the object where it is declared.
* @author tex
*
*/
class Plug{

	/**
	 * The plugged method
	 */
	private final Method method;

	/**
	 * Name of the method to plug
	 */
	private final String methodName;

	/**
	 * Object containg the method to plug
	 */
	private final Object object;
	
	/**
	 * Class of the object containing the method to plug
	 */
	private final Class objectClass;
	
	/**
	 * Kind of Parameter that is handled by the plug can be
	 * NOTE, Controller, Program Change or a MidiEvent at general
	 */
	private boolean hasParameter = false;

	/**
	 * Initializes a new Plug by a method name and the object 
	 * declaring the method.
	 * @param i_object
	 * @param i_methodName
	 */
	public Plug(
		final Object i_object,
		final String i_methodName
	){
		object = i_object;
		objectClass = object.getClass();
		methodName = i_methodName;
		method = initPlug();
	}
	
	/**
	 * Initializes a new Plug by a method name and the object 
	 * declaring the method.
	 * @param i_object
	 * @param i_methodName
	 */
	public Plug(
		final Object i_object,
		final String i_methodName,
		final boolean i_hasParameter
	){
		object = i_object;
		objectClass = object.getClass();
		methodName = i_methodName;
		hasParameter = i_hasParameter;
		method = initPlug();
	}
	
	boolean hasParamter(){
		return hasParameter;
	}
	
	/**
	 * @throws Exception 
	 * 
	 *
	 */
	private boolean checkParameter(final Class[] objectMethodParams) throws Exception{
		if(hasParameter){
			if(objectMethodParams.length == 2 && hasParameter){
				final Class paramClass1 = objectMethodParams[0];
				final Class paramClass2 = objectMethodParams[1];
			
				if(paramClass1.getName().equals("float")&&paramClass2.getName().equals("float")){
					return true;
				}
			
				throw new RuntimeException("Your plugged method is only allowed to receive float values. " + 
					"Change you method signature from "+
					methodName + "(" + paramClass1.getName()+ "," + paramClass2.getName() + ") to:"+
					methodName + "(float x, float y)."
				);
			}
			throw new RuntimeException("To plug a cross button method your method needs to receive two float values. "+
				"Change you method signature to "+methodName+ "(float x, float y).");
		}else if(objectMethodParams.length == 0){
			return true;
		}
		throw new RuntimeException("To plug a button method your method is not allowed to receive parameters."+
			"Change you method signature to "+methodName+ "( ).");
	}

	/**
	 * Intitializes the method that has been plugged.
	 * @return
	 */
	private Method initPlug(){		
		if (methodName != null && methodName.length() > 0){
			final Method[] objectMethods = objectClass.getDeclaredMethods();
			
			for (int i = 0; i < objectMethods.length; i++){
				objectMethods[i].setAccessible(true);
				
				if (objectMethods[i].getName().equals(methodName)){
					final Class[] objectMethodParams = objectMethods[i].getParameterTypes();
					try{
						checkParameter(objectMethodParams);
						return objectClass.getDeclaredMethod(methodName, objectMethodParams);
					}catch (Exception e){
						e.printStackTrace();
						break;
					}
				}
			}
		}
		throw new RuntimeException("Error on plug: >" +methodName + 
			"< proControll found no method with that name in the given object.");
	}
	
	/**
	 * Calls the plug by invoking the method given by the plug.
	 * @param i_value
	 */
	void call(final float i_x,final float i_y){
      try{
			method.invoke(object,new Object[]{new Float(i_x),new Float(i_y)});
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error on calling plug: " +methodName);
		}
	}
	
	void call(){
		try{
			method.invoke(object,new Object[]{});
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error on calling plug: " +methodName);
		}
	}
}

