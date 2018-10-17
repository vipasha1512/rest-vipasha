package hello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

@Controller
public class OrderController {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	ArrayList<Order> list = new ArrayList();
	Object obj;
	JSONArray jo;
	String updateDetails;
	int dataLength = 0;

	@Scheduled(fixedRate = 60000)
	public void updateData() {

		System.out.println("Hello Scheduler");
		try {
			dataLength = list.size();
			list.clear();
			obj = new JSONParser().parse(new FileReader("data.json"));
			jo = (JSONArray) obj;
			for (int i = 0; i < jo.size(); i++) {
				JSONObject jsonobject = (JSONObject) jo.get(i);
				Order order = new Order((int) (long) jsonobject.get("orderid"), (String) jsonobject.get("side"),
						(String) jsonobject.get("security"), (String) jsonobject.get("fundname"),
						(int) (long) jsonobject.get("quantity"), (double) (long) jsonobject.get("price"));

				list.add(order);

			}
			if (list.size() > dataLength) {
				updateDetails = "New Data Added";
			} else {
				updateDetails = "No New Data Added";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/orders")
	public @ResponseBody JSONObject orders() {
		JSONObject message = new JSONObject();
		message.put("message", updateDetails);
		message.put("data", list);
		return message;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ordersSummary")
	public @ResponseBody JSONObject ordersSummary() {
		int totalOrders = 0;
		int totalQuantity = 0;
		double totalPrice = 0;
		double avg = 0;
		Map<String, Integer> combination = new HashMap<>();
		String combi = "";
		int combiCounter = 0;
		JSONObject message = new JSONObject();
		JSONObject output = new JSONObject();
		

		for (Order item : list) {

			String s = "(" + item.getSide() + "+" + item.getSecurity() + "+" + item.getFund_name() + ")";
			if (combination.containsKey(s)) {
				combination.put(s, combination.get(s) + 1);

			} else
				combination.put(s, 1);
		}

		for (Map.Entry<String, Integer> entry : combination.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			if (value > 1) {
				combi = combi + key;
				combiCounter++;
			}

		}

		combi = Integer.toString(combiCounter) + combi;

		for (int i = 0; i < list.size(); i++) {
			Order order = list.get(i);
			totalPrice = totalPrice + order.getAmount();
			totalQuantity = totalQuantity + order.getQuantity();
			totalOrders++;

		}

		avg = totalPrice / totalOrders;
		output.put("total number of orders", totalOrders);
		output.put("total quantity", totalQuantity);
		output.put("average price", avg);
		output.put("total number of combinable orders", combi);

		message.put("message", updateDetails);
		message.put("data", output);

		return message;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ordersSecurity")
	public @ResponseBody JSONObject orderSecurity(
			@RequestParam(value = "security", required = false, defaultValue = "all") String desc) {

		int totalOrders = 0;
		int totalQuantity = 0;
		double totalPrice = 0;
		double avg = 0;
		Map<String, Integer> combination = new HashMap<>();

		String combi = "";
		int combiCounter = 0;
		JSONObject message = new JSONObject();
		JSONObject output = new JSONObject();

		for (Order item : list) {
			if (item.getSecurity().equals(desc) || desc.equals("all")) {
				String s = "(" + item.getSide() + "+" + item.getSecurity() + "+" + item.getFund_name() + ")";
				if (combination.containsKey(s)) {
					combination.put(s, combination.get(s) + 1);

				} else
					combination.put(s, 1);
			}

		}

		for (Map.Entry<String, Integer> entry : combination.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			if (value > 1) {
				combi = combi + key;
				combiCounter++;
			}

		}

		combi = Integer.toString(combiCounter) + combi;

		for (int i = 0; i < list.size(); i++) {
			Order order = list.get(i);
			if (order.getSecurity().equals(desc) || desc.equals("all")) {
				totalPrice = totalPrice + order.getAmount();
				totalQuantity = totalQuantity + order.getQuantity();
				totalOrders++;
			}

		}
		
		avg = totalPrice / totalOrders;
		output.put("total number of orders", totalOrders);
		output.put("total quantity", totalQuantity);
		output.put("average price", avg);
		output.put("total number of combinable orders", combi);

		message.put("message", updateDetails);
		message.put("data", output);

		return message;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/ordersFund")
	public @ResponseBody JSONObject orderFund(
			@RequestParam(value = "fund", required = false, defaultValue = "all") String desc) {

		int totalOrders = 0;
		int totalQuantity = 0;
		double totalPrice = 0;
		double avg = 0;
		Map<String, Integer> combination = new HashMap<>();

		String combi = "";
		int combiCounter = 0;
		JSONObject message = new JSONObject();
		JSONObject output = new JSONObject();

		for (Order item : list) {
			if (item.getFund_name().equals(desc) || desc.equals("all")) {
				String s = "(" + item.getSide() + "+" + item.getSecurity() + "+" + item.getFund_name() + ")";
				if (combination.containsKey(s)) {
					combination.put(s, combination.get(s) + 1);

				} else
					combination.put(s, 1);
			}

		}

		for (Map.Entry<String, Integer> entry : combination.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();

			if (value > 1) {
				combi = combi + key;
				combiCounter++;
			}

		}

		combi = Integer.toString(combiCounter) + combi;

		for (int i = 0; i < list.size(); i++) {
			Order order = list.get(i);
			if (order.getFund_name().equals(desc) || desc.equals("all")) {
				totalPrice = totalPrice + order.getAmount();
				totalQuantity = totalQuantity + order.getQuantity();
				totalOrders++;
			}

		}
		
		avg = totalPrice / totalOrders;
		output.put("total number of orders", totalOrders);
		output.put("total quantity", totalQuantity);
		output.put("average price", avg);
		output.put("total number of combinable orders", combi);

		message.put("message", updateDetails);
		message.put("data", output);

		return message;
	}

}
