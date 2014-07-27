package wrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;


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
		
		JdomParser parser = new JdomParser();
		JsonRootNode stuff = parser.parse(content);

		// real parsing starts
		kloutId = stuff.getStringValue("kloutId");
		nick = stuff.getStringValue("nick");
		score = Double.parseDouble(stuff.getNode("score").getNumberValue("score"));
		bucket = stuff.getNode("score").getStringValue("bucket");

		dayChange = Double.parseDouble(stuff.getNode("scoreDeltas").getNumberValue("dayChange"));
		weekChange = Double.parseDouble(stuff.getNode("scoreDeltas").getNumberValue("weekChange"));
		monthChange = Double.parseDouble(stuff.getNode("scoreDeltas").getNumberValue("monthChange"));
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

		JdomParser parser = new JdomParser();
		List<JsonNode> arr = parser.parse(content).getArrayNode();

		Topic[] t = new Topic[arr.size()];

		for (int i = 0; i < arr.size(); i++) {
			JsonNode jo = arr.get(i);
			Topic temp = new Topic(jo.getStringValue("id"), jo.getStringValue("displayName"), jo.getStringValue("name"),
					jo.getStringValue("slug"), jo.getStringValue("imageUrl"), jo.getStringValue("displayType"),
					jo.getStringValue("topicType"));
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

		JdomParser parser = new JdomParser();
		JsonNode stuff = parser.parse(content);
		
		List<JsonNode> arr = stuff.getArrayNode("myInfluencers");

		User[] users = new User[arr.size()];

		for (int i = 0; i < arr.size(); i++) {
			JsonNode ent = arr.get(i).getNode("entity");
			String id = ent.getStringValue("id");
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

		JdomParser parser = new JdomParser();
		JsonNode stuff = parser.parse(content);
		
		List<JsonNode> arr = stuff.getArrayNode("myInfluencees");

		User[] users = new User[arr.size()];

		for (int i = 0; i < arr.size(); i++) {
			JsonNode ent = arr.get(i).getNode("entity");
			String id = ent.getStringValue("id");
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
