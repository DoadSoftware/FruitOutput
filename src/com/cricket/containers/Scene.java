package com.cricket.containers;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Scene {
	
	private String scene_path;
	private String broadcaster;
	private String which_layer;
	
	public Scene() {
		super();
	}

	public Scene(String scene_path, String which_layer) {
		super();
		this.scene_path = scene_path;
		this.which_layer = which_layer;
	}
	
	public String getScene_path() {
		return scene_path;
	}

	public void setScene_path(String scene_path) {
		this.scene_path = scene_path;
	}
	
	public String getBroadcaster() {
		return broadcaster;
	}

	public void setBroadcaster(String broadcaster) {
		this.broadcaster = broadcaster;
	}

	public String getWhich_layer() {
		return which_layer;
	}

	public void setWhich_layer(String which_layer) {
		this.which_layer = which_layer;
	}
	
	public void scene_load(List<PrintWriter> print_writers, String broadcaster) throws InterruptedException
	{
		switch (broadcaster.toUpperCase()) {
		case "DOAD-VIZ-MULTI":
			for(PrintWriter print_writer : print_writers) {
				switch(this.which_layer.toUpperCase()) {
				case "FRONT_LAYER":
					print_writer.println("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*" + this.scene_path + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*SCENE_DATA INITIALIZE \0");
					TimeUnit.MILLISECONDS.sleep(500);
					break;
				case "MIDDLE_LAYER":
					print_writer.println("-1 RENDERER SET_OBJECT SCENE*" + this.scene_path + "\0");
					print_writer.println("-1 RENDERER*SCENE_DATA INITIALIZE \0");
					TimeUnit.MILLISECONDS.sleep(500);
					break;
				}
			}
			break;
		
		}
	}	
	public void scene_load(PrintWriter print_writer, String broadcaster) throws InterruptedException
	{
		switch (broadcaster.toUpperCase()) {
		case "DOAD_FRUIT":	
			switch(this.which_layer.toUpperCase()) {
			case "FRONT_LAYER":		System.out.println("DOAD_FRUIT");

				System.out.println(this.which_layer);

				print_writer.println("LAYER1*EVEREST*SINGLE_SCENE LOAD " + this.scene_path + ";");
				
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				TimeUnit.MILLISECONDS.sleep(500);
				break;
			case "MIDDLE_LAYER":
				print_writer.println("LAYER2*EVEREST*SINGLE_SCENE LOAD " + this.scene_path + ";");
				
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER2*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
				TimeUnit.MILLISECONDS.sleep(500);
				break;
			}
			break;
		}
	}
}
