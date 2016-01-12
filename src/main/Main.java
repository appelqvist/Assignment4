
package main;

import java.util.LinkedList;

public class Main
{
	public static void main(String[] args)
	{
		GUIMonitor gui = new GUIMonitor();
		Controller controller = new Controller(gui);
		gui.setController(controller);
		gui.Start();

	}

}
