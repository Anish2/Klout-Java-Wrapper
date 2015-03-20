package be.bigindustries.klout;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The User class stores all the information pertaining to a klout id user.
 * Each user object has a kloutId, nick name, klout score, and bucket. It also provides
 * the day change, week change, and month change of the klout score. With this class
 * you can also retrieve the user's topics, influencers, and influencees.
 *
 * @author Anish Visaria
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
   * @param id      klout id of user
   * @param api_key your api key
   * @throws IOException
   */
  public User(String id, String api_key) throws IOException {
    String content = getContentBody("http://api.klout.com/v2/user.json/" + id + "?key=" + api_key);
    this.api_key = api_key;

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(content);

    // real parsing starts
    kloutId = jsonNode.get("kloutId").textValue();
    nick = jsonNode.get("nick").textValue();
    score = jsonNode.get("score").get("score").asDouble();
    bucket = jsonNode.get("score").get("bucket").textValue();

    dayChange = jsonNode.get("scoreDeltas").get("dayChange").asDouble(0.0);
    weekChange = jsonNode.get("scoreDeltas").get("weekChange").asDouble(0.0);
    monthChange = jsonNode.get("scoreDeltas").get("monthChange").asDouble(0.0);
  }

  /**
   * Returns klout id.
   *
   * @return klout id
   */
  public String kloutid() {
    return kloutId;
  }

  /**
   * Returns nick name of user.
   *
   * @return nick name
   */
  public String nick() {
    return nick;
  }

  /**
   * Returns score.
   *
   * @return klout score
   */
  public double score() {
    return score;
  }

  /**
   * Returns bucket of score.
   *
   * @return bucket
   */
  public String bucket() {
    return bucket;
  }

  /**
   * Returns day change in score.
   *
   * @return day change
   */
  public double dayChange() {
    return dayChange;
  }

  /**
   * Returns week change in score.
   *
   * @return week change
   */
  public double weekChange() {
    return weekChange;
  }

  /**
   * Returns month change in score.
   *
   * @return month change
   */
  public double monthChange() {
    return monthChange;
  }

  /**
   * Retrieves topics of this user.
   *
   * @return array of topics
   * @throws IOException
   */
  public Topic[] getTopics() throws IOException {
    String content = getContentBody("http://api.klout.com/v2/user.json/" + kloutId + "/topics?key=" + api_key);


    ObjectMapper objectMapper = new ObjectMapper();
    ArrayNode arrayNode = objectMapper.readValue(content, ArrayNode.class);

    Topic[] t = new Topic[arrayNode.size()];

    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode jsonNode = arrayNode.get(i);
      Topic temp = new Topic(jsonNode.get("id").textValue(), jsonNode.get("displayName").textValue(),
                             jsonNode.get("name").textValue(), jsonNode.get("slug").textValue(),
                             jsonNode.get("imageUrl").textValue(), jsonNode.get("displayType").textValue(),
                             jsonNode.get("topicType").textValue());
      t[i] = temp;
    }

    return t;

  }


  /**
   * Returns User[] of influencers.
   *
   * @return influencers
   * @throws IOException
   */
  public User[] getInfluencers() throws IOException {
    String content = getContentBody("http://api.klout.com/v2/user.json/" + kloutId + "/influence?key=" + api_key);

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(content);

    ArrayNode arrayNode = objectMapper.readValue(jsonNode.get("myInfluencers").toString(), ArrayNode.class);

    User[] users = new User[arrayNode.size()];

    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode ent = arrayNode.get(i).get("entity");
      String id = ent.get("id").textValue();
      User temp = new User(id, api_key);
      users[i] = temp;
    }

    return users;
  }


  /**
   * Returns User[] of influencees.
   *
   * @return influencees
   * @throws IOException
   */
  public User[] getInfluencees() throws IOException {
    String content = getContentBody("http://api.klout.com/v2/user.json/" + kloutId + "/influence?key=" + api_key);

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(content);

    ArrayNode arrayNode = objectMapper.readValue(jsonNode.get("myInfluencees").toString(), ArrayNode.class);

    User[] users = new User[arrayNode.size()];

    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode ent = arrayNode.get(i).get("entity");
      String id = ent.get("id").textValue();
      User temp = new User(id, api_key);
      users[i] = temp;
    }

    return users;
  }


  private String getContentBody(String url) throws IOException {
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
