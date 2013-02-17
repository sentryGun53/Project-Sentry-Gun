/*
  Part of the GUI for Processing library 
  	http://www.lagers.org.uk/g4p/index.html
	http://gui4processing.googlecode.com/svn/trunk/

  Copyright (c) 2008-09 Peter Lager

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package guicomponents;

/**
 * CLASS FOR INTERNAL USE ONLY
 * 
 * @author Peter Lager
 *
 */
public class GMessenger implements GConstants {

	public static void message(Integer id, Object obj, Object[] info){
		// Display G4P messages if required
		if(G4P.messages){
			switch(id){
			case MISSING:
				missingEventHandler(obj, info);
				break;
			case NONEXISTANT:
				nonexistantEventHandler(obj, info);
				break;
			case ADD_DUPLICATE:
				System.out.println("Component " + obj + " has already been registered!");
				break;
			case USER_COL_SCHEME:
				System.out.println("USER DEFINED colour schema active");
				break;
			case DISABLE_AUTO_DRAW:
				System.out.println("You have disabled autoDraw so you have to use");
				System.out.println("G4P.draw() when you want to display the GUI" );
				System.out.println("this is not action is not reversible." );
				break;
			}
		}
		// Display all runtime errors
		switch(id){
		case EXCP_IN_HANDLER:
			eventHandlerFailed(obj, info);
			break;
		case NOT_PEASYCAM:
			System.out.println("G4P.draw(pcam) - pcam must be a PeasyCam object");
			System.out.println("	you have used a " + obj.getClass().getSimpleName() + " object");
			break;
		case HUD_UNSUPPORTED:
			System.out.println("Please use latest version of PeasyCam");
			break;
		case INVALID_STATUS:
			System.out.println("Unknown camera status - inform Quark at www.processing.org");
		}
	}
	
	/**
	 * 
	 * @param handler
	 * @param info
	 */
	private static void eventHandlerFailed(Object handler, Object[] info) {
		String className = handler.getClass().getSimpleName();
		String methodName = (String) info[0];
		Exception e = (Exception) info[1];
		Throwable t = e.getCause();
		StackTraceElement[] calls = t.getStackTrace();
		StringBuilder output = new StringBuilder();
		output.append("#############  EXCEPTION IN EVENT HANDLER  #############");
		output.append("\nAn error occured during execution of the eventhandler:");
		output.append("\nCLASS: "+className+"   METHOD: "+methodName);
		output.append("\n\tCaused by " + t.toString());
		if(calls.length > 0)
			output.append("\n\t"+ calls[0].toString());
		System.out.println(output);
		System.out.println("########################################################\n");
	}

	/**
	 * 
	 * @param obj1 the object generating the method
	 * @param obj2 the method name
	 * @param obj3 parameter types (Class[])
	 */
	@SuppressWarnings("unchecked")
	private static void missingEventHandler(Object caller, Object[] info) {
		String className = caller.getClass().getSimpleName();
		String methodName = (String) info[0];
		String pname;
		StringBuilder output = new StringBuilder();
		
		output.append("You might want to add a method to handle " + className + " events syntax is\n");
		output.append("void " + methodName + "(");
		if(info != null && info.length > 1){
			Class[] parameters = (Class[])(info[1]);
			if(parameters == null)
				parameters = new Class[0];
			for(int i = 0; i < parameters.length; i++){
				pname = (parameters[i]).getSimpleName();
				output.append(pname + " " + pname.substring(1).toLowerCase());
//				if(parameters.length > 1)
//					output.append(i);
				if(i < parameters.length - 1)
					output.append(", ");
			}
		}
		output.append(") { /* code */ }\n");
		System.out.println(output.toString());
	}

	/**
	 * 
	 * @param obj1 the object generating the method
	 * @param obj2 the method name
	 * @param obj3 parameter types (Class[])
	 */
	@SuppressWarnings("unchecked")
	private static void nonexistantEventHandler(Object handler, Object[] info) {
		String className = handler.getClass().getSimpleName();
		String methodName = (String) info[0];
		String pname;
		StringBuilder output = new StringBuilder();
		
		output.append("The "+className+" class cannot find this method \n");
		output.append("\tvoid " + methodName + "(");
		if(info != null && info.length > 1){
			Class[] parameters = (Class[])(info[1]);
			if(parameters == null)
				parameters = new Class[0];
			for(int i = 0; i < parameters.length; i++){
				pname = (parameters[i]).getSimpleName();
				output.append(pname + " " + pname.substring(1).toLowerCase());
//				if(parameters.length > 1)
//					output.append(i);
				if(i < parameters.length - 1)
					output.append(", ");
			}
		}
		output.append(") { /* code */ }\n");
		System.out.println(output.toString());
	}


}
