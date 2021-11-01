package cscie90.alex.hw6;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import org.json.JSONObject;

public class Consumer {

	final static String PROFILE_NAME = "developer";
	final static int ITERATIONS = 5;
	final static int MAX_VAL = 100;
	final static int SLEEP = 100;
	final static Regions REGION = Regions.US_EAST_1;
	final static String SQS_URL = "https://queue.amazonaws.com/296095540176/homework6sqs";

	private static AmazonSQS sqs;


	private static void init() throws Exception {

		/*
		 * The default credentials file should have a profile named 'developer'
		 */
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider(PROFILE_NAME);
		try {
			credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " +
							"Please make sure that your credentials file in the default location contains" +
							" a profile named 'developer' who has developer access");
		}
		sqs = AmazonSQSClientBuilder.standard()
				.withCredentials(credentialsProvider)
				.withRegion(REGION)
				.build();
	}

	public static void main(String[] args) throws Exception {

		init();

		// retrieve all messages, read, delete them
		List<Message> messages = sqs.receiveMessage(SQS_URL).getMessages();
		int evenCount=0;
		int oddCount =0;
		
		while (!messages.isEmpty()) {
			for (Message message : messages) {
				JSONObject jsonBody = new JSONObject(message.getBody());
				int messageInt = jsonBody.getInt("Message");
				if(messageInt %2 == 0) {
					evenCount++;
				} else {
					oddCount++;
				}
				System.out.println("Read message: " + messageInt + " Evens: "+ evenCount + " Odds: "+ oddCount);
			    sqs.deleteMessage(SQS_URL, message.getReceiptHandle());
			}
			// get more messages for each loop iteration
			messages = sqs.receiveMessage(SQS_URL).getMessages();
		}

	}

}

