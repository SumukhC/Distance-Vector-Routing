package sumukh.ccn.project3;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Router {

	private static String hostName;
	private static int hostPortNo;
	private static String fileName;
	private static int noOfNeighbors;
	private static int noOfNodesInNetwork;
	private static Map<String, Station> stations;
	private static Map<String, Station> networkInfo;
	private static int counter = 0;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		stations = new HashMap<>();
		networkInfo = new HashMap<>();
		hostName = args[0];
		hostPortNo = Integer.parseInt(args[1]);
		fileName = (System.getProperty("user.dir")).toString() + "\\" + args[2];
		noOfNodesInNetwork = 0;
		readFile();
		startOperation();
	}

	@SuppressWarnings("resource")
	private static void startOperation() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		InetAddress IPAddress = InetAddress.getByName("localhost");
		DatagramSocket socket = new DatagramSocket(hostPortNo);
		byte[] sendingData = new byte[2048];
		byte[] receivedData = new byte[2048];
		UpdaterMessage receivedMessage = new UpdaterMessage();
		boolean isSendingInfo = true;
		while (true) {
			try {
				if (isSendingInfo) {
					socket.setSoTimeout(5000);
					isSendingInfo = false;
				}
				DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
				socket.receive(receivedPacket);
				receivedMessage = convertFromBytesToObject(receivedData);

				if (receivedMessage != null) {
					checkDistanceVectorComputation(receivedMessage);
				}
				if (!isReloadingFileRequired()) {
					readFile();
				}
			} catch (SocketTimeoutException e) {
				isSendingInfo = true;
				UpdaterMessage sendingMessage;
				DatagramPacket sendingPacket;
				for (Map.Entry<String, Station> station : stations.entrySet()) {
					sendingMessage = new UpdaterMessage(hostName, hostPortNo, station.getValue().getCost(), stations,
							networkInfo);
					sendingData = convertFromObjectToBytes(sendingMessage);
					sendingPacket = new DatagramPacket(sendingData, sendingData.length, IPAddress,
							station.getValue().getPortNo());
					socket.send(sendingPacket);
				}
				System.out.println("\nOutput Number: " + ++counter);
				for (Map.Entry<String, Station> station : networkInfo.entrySet()) {
					System.out.println("Shortest Path " + hostName + " - " + station.getKey() + ": "
							+ "The next hop is " + station.getValue().getNextHopRouter() + " and the cost is "
							+ station.getValue().getCost());
				}
				System.out.println();

			}
		}
	}

	private static boolean isReloadingFileRequired() throws FileNotFoundException {
		// TODO Auto-generated method stub

		FileReader fileReader = new FileReader(fileName);
		BufferedReader reader = new BufferedReader(fileReader);
		try {
			noOfNeighbors = Integer.parseInt(reader.readLine());
			if (noOfNodesInNetwork < noOfNeighbors) {
				noOfNodesInNetwork = noOfNeighbors;
			}
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				String name = parts[0];
				int portNo = Integer.parseInt(parts[1]);
				double cost = Double.parseDouble(parts[2]);
				if (stations.containsKey(name)) {
					if ((stations.get(name).getCost() != cost) || (stations.get(name).getPortNo() != portNo)) {
						reader.close();
						return true;
					}
				} else {
					reader.close();
					return false;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;

	}

	private static void checkDistanceVectorComputation(UpdaterMessage receivedMessage) {
		// TODO Auto-generated method stub
		Map<String, Station> networkInfoOfNeighbor = receivedMessage.getNetworkInfo();
		Iterator<Map.Entry<String, Station>> iterator = networkInfoOfNeighbor.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Station> entry = (Map.Entry<String, Station>) iterator.next();
			if (networkInfo.containsKey(entry.getKey())) {

				if ((entry.getValue().getCost()
						+ networkInfo.get(receivedMessage.getSenderName()).getCost()) < (networkInfo.get(entry.getKey())
								.getCost())) {

					double newCost = entry.getValue().getCost()
							+ networkInfo.get(receivedMessage.getSenderName()).getCost();
					networkInfo.get(entry.getKey()).setCost(newCost);
					networkInfo.get(entry.getKey()).setNextHopRouter(receivedMessage.getSenderName());
				}

			} else if (!entry.getKey().equals(hostName)) {

				Station newStation = entry.getValue();
				double cost = newStation.getCost() + networkInfo.get(receivedMessage.getSenderName()).getCost();
				newStation.setCost(cost);
				newStation.setHost(hostName);
				newStation.setNextHopRouter(receivedMessage.getSenderName());
				networkInfo.put(entry.getKey(), newStation);
				++noOfNodesInNetwork;
			}
		}
	}

	private static void readFile() throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		FileReader fileReader = new FileReader(fileName);
		BufferedReader reader = new BufferedReader(fileReader);
		try {

			noOfNeighbors = Integer.parseInt(reader.readLine());
			if (noOfNodesInNetwork < noOfNeighbors) {
				noOfNodesInNetwork = noOfNeighbors;
			}
			String line = null;
			while ((line = reader.readLine()) != null) {
				addStationInformation(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.close();

	}

	private static void addStationInformation(String line) {
		// TODO Auto-generated method stub
		String[] parts = line.split(" ");
		String name = parts[0];
		int portNo = Integer.parseInt(parts[1]);
		double cost = Double.parseDouble(parts[2]);
		Station station = new Station(hostName, name, portNo, cost, name);
		stations.put(name, station);
		networkInfo.put(name, station);
	}

	public static byte[] convertFromObjectToBytes(Object o) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		objStream.writeObject(o);
		objStream.flush();
		return byteStream.toByteArray();
	}

	public static UpdaterMessage convertFromBytesToObject(byte[] b) throws ClassNotFoundException, IOException {
		UpdaterMessage message = null;
		ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
		ObjectInputStream objStream = new ObjectInputStream(byteStream);
		message = (UpdaterMessage) objStream.readObject();
		return message;
	}

}
