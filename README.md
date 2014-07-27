Klout Java Wrapper
------------------

Klout Java Wrapper is an interface for the Klout API.

In order to use the API you need to first acquire an API key for the Klout website (http://developer.klout.com/member/register).


How to Install it
------------------

You can easily use it by downloading <b>klout-java-wrapper.jar</b> and attaching
it to your project.


How to Use it
--------------

See the <b>javadoc</b> for more details.

```java

Klout k = new Klout("your api key");
		
// retrieves klout id with twitter screen name
String[] data = k.getIdentity("jtimberlake", Klout.TWITTER_SCREEN_NAME); // contains ["635263", "ks]
		
// retrieves klout id with twitter id
String[] d = k.getIdentity("500042487", Klout.TWITTER); // contains ["54887627490056592", "ks"]
		

// gets user with klout id
User u = k.getUser("635263");
		
double score = u.score();
		
Topic[] topics = u.getTopics();
		
User[] influencers = u.getInfluencers();
User[] influencees = u.getInfluencees();


```
