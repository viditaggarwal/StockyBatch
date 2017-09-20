package com.stocky.batch.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.stocky.batch.model.Coin;
import com.stocky.batch.model.Portfolio;

public class CurrentPriceUtil {
	private static final String DELIMITER = "_";
	public final static String PRICE_ATTRIBUTE = "PRICE";
	public final static String PRICE_BEFORE_24_ATTRIBUTE = "price_before_24h";
	public static final String CHANGE = "CHANGEPCT24HOUR";
	
	private static Map<String, Coin> stockMap = new HashMap<String, Coin>();

	public static List<Map<String, Map<String, String>>> getStockPrices(List<String> inputs){
		List<Map<String, Map<String, String>>> stockPriceList = new ArrayList<>();
		Iterator<String> it = inputs.iterator();
		while(it.hasNext()){
			String s = it.next();
			if(stockMap.containsKey(s) && 
					(stockMap.get(s).getTimeStamp().getTime() + 900000) >  new java.util.Date().getTime()){
				it.remove();
				addStockToResult(stockPriceList, s);
			}
		}
		List<CompletableFuture<Void>> futures = 
				new ArrayList<CompletableFuture<Void>>();
		CompletableFuture<Void> future = null;
		
		for (String input : inputs) {
			future = createFuture(input);
			futures.add(future);
		}
		
		try {
			executeFutures(futures);
			for(String input : inputs){
				addStockToResult(stockPriceList, input);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stockPriceList;
	}

	private static void addStockToResult(List<Map<String, Map<String, String>>> stockPriceList, String input) {
		HashMap<String, Map<String, String>> individualStockMap = new HashMap<String, Map<String, String>>();
		HashMap<String, String> value = new HashMap<String, String>();
		value.put(PRICE_ATTRIBUTE, Double.toString(stockMap.get(input).getValue()));
		individualStockMap.put(input, value);
		stockPriceList.add(individualStockMap);
	}

	private static void executeFutures(
			List<CompletableFuture<Void>> futures) throws Exception {
		// Execute all futures in parallel
		CompletableFuture
				.allOf(futures.toArray(new CompletableFuture[futures.size()]))
				.thenApply(aVoid -> futures.stream().map(CompletableFuture::join)).get();
		
	}

	private static CompletableFuture<Void> createFuture(String stockId) {

		ExecutorService threadPool = Executors.newWorkStealingPool();
		Supplier<Void> asyncFunction = () -> {
				try {
					return getStockPrice(stockId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
		};

		return CompletableFuture.supplyAsync(asyncFunction, threadPool);
	}
	
	public static void setLatestPriceForPortfolio(List<Portfolio> list) {
    	List<String> stockIdList = list.stream()
    		              .map(Portfolio::getStockId)
    		              .collect(Collectors.toList());
    	Map<String, Map<String, String>> result = CurrentPriceUtil.getStockPrices(stockIdList).stream()
    			.flatMap(map -> map.entrySet().stream())
    			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    	
    	Map<String, Portfolio> map = list.stream()
    	        .collect(Collectors.toMap(Portfolio::getStockId, p->p));
    	
    	for(Entry<String, Map<String, String>> e : result.entrySet()){
    		map.get(e.getKey()).setPrice(Double.parseDouble(e.getValue().get(PRICE_ATTRIBUTE)));
    		map.get(e.getKey()).setChange(Double.parseDouble(e.getValue().get(CHANGE)) > 0 ? true : false);
    	}
	}
	
	private static Void getStockPrice(String stockId) throws Exception {
		Map<String, Map<String, String>> stockIdToPriceMap = new HashMap<>();
		stockId = stockId.replace("-", DELIMITER);
		URL url = new URL(Constants.FINANCE_GET_PRICE + stockId);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
//		conn.setRequestProperty("Accept", "application/json");

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

		String fsym = stockId.split(DELIMITER)[0].toUpperCase();
		String tsym = stockId.split(DELIMITER)[1].toUpperCase();
		JSONObject responseJson = new JSONObject(sb.toString());
		responseJson = responseJson.getJSONObject("RAW").getJSONObject(fsym).getJSONObject(tsym);
		String price = Double.toString(responseJson.getDouble(PRICE_ATTRIBUTE));
		
		stockMap.put(stockId, new Coin(Double.valueOf(price), new Date(new java.util.Date().getTime())));
		
		bufferedReader.close();
		inputStreamReader.close();
		conn.disconnect();
		return null;
	}
	
	public static void main(String args[]){
		List<String> l = new ArrayList<>();
		l.add("BTC_USD");
//		setLatestPriceForPortfolio(l);
	}
}
