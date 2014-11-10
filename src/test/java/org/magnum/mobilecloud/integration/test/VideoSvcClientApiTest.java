package org.magnum.mobilecloud.integration.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;
import org.magnum.mobilecloud.video.TestData;
import org.symptomcheck.capstone.client.SecuredRestBuilder;
import org.symptomcheck.capstone.client.SecuredRestException;
import org.symptomcheck.capstone.client.VideoSvcApi;
import org.symptomcheck.capstone.repository.Video;

import retrofit.RestAdapter.LogLevel;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

import com.google.gson.JsonObject;

/**
 * 
 * This integration test sends a POST request to the VideoServlet to add a new
 * video and then sends a second GET request to check that the video showed up
 * in the list of videos. Actual network communication using HTTP is performed
 * with this test.
 * 
 * The test requires that the VideoSvc be running first (see the directions in
 * the README.md file for how to launch the Application).
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * Pay attention to how this test that actually uses HTTP and the test that just
 * directly makes method calls on a VideoSvc object are essentially identical.
 * All that changes is the setup of the videoService variable. Yes, this could
 * be refactored to eliminate code duplication...but the goal was to show how
 * much Retrofit simplifies interaction with our service!
 * 
 * @author jules
 *
 */
public class VideoSvcClientApiTest {

	private final String USERNAME = "admin";
	private final String PASSWORD = "pass";
	private final String CLIENT_ID = "mobile";
	private final String READ_ONLY_CLIENT_ID = "mobileReader";

	//private final String TEST_URL = "https://localhost:8443";
	private final String TEST_URL = "http://localhost:8080";
	private final String TEST_URL_REMOTE = "https://spring-mvc-capstone-test.appspot.com";

	
	private VideoSvcApi videoServiceNoOAuth = new RestAdapter.Builder()
		
		.setEndpoint(TEST_URL_REMOTE).setLogLevel(LogLevel.FULL).build()
		.create(VideoSvcApi.class);
	
	
	private VideoSvcApi videoServiceRemote = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL_REMOTE + VideoSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			//.setClient(new ApacheClient())
			.setEndpoint(TEST_URL_REMOTE).setLogLevel(LogLevel.FULL).build()
			.create(VideoSvcApi.class);
	
	private VideoSvcApi videoService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			//.setClient(new ApacheClient())
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(VideoSvcApi.class);

	private VideoSvcApi readOnlyVideoService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(READ_ONLY_CLIENT_ID)
			//.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setClient(new ApacheClient())
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(VideoSvcApi.class);

	private VideoSvcApi invalidClientVideoService = new SecuredRestBuilder()
			.setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH)
			.setUsername(UUID.randomUUID().toString())
			.setPassword(UUID.randomUUID().toString())
			.setClientId(UUID.randomUUID().toString())
			//.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setClient(new ApacheClient())
			.setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(VideoSvcApi.class);

	private Video video = TestData.randomVideo();

	
	//@Test
	public void testVideoAddAndList_NoAuth() throws Exception {
		// Add the video
		//videoService.addVideo(video);

		// We should get back the video that we added above
		Collection<Video> videos = videoServiceNoOAuth.getVideoList();
		assertTrue(videos.contains(video));
	}
	
	/**
	 * This test creates a Video, adds the Video to the VideoSvc, and then
	 * checks that the Video is included in the list when getVideoList() is
	 * called.
	 * 
	 * @throws Exception
	 */
	//@Test
	public void testVideoAddAndList() throws Exception {
		// Add the video
		videoService.addVideo(video);

		// We should get back the video that we added above
		Collection<Video> videos = videoService.getVideoList();
		assertTrue(videos.contains(video));
	}
	
	@Test
	public void testVideoAddAndList_Remote() throws Exception {
		// Add the video

		videoServiceRemote.addVideo(video);

		// We should get back the video that we added above
		Collection<Video> videos = videoServiceRemote.getVideoList();
		assertTrue(videos.contains(video));
	}
	
	

	/**
	 * This test ensures that clients with invalid credentials cannot get
	 * access to videos.
	 * 
	 * @throws Exception
	 */
	//@Test
	public void testAccessDeniedWithIncorrectCredentials() throws Exception {

		try {
			// Add the video
			invalidClientVideoService.addVideo(video);

			fail("The server should have prevented the client from adding a video"
					+ " because it presented invalid client/user credentials");
		} catch (RetrofitError e) {
			assert (e.getCause() instanceof SecuredRestException);
		}
	}
	
	/**
	 * This test ensures that read-only clients can access the video list
	 * but not add new videos.
	 * 
	 * @throws Exception
	 */
	//@Test
	public void testReadOnlyClientAccess() throws Exception {

		Collection<Video> videos = readOnlyVideoService.getVideoList();
		assertNotNull(videos);
		
		try {
			// Add the video
			readOnlyVideoService.addVideo(video);

			fail("The server should have prevented the client from adding a video"
					+ " because it is using a read-only client ID");
		} catch (RetrofitError e) {
			JsonObject body = (JsonObject)e.getBodyAs(JsonObject.class);
			assertEquals("insufficient_scope", body.get("error").getAsString());
		}
	}


}
