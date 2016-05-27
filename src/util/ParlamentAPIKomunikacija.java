package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import domain.Poslanik;

public class ParlamentAPIKomunikacija {
	
	private static final String servis = "http://147.91.128.71:9090/parlament/api/members";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	
	public static List<Poslanik> vratiPoslanike() throws Exception{
		String podaci = sendGet(servis);
		Gson gson = new GsonBuilder().create();
		JsonArray jsonNiz = gson.fromJson(podaci, JsonArray.class);
		
		List<Poslanik> poslanici = new LinkedList<>();
		
		for (int i = 0; i < jsonNiz.size(); i++) {
			
			JsonObject jsonObjekat = (JsonObject) jsonNiz.get(i);
			Poslanik p = new Poslanik();
			
			if(jsonObjekat.get("id") != null)
				p.setId(jsonObjekat.get("id").getAsInt());
			if(jsonObjekat.get("name") != null)
				p.setIme(jsonObjekat.get("name").getAsString());
			if(jsonObjekat.get("lastName") != null)
				p.setPrezime(jsonObjekat.get("lastName").getAsString());
			if(jsonObjekat.get("birthDate") != null)
				p.setDatumRodjenja(sdf.parse(jsonObjekat.get("birthDate").getAsString()));

			poslanici.add(p);

		}
		return poslanici;
	}
	
	private static String sendGet(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

		boolean endReading = false;
		String response = "";

		while (!endReading) {
			String s = in.readLine();

			if (s != null) {
				response += s;
			} else {
				endReading = true;
			}
		}
		in.close();

		return response.toString();
	}

}

