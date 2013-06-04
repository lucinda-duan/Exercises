/* Copyright 2010 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * Modified by Sambit Sahu
 * Modified by Kyung-Hwa Kim (kk2515@columbia.edu)
 * 
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AttachVolumeRequest;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateImageRequest;
import com.amazonaws.services.ec2.model.CreateImageResult;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.CreateVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.DetachVolumeRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


public class Assignment1 {

    /*
     * Important: Be sure to fill in your AWS access credentials in the
     *            AwsCredentials.properties file before you try to run this
     *            sample.
     * http://aws.amazon.com/security-credentials
     */

    static AmazonEC2      ec2;
    static AmazonS3Client s3;

    public static void main(String[] args) throws Exception {


    	 AWSCredentials credentials = new PropertiesCredentials(
    			 Assignment1.class.getResourceAsStream("AwsCredentials.properties"));

         /*********************************************
          *  #1 Create Amazon Client object
          **********************************************/
    	 ec2 = new AmazonEC2Client(credentials);

         
         // We assume that we've already created an instance. Use the id of the instance.
         //String instanceId = "i-4e6c2a3d"; //put your own instance id to test this code.
         
         try{
       
        	 CreateSecurityGroupRequest createSecurityGroupRequest = new CreateSecurityGroupRequest();
             
             createSecurityGroupRequest.withGroupName("mini").withDescription("My Java Security Group");
           
             CreateSecurityGroupResult createSecurityGroupResult = ec2.createSecurityGroup(createSecurityGroupRequest);
           
             IpPermission ipPermission = new IpPermission();
            		    	
              
                    ipPermission.withIpRanges("0.0.0.0/0", "150.150.150.150/32")
            		            .withIpProtocol("tcp")
            		            .withFromPort(22)
            		            .withToPort(22);
   AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest =new AuthorizeSecurityGroupIngressRequest();
            			    	
     authorizeSecurityGroupIngressRequest.withGroupName("mini")
            			                 .withIpPermissions(ipPermission);
     ec2.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
            			
    CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest();
            					    	
            			 createKeyPairRequest.withKeyName("E3instance_key");
            				
   CreateKeyPairResult createKeyPairResult = ec2.createKeyPair(createKeyPairRequest);
            				
            				KeyPair keyPair = new KeyPair();
            		        keyPair = createKeyPairResult.getKeyPair();
            					    	
            				String privateKey = keyPair.getKeyMaterial();
            				
            				System.out.print(privateKey);
            		
            				   /*********************************************
            	             *                 
            	             *  #1.1 Describe Key Pair
            	             *                 
            	             *********************************************/
            	            System.out.println("\n#1.1 Describe Key Pair");
            	            DescribeKeyPairsResult dkr = ec2.describeKeyPairs();
            	            System.out.println(dkr.toString());
            	            

            	            
            	            /*********************************************
            	             * 
            	             *  #1.2 Create an Instance
            	             *  
            	             *********************************************/
            	            
            RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
            					        	
            					  runInstancesRequest.withImageId("ami-ab844dc2")
            					                     .withInstanceType("t1.micro")
            					                     .withMinCount(2)
            					                     .withMaxCount(2)
            					                     .withKeyName("E3instance_key")
            					                     .withSecurityGroups("mini");
           RunInstancesResult runInstancesResult =  ec2.runInstances(runInstancesRequest);
            	             
                             System.out.println("\n#1.2 Create an Instance");
            	           
           List<Instance> resultInstance = runInstancesResult.getReservation().getInstances();
            	            
           String createdInstanceId = null; 
            	             
           for (Instance ins : resultInstance)
                			 {
            	            	createdInstanceId = ins.getInstanceId();
            	            	System.out.println("New instance has been created: "+ins.getInstanceId());
            	             }
            	            
           String myinstanceZone=resultInstance.get(0).getPlacement().getAvailabilityZone();
           String myinstanceZone1=resultInstance.get(1).getPlacement().getAvailabilityZone();
           String myinstanceID=resultInstance.get(0).getInstanceId();
           String myinstanceID1=resultInstance.get(1).getInstanceId();
            	            
           Thread.sleep(1000*1*60);
            	             
            	                         
        	 
        	 
        	/*********************************************
            *  #2.1 Create a volume
            *********************************************/
         	//create a volume
        	CreateVolumeRequest cvr = new CreateVolumeRequest();
        	CreateVolumeRequest cvr1 = new CreateVolumeRequest();
	        cvr.setAvailabilityZone(myinstanceZone);
	        cvr1.setAvailabilityZone(myinstanceZone1);
	        cvr.setSize(10); //size = 10 gigabytes
	        cvr1.setSize(10); 
        	CreateVolumeResult volumeResult = ec2.createVolume(cvr);
        	CreateVolumeResult volumeResult1 = ec2.createVolume(cvr1);
        	String createdVolumeId = volumeResult.getVolume().getVolumeId();
        	String createdVolumeId1 = volumeResult1.getVolume().getVolumeId();
         	String[] volumeID= new String[2];
         	volumeID[0]=createdVolumeId;
         	volumeID[1]=createdVolumeId1;
        	System.out.println("\n#2.1 Create a volume for each instance");
        	
        	Thread.sleep(1000*1*60);
        	/*********************************************
            *  #2.2 Attach the volume to the instance
            *********************************************/
        	AttachVolumeRequest avr = new AttachVolumeRequest();
        	AttachVolumeRequest avr1 = new AttachVolumeRequest();
        	avr.setInstanceId(myinstanceID);
        	avr1.setInstanceId(myinstanceID1);
        	avr.setVolumeId(createdVolumeId);
        	avr1.setVolumeId(createdVolumeId1);
        	avr.setDevice("/dev/sda2");
        	avr1.setDevice("/dev/sda2");
        	//avr.setVolumeId(createdVolumeId);
        	//avr.setInstanceId(createdInstanceId);
        	//avr.setDevice("/dev/sdf");
        	ec2.attachVolume(avr);
        	ec2.attachVolume(avr1);
        	System.out.println("\n#2.2 Attach the volume");
        	System.out.println("EBS volume has been attached and the volume ID is: "+createdVolumeId);
        	System.out.println("EBS volume has been attached and the volume ID is: "+createdVolumeId1);

        	
        	Thread.sleep(1000*2*60);
        	/***********************************
			 *   #2.3 Monitoring (CloudWatch)
			 *********************************/
			
			//create CloudWatch client
			AmazonCloudWatchClient cloudWatch = new AmazonCloudWatchClient(credentials) ;
			
			//create request message
			GetMetricStatisticsRequest statRequest = new GetMetricStatisticsRequest();
			
			//set up request message
			statRequest.setNamespace("AWS/EC2"); //namespace
			statRequest.setPeriod(60); //period of data
			ArrayList<String> stats = new ArrayList<String>();
			
			//Use one of these strings: Average, Maximum, Minimum, SampleCount, Sum 
			stats.add("Average"); 
			stats.add("Sum");
			statRequest.setStatistics(stats);
			
			//Use one of these strings: CPUUtilization, NetworkIn, NetworkOut, DiskReadBytes, DiskWriteBytes, DiskReadOperations  
			statRequest.setMetricName("CPUUtilization"); 
			
			// set time
			GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
			calendar.add(GregorianCalendar.SECOND, -1 * calendar.get(GregorianCalendar.SECOND)); // 1 second ago
			Date endTime = calendar.getTime();
			calendar.add(GregorianCalendar.MINUTE, -10); // 10 minutes ago
			Date startTime = calendar.getTime();
			statRequest.setStartTime(startTime);
			statRequest.setEndTime(endTime);
			
			//specify an instance
			ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
			
			String monitorInstanceId = null;
			int i=0;
			String[] idleInstance = new String[2];
			
			for (Instance ins : resultInstance)
			{
				monitorInstanceId = ins.getInstanceId();
				dimensions.add(new Dimension().withName("InstanceId").withValue(monitorInstanceId));
                statRequest.setDimensions(dimensions);
		    
			    Thread.sleep(1000*3*60);
			    //get statistics
			    GetMetricStatisticsResult statResult = cloudWatch.getMetricStatistics(statRequest);
			
			    //display
			    System.out.println(statResult.toString());
			    List<Datapoint> dataList = statResult.getDatapoints();
			    Double averageCPU = null;
			    Date timeStamp = null;
			    for (Datapoint data : dataList)
			    {
				averageCPU = data.getAverage();
				timeStamp = data.getTimestamp();
				System.out.println("Average CPU utlilization for last 1 minutes: "+averageCPU);
				//System.out.println("Total CPU utlilization for last 1 minutes: "+data.getSum());
	     
				
				//Calendar vmTime=GregorianCalendar.getInstance();
				//vmTime.setTime(timeStamp);
				//vmTime.get(Calendar.HOUR_OF_DAY);
			    if(averageCPU<50 && i<2)
			     {
			       	idleInstance[i]=monitorInstanceId;
			    	i++;
			     }
			    }
	  
			}  
			 System.out.println("\n" + i + " instance(s) idling.");
        	/*********************************************
            *  #2.4 Detach the volume from the instance 
            *********************************************/
        	   
        	DetachVolumeRequest dvr = new DetachVolumeRequest();
        	DetachVolumeRequest dvr1 = new DetachVolumeRequest();
        	dvr.setVolumeId(createdVolumeId);
        	dvr1.setVolumeId(createdVolumeId1);
        	dvr.setInstanceId(myinstanceID);
        	dvr1.setInstanceId(myinstanceID1);
        	dvr.setDevice("/dev/sda2");
        	dvr1.setDevice("/dev/sda2");
        	ec2.detachVolume(dvr);
        	ec2.detachVolume(dvr1);
        	System.out.println("\n#2.4 Detach the volume");  
        	
        	Thread.sleep(1000*1*60);
         
        	/*********************************************
             *  #2.5 Create new AMI for idle instance
             *********************************************/
        	String[] idleAMIID = new String[2];
        	int j=0;
        	for(j=0;j<idleInstance.length;j++)
        	{
        		 CreateImageRequest Im=new CreateImageRequest(idleInstance[j], "image"+j);
        	
        	//CreateImageRequest Im1=new CreateImageRequest(myinstanceID1, "image1");
        	     Im.setInstanceId(idleInstance[j]);
        	//Im1.setInstanceId(myinstanceID1);
              
        	      CreateImageResult myAMI= ec2.createImage(Im); 
        	      idleAMIID[j]=myAMI.getImageId();
        	      
        	//CreateImageResult myAMI1= ec2.createImage(Im1); 
        	      System.out.println("\n#2.5 Create new AMI"); 
        	}
        	       Thread.sleep(1000*1*60);
        	       /*********************************************
                    * 
                    *  # Terminate an Instance
                    *  
                    *********************************************/
                   //System.out.println("#8 Terminate the Instance");
            
                  // TerminateInstancesRequest tir = new TerminateInstancesRequest(instanceIds);
                   
                   //ec2.terminateInstances(tir);
        	/*********************************************
             *  #2.6 Create new VMs
             *********************************************/
        RunInstancesRequest runNewInstancesRequest = new RunInstancesRequest();
        int m;	String[] newCreatedInstanceId = new String[2];
        for(m=0;m<j;m++)//j is the number of AMI created
        {
			 runNewInstancesRequest.withImageId(idleAMIID[m])
			                     .withInstanceType("t1.micro")
			                     .withMinCount(1)
			                     .withMaxCount(1)
			                     .withKeyName("E3instance_key")
			                     .withSecurityGroups("mini");
             RunInstancesResult runNewInstancesResult =  ec2.runInstances(runNewInstancesRequest);
             List<Instance> newResultInstance = runNewInstancesResult.getReservation().getInstances();
	            
             String newInstanceId = null; 
              	             
             for (Instance ins : newResultInstance)
                  			 {
              	            	newInstanceId = ins.getInstanceId();  
                  			 }
             newCreatedInstanceId[m]=newInstanceId;
             System.out.println("Using AMI, a new instance has been created: "+newCreatedInstanceId[m]);
              	              
        }
              Thread.sleep(1000*1*60);	
        //System.out.println("\n#2.6 Create "+ m + " instance using AMI");
        	
        	/*********************************************
             *  #2.7 Attach the volume to the new instance
             *********************************************/
        	int n;
        	for(n=0;n<idleInstance.length;n++)	
        	{
         	    AttachVolumeRequest new_avr = new AttachVolumeRequest();
         	//AttachVolumeRequest new_avr1 = new AttachVolumeRequest();
         	    new_avr.setInstanceId(newCreatedInstanceId[n]);
         	//avr1.setInstanceId(myinstanceID1);
         	    new_avr.setVolumeId(volumeID[n]);
         	//avr1.setVolumeId(createdVolumeId1);
         	    new_avr.setDevice("/dev/sda2");
         	//avr1.setDevice("/dev/sda2");
         	//avr.setVolumeId(createdVolumeId);
         	//avr.setInstanceId(createdInstanceId);
         	//avr.setDevice("/dev/sdf");
         	    ec2.attachVolume(new_avr);
         	//ec2.attachVolume(avr1);
         	    System.out.println("\n#2.7 Re-attach the volume");
         	    System.out.println("EBS volume has been attached and the volume ID is: "+volumeID[n]);
         	//System.out.println("EBS volume has been attached and the volume ID is: "+createdVolumeId1);

         	
         	Thread.sleep(1000*1*60);
        	}
            /************************************************
            *    #3 S3 bucket and object
            ***************************************************/
            s3  = new AmazonS3Client(credentials);
            
            //create bucket
            String bucketName = "lucinda.duan";
            s3.createBucket(bucketName);
            
            //set key
            String key = "object-name.txt";
            
            //set value
            File file = File.createTempFile("temp", ".txt");
            file.deleteOnExit();
            Writer writer = new OutputStreamWriter(new FileOutputStream(file));
            writer.write("This is a sample sentence.\r\nYes!");
            writer.close();
            
            //put object - bucket, key, value(file)
            s3.putObject(new PutObjectRequest(bucketName, key, file));
            
            //get object
            S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
            BufferedReader reader = new BufferedReader(
            	    new InputStreamReader(object.getObjectContent()));
            String data = null;
            while ((data = reader.readLine()) != null) {
                System.out.println(data);
            }
            
            
            
            /*********************************************
             *  #4 shutdown client object
             *********************************************/
           // ec2.shutdown();
           // s3.shutdown();

            
			 } catch (AmazonServiceException ase) 
			{
                System.out.println("Caught Exception: " + ase.getMessage());
                System.out.println("Reponse Status Code: " + ase.getStatusCode());
                System.out.println("Error Code: " + ase.getErrorCode());
                System.out.println("Request ID: " + ase.getRequestId());
			}

         }
}