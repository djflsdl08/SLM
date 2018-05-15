package main;

import model.AccessDistrict;
import mqtt.MqttCallbackClient;

public class Main {

	public static void main(String[] args) {
		System.out.println("start main");

		long start = System.currentTimeMillis();

		AccessDistrict d = new AccessDistrict();
		d.findSurfaceArea();
		
		MqttCallbackClient.getInstance().connect();
		MqttCallbackClient.getInstance().subscribe("location", 1);
		
		long end = System.currentTimeMillis();

		System.out.println( "\n>> 실행 시간 : " + ( end - start )/1000.0 );
				
	}

}
