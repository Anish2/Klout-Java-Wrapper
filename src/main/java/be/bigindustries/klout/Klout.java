package be.bigindustries.klout;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This serves as a Klout Java/Android API Wrapper. All functions that can be done with the Klout API
 * can be done through this wrapper as well in a more elegant manner.
 *
 * @author Anish Visaria
 * @author Emre Sevinç
 */
public class Klout {

  public static final String TWITTER = "tw";
  public static final String GOOGLE_PLUS = "gp";
  public static final String INSTAGRAM = "ig";
  public static final String KLOUT = "klout";
  public static final String TWITTER_SCREEN_NAME = "screenName";

  private String api_key;
  private HttpURLConnection conn;
  private final String USER_AGENT = "Mozilla/5.0";


  /**
   * Initializes the Klout object with the api key provided.
   *
   * @param key your api key
   */
  public Klout(String key) {
    api_key = key;
  }


  /**
   * Retrieves the id and network of the specified type. All types return a Klout network id
   * except when the type is Klout.
   *
   * @param id   social network id
   * @param type classification of id
   * @return String[] with elements id and network, respectively.
   * @throws IOException
   */
  public String[] getIdentity(String id, String type) throws IOException {
    String content;
    if (type.equals(KLOUT)) {
      content = getContentBody(String.format("http://api.klout.com/v2/identity.json/%s/%s/tw?key=%s",
                                             type, id, api_key));
    } else if (type.equals(TWITTER_SCREEN_NAME)) {
      content = getContentBody(String.format("http://api.klout.com/v2/identity.json/twitter?screenName=%s&key=%s",
                                             id, api_key));
    } else {
      content = getContentBody(String.format("http://api.klout.com/v2/identity.json/%s/%s?key=%s", type, id, api_key));
    }

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(content);

    return new String[]{jsonNode.get("id").textValue(), jsonNode.get("network").textValue()};
  }


  /**
   * Retrieves a User object with the specified kloutId.
   *
   * @param kloutId klout id of user
   * @return User with id kloutId
   * @throws Exception
   */
  public User getUser(String kloutId) throws IOException {
    return new User(kloutId, api_key);
  }


  private String getContentBody(String url) throws IOException {

    URL obj = new URL(url);
    conn = (HttpURLConnection) obj.openConnection();

    // default is GET
    conn.setRequestMethod("GET");

    conn.setUseCaches(false);

    // act like a browser
    conn.setRequestProperty("User-Agent", USER_AGENT);
    conn.setRequestProperty("Accept", "text/html,application/json;q=0.9,*/*;q=0.8");
    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    return response.toString();
  }


}
