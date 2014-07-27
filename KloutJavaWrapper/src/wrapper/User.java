package wrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * The User class stores all the information pertaining to a klout id user.
 * Each user object has a kloutId, nick name, klout score, and bucket. It also provides 
 * the day change, week change, and month change of the klout score. With this class
 * you can also retrieve the user's topics, influencers, and influencees.
 * 
 * @author Anish Visaria
 *
 */
public class User {

	private String kloutId, nick, bucket;
	private double score, dayChange, weekChange, monthChange;
	private HttpURLConnection conn;
	private String api_key;
	private final String USER_AGENT = "Mozilla/5.0";

	
	/**
	 * Parses JSON of the user given by the klout id and stores in fields.
	 * 
	 * @param id klout id of user
	 * @param api_key your api key
	 * @throws Exception
	 */
	public User(String id, String api_key) throws Exception {
		String content = getContentBody("http://api.klout.com/v2/user.json/"+id+"?key="+api_key);
		this.api_key = api_key;
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(content);
		JSONObject jsonObject = (JSONObject) obj;

		// real parsing starts
		kloutId = (String) jsonObject.get("kloutId");
		nick = (String) jsonObject.get("nick");
		score = (double) ((JSONObject) jsonObject.get("score")).get("score");
		bucket = (String) ((JSONObject) jsonObject.get("score")).get("bucket");

		dayChange = (double) ((JSONObject) jsonObject.get("scoreDeltas")).get("dayChange");
		weekChange = (double) ((JSONObject) jsonObject.get("scoreDeltas")).get("weekChange");
		monthChange = (double) ((JSONObject) jsonObject.get("scoreDeltas")).get("monthChange");
	}

	/**
	 * Returns klout id.
	 * @return klout id
	 */
	public String kloutid() {
		return kloutId;
	}

	/**
	 * Returns nick name of user.
	 * @return nick name
	 */
	public String nick() {
		return nick;
	}

	/**
	 * Returns score.
	 * @return klout score
	 */
	public double score() {
		return score;
	}

	/**
	 * Returns bucket of score.
	 * @return bucket
	 */
	public String bucket() {
		return bucket;
	}

	/**
	 * Returns day change in score.
	 * @return day change
	 */
	public double dayChange() {
		return dayChange;
	}

	/**
	 * Returns week change in score.
	 * @return week change
	 */
	public double weekChange() {
		return weekChange;
	}

	/**
	 * Returns month change in score.
	 * @return month change
	 */
	public double monthChange() {
		return monthChange;
	}

	/**
	 * Retrieves topics of this user.
	 * @return array of topics
	 * @throws Exception
	 */
	public Topic[] getTopics() throws Exception {
		String content = getContentBody("http://api.klout.com/v2/user.json/"+kloutId+"/topics?key="+api_key);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(content);

		JSONArray arr = (JSONArray) obj;

		Topic[] t = new Topic[arr.size()];

		for (int i = 0; i < arr.size(); i++) {
			JSONObject jo = (JSONObject) arr.get(i);
			Topic temp = new Topic((String) jo.get("id"), (String) jo.get("displayName"), (String) jo.get("name"),
					(String) jo.get("slug"), (String) jo.get("imageUrl"), (String) jo.get("displayType"),
					(String) jo.get("topicType"));
			t[i] = temp;
		}

		return t;

	}


	/**
	 * Returns User[] of influencers.
	 * @return influencers
	 * @throws Exception
	 */
	public User[] getInfluencers() throws Exception {
		String content = getContentBody("http://api.klout.com/v2/user.json/"+kloutId+"/influence?key="+api_key);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(content);

		JSONArray arr = (JSONArray) ((JSONObject) obj).get("myInfluencers");

		User[] users = new User[arr.size()];

		for (int i = 0; i < arr.size(); i++) {
			String id = (String) ((JSONObject)((JSONObject)arr.get(i)).get("entity")).get("id");
			User temp = new User(id, api_key);
			users[i] = temp;
		}

		return users;
	}

	/**
	 * Returns User[] of influencees.
	 * @return influencees
	 * @throws Exception
	 */
	public User[] getInfluencees() throws Exception {
		String content = getContentBody("http://api.klout.com/v2/user.json/"+kloutId+"/influence?key="+api_key);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(content);

		JSONArray arr = (JSONArray) ((JSONObject) obj).get("myInfluencees");

		User[] users = new User[arr.size()];

		for (int i = 0; i < arr.size(); i++) {
			String id = (String) ((JSONObject)((JSONObject)arr.get(i)).get("entity")).get("id");
			User temp = new User(id, api_key);
			users[i] = temp;
		}

		return users;
	}


	private String getContentBody(String url) throws Exception {

		URL obj = new URL(url);
		conn = (HttpURLConnection) obj.openConnection();

		// default is GET
		conn.setRequestMethod("GET");

		conn.setUseCaches(false);

		// act like a browser
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		BufferedReader in = 
				new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();

	}
	


}
