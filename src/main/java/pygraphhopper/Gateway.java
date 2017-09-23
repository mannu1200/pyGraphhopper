package pygraphhopper;

import py4j.GatewayServer;

public class Gateway {

	public route_calculation getRCObject(String workDir, String vehicle, String osmFile) {
		route_calculation rc = new route_calculation(workDir, vehicle, osmFile);
		return rc;
	}

	public static void main(String[] args) {
		GatewayServer gatewayServer = new GatewayServer(new Gateway());
		gatewayServer.start();
		System.out.println("Gateway Server Started!");
	}
}
