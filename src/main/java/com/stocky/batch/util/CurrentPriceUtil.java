package com.stocky.batch.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codehaus.jettison.json.JSONObject;

import com.stocky.batch.model.Portfolio;

public class CurrentPriceUtil {
	private final static String PRICE_ATTRIBUTE = "l_fix";

	public static List<Map<String, String>> getStockPrices(List<String> inputs){
		try{
			List<CompletableFuture<Map<String, String>>> futures = new ArrayList<CompletableFuture<Map<String, String>>>();
			CompletableFuture<Map<String, String>> future = null;
			
			for (String input : inputs) {
				future = createFuture(input);
				futures.add(future);
			}
			
			List<Map<String, String>> stockPriceList = null;
			try {
				stockPriceList = executeFutures(futures);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			return stockPriceList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private static List<Map<String, String>> executeFutures(List<CompletableFuture<Map<String, String>>> futures) throws Exception {
		// Execute all futures in parallel
		Stream<Map<String, String>> completedFuture = CompletableFuture
				.allOf(futures.toArray(new CompletableFuture[futures.size()]))
				.thenApply(aVoid -> futures.stream().map(CompletableFuture::join)).get();
		return completedFuture.collect(Collectors.toList());
		
	}

	private static CompletableFuture<Map<String, String>> createFuture(String stockId) {

		ExecutorService threadPool = Executors.newWorkStealingPool();
		Supplier<Map<String, String>> asyncFunction = () -> {
				try {
					return getStockPriceFromGoogle(stockId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
		};

		return CompletableFuture.supplyAsync(asyncFunction, threadPool);
	}
	
	public static void setLatestPriceForPortfolio(List<Portfolio> list) {
		try{
	    	List<String> stockIdList = list.stream()
	    		              .map(Portfolio::getStockId)
	    		              .collect(Collectors.toList());
	    	Map<String, String> result = CurrentPriceUtil.getStockPrices(stockIdList).stream()
	    			.flatMap(map -> map.entrySet().stream())
	    			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	    	
	    	Map<String, Portfolio> map = list.stream()
	    	        .collect(Collectors.toMap(Portfolio::getStockId, p->p));
	    	
	    	for(Entry<String, String> e : result.entrySet()){
	    		map.get(e.getKey()).setPrice(Double.parseDouble(e.getValue()));
	    	}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static Map<String, String> getStockPriceFromGoogle(String stockId) throws Exception {
		Map<String, String> stockIdToPriceMap = new HashMap<>();
		URL url = new URL(Constants.FINANCE_GET_PRICE + stockId);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());

		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = bufferedReader.readLine()) != null) {
			if (!(line.contains("//") || line.contains("]"))) {
				sb.append(line);
			}
		}

		JSONObject responseJson = new JSONObject(sb.toString());
		String price = responseJson.getString(PRICE_ATTRIBUTE);

		bufferedReader.close();
		inputStreamReader.close();

		conn.disconnect();
		stockIdToPriceMap.put(stockId, price);
		return stockIdToPriceMap;
	}
}
