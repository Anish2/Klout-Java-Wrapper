package be.bigindustries.klout;

/**
 * The topic class stores all the information given by the Klout API
 * of a topic. All fields are publicly accessible.
 *
 * @author Anish Visaria
 */
public class Topic {

  public final String id;
  public final String display_name;
  public final String name;
  public final String slug;
  public final String imageUrl;
  public final String displayType;
  public final String topicType;


  /**
   * Constructs topic object.
   *
   * @param id
   * @param display_name
   * @param name
   * @param slug
   * @param image_url
   * @param displayType
   * @param topicType
   */
  public Topic(String id, String display_name, String name, String slug, String image_url,
               String displayType, String topicType) {
    this.id = id;
    this.display_name = display_name;
    this.name = name;
    this.slug = slug;
    imageUrl = image_url;
    this.displayType = displayType;
    this.topicType = topicType;
  }

}
