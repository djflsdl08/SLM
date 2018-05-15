package publishData;

import java.util.List;

import dao2.LocationDAO;
import mqtt.MqttCallbackClient;

public class Publish {
	private static Publish testPublish = null;
	
	public static Publish getInstance() {
		if(testPublish == null) {
			MqttCallbackClient.getInstance().connect();
			testPublish = new Publish();
		}
		return testPublish;
	}
	public void publishFoundLocation(String clientId, String district) {
		MqttCallbackClient.getInstance().publish(clientId, 1, district);
	}
	public void publishDistrict(String district, String msg) {
		MqttCallbackClient.getInstance().publish(district, 1, msg);
	}
	public void publishLocation(Double[] locations, String msg) {
		List<String> clients = LocationDAO.getInstance().getClientsAboutLocation(locations);
		for(String client : clients) {
			MqttCallbackClient.getInstance().publish(client, 1, msg);
		}
	}
}
