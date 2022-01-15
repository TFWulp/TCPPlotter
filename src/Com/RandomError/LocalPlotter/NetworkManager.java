package Com.RandomError.LocalPlotter;

import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

public class NetworkManager {

	private Socket connectionSocket;
	private ServerSocket serverSocket;
	private BufferedReader rec;
	public int dataLock = 0;
	public LinkedList<Double> Scores = new LinkedList<>();
	public LinkedList<Double> Iterations = new LinkedList<>();
	private long dataPointsCount = 0;
	
	public void startServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		listenForConnection();
		connectionSocket.close();
		serverSocket.close();
		System.out.println("Closed everything");
	}
	
	private void listenForConnection() throws IOException {
		System.out.println("Waiting for connection...");
		connectionSocket = serverSocket.accept();
		Scores.clear();
		Iterations.clear();
		dataPointsCount = 0;
		System.out.println("Connected");
		rec = new BufferedReader(new InputStreamReader (connectionSocket.getInputStream()));
		listenForData();
		main.chart.updateXYSeries("randomWalk", Iterations, Scores, null);
		main.chart.setTitle(main.chart.getTitle() + " | DISCONNECTED ");
		listenForConnection();
		
	}
	
	private void listenForData() {
		String receivingData;
		try {
			while((receivingData = rec.readLine()) != null) {
				//System.out.println(receivingData);
				handleData(receivingData);
				//listenForData();
			}
			
		} catch (IOException e) {
			
		}
		
	}
	
	private void handleData(String data) {
		//System.out.println(data);
		String splitData[] = data.split(";");
		//System.out.println(splitData[0] + " ; " + splitData[1]);
		double score =  Double.parseDouble(splitData[0]);
		double iteration = Double.parseDouble(splitData[1]);
		double Temprature = Double.parseDouble(splitData[2]);
		int paused = Integer.parseInt(splitData[3]);
		
		//if(dataPointsCount > 10000) {
		//	Scores.remove(0);
		//	Iterations.remove(0);
		//}
		
		Scores.add(score);
		Iterations.add(iteration);
		dataPointsCount++;
		if(dataPointsCount > 15000) {
			Scores.remove(0);
			Iterations.remove(0);
		}
		//if(dataPointsCount % 10 == 0) {
			main.chart.setTitle("Temprature: " + Math.floor(Temprature * 100) / 100 + "| Paused: " + paused);
			main.chart.updateXYSeries("randomWalk", Iterations, Scores, null);
		//}
		
		
		
		 
		//if(dataPointsCount % 10 == 0) {
		//	main.chart.updateXYSeries("randomWalk", Iterations, Scores, null);
		//    main.sw.repaintChart();
		//}
		
		//System.out.println(score + "|" + iteration);
		
	}
}
