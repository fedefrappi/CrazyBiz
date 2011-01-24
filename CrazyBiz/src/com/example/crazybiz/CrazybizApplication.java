package com.example.crazybiz;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class CrazybizApplication extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("Crazybiz Application");
		mainWindow.addComponent(new LoginView());
		setMainWindow(mainWindow);
	}

}
