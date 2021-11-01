package cscie90.alex.hw6;

import java.util.Random;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

public class Producer {
	
	final static String PROFILE_NAME = "developer";
	final static int ITERATIONS = 5000;
	final static int MAX_VAL = 100;
	final static int SLEEP = 5000;
	final static Regions REGION = Regions.US_EAST_1;
	final static String TOPIC_ARN = "arn:aws:sns:us-east-1:296095540176:homework6";

	private static AmazonSNS sns;


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
		sns = AmazonSNSClientBuilder.standard()
				.withCredentials(credentialsProvider)
				.withRegion(REGION)
				.build();
	}

	public static void main(String[] args) throws Exception {

		init();

		// loop and publish a random value, sleeping between publications

		Random rand_gen = new Random();

		for (int i=0; i<ITERATIONS; i++) {
			int publishInt = rand_gen.nextInt(MAX_VAL)+1;
			sns.publish(TOPIC_ARN, Integer.toString(publishInt));
			System.out.println("Published " + publishInt + " to topic: "+ TOPIC_ARN);
			Thread.sleep(SLEEP);
		}

	}

}

