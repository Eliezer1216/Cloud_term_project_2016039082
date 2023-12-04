package aws;

/*
 * Cloud Computing
 *
 * Dynamic Resource Management Tool
 * using AWS Java SDK Library
 *
 */
// Cloud_Computing_project_2016039082
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.costexplorer.AWSCostExplorer;
import com.amazonaws.services.costexplorer.AWSCostExplorerClientBuilder;
import com.amazonaws.services.costexplorer.model.*;
import com.jcraft.jsch.*;

import java.io.*;
import java.util.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;



public class awsTest {

    static AmazonEC2 ec2;

    private static void init() throws Exception {

        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        ec2 = AmazonEC2ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion("us-east-2")	/* check the region at AWS console */
                .build();
    }

    public static void main(String[] args) throws Exception {

        init();

        Scanner menu = new Scanner(System.in);
        Scanner id_string = new Scanner(System.in);
        int number = 0;

        while(true)
        {
            System.out.println("                                                            ");
            System.out.println("                                                            ");
            System.out.println("------------------------------------------------------------");
            System.out.println("           Amazon AWS Control Panel using SDK               ");
            System.out.println("------------------------------------------------------------");
            System.out.println("  1. list instance                2. available zones        ");
            System.out.println("  3. start instance               4. available regions      ");
            System.out.println("  5. stop instance                6. create instance        ");
            System.out.println("  7. reboot instance              8. list images            ");
            System.out.println("  9. condor_status               99. quit                   ");
            System.out.println("                                 10. terminate instance     ");
            System.out.println(" 11. describe instance           12. start monitoring       ");
            System.out.println(" 13. stop monitoring             14. Find Running Instance  ");
            System.out.println(" 15. show the bill               16. copy file to EC2       ");
            System.out.println(" 17. ready for condor_submit     18.condor_submit           ");
            System.out.println(" 19. condor_q                                               ");
            System.out.println("------------------------------------------------------------");

            System.out.print("Enter an integer: ");

            if(menu.hasNextInt()){
                number = menu.nextInt();
            }else {
                System.out.println("concentration!");
                break;
            }


            String instance_id = "";
            String jds_file_path="";
            switch(number) {
                case 1:
                    listInstances();
                    break;

                case 2:
                    availableZones();
                    break;

                case 3:
                    System.out.print("Enter instance id: ");
                    if(id_string.hasNext())
                        instance_id = id_string.nextLine();

                    if(!instance_id.isBlank())
                        startInstance(instance_id);
                    break;

                case 4:
                    availableRegions();
                    break;

                case 5:
                    FindRunningInstances(ec2);
                    System.out.print("\n\nEnter instance id: ");
                    if(id_string.hasNext())
                        instance_id = id_string.nextLine();

                    if(!instance_id.isBlank())
                        stopInstance(instance_id);
                    break;

                case 6:
                    System.out.print("Enter ami id: ");
                    String ami_id = "";

                    if(id_string.hasNext())
                        ami_id = id_string.nextLine();

                    if(!ami_id.isBlank())
                    {
                        System.out.println("How many instances will you create?: ");
                        String num="";
                        if(id_string.hasNext())
                        {
                            num=id_string.nextLine();
                        }
                        if(!num.isBlank())
                            createInstance(ami_id,num);
                    }
                    break;

                case 7:
                    System.out.print("Enter instance id: ");
                    if(id_string.hasNext())
                        instance_id = id_string.nextLine();

                    if(!instance_id.isBlank())
                        rebootInstance(instance_id);
                    break;

                case 8:
                    listImages();
                    break;
                case 9:
                    condor_status();
                    break;
                case 99:
                    System.out.println("bye!");
                    menu.close();
                    id_string.close();
                    return;
                case 10:
                    listInstances();
                    System.out.print("Enter instance id: ");
                    if(id_string.hasNext())
                        instance_id = id_string.nextLine();

                    if(!instance_id.isBlank())
                        terminateInstances(instance_id);
                    break;
                case 11:
                    decribeInstances();
                    break;
                case 12:
                    listInstances();
                    System.out.print("\nEnter instance id: ");
                    if(id_string.hasNext())
                        instance_id = id_string.nextLine();

                    if(!instance_id.isBlank())
                        monitorInstances(instance_id);
                    break;
                case 13:
                    stopMonitoring();
                    break;
                case 14:
                    FindRunningInstances(ec2);
                    break;
                case 15:
                    System.out.println("Please enter the date correctly\nex) 2023-11-01");
                    System.out.print("\nStart date: ");
                    String Start_date = "";

                    if(id_string.hasNext())
                        Start_date = id_string.nextLine();

                    if(!Start_date.isBlank())
                    {
                        System.out.println("End date: ");
                        String End_date="";
                        if(id_string.hasNext())
                        {
                            End_date=id_string.nextLine();
                        }
                        if(!End_date.isBlank())
                            showthebill(Start_date,End_date);
                    }
                    break;
                case 16:
                    copyFileToEc2();
                    break;
                case 17:
                    System.out.println("Please enter the txt file path\n(This file will change to shell script)");
                    System.out.print("\nFile Path: ");
                    String File_Path1 = "";

                    if(id_string.hasNext())
                        File_Path1 = id_string.nextLine();

                    if(!File_Path1.isBlank())
                    {
                        System.out.println("Please enter the txt file path\n(This file will change to .jds File");
                        System.out.println("File Path: ");
                        String File_Path2="";
                        if(id_string.hasNext())
                        {
                            File_Path2=id_string.nextLine();
                        }
                        if(!File_Path2.isBlank())
                            ready_for_condor_submit(File_Path1,File_Path2);
                    }
                    break;
                case 18:
                    System.out.print("\nEnter jds File Path: ");
                    if(id_string.hasNext())
                        jds_file_path = id_string.nextLine();

                    if(!jds_file_path.isBlank())
                    condor_submit(jds_file_path);
                    break;
                case 19:
                    condor_q();
                    break;
                default: System.out.println("concentration!");
            }

        }

    }

    public static void listInstances() {

        System.out.println("Listing instances....");
        boolean done = false;

        DescribeInstancesRequest request = new DescribeInstancesRequest();

        while(!done) {
            DescribeInstancesResult response = ec2.describeInstances(request);

            for(Reservation reservation : response.getReservations()) {
                for(Instance instance : reservation.getInstances()) {
                    System.out.printf(
                            "[id] %s, " +
                                    "[AMI] %s, " +
                                    "[type] %s, " +
                                    "[state] %10s, " +
                                    "[monitoring state] %s",
                            instance.getInstanceId(),
                            instance.getImageId(),
                            instance.getInstanceType(),
                            instance.getState().getName(),
                            instance.getMonitoring().getState());
                }
                System.out.println();
            }

            request.setNextToken(response.getNextToken());

            if(response.getNextToken() == null) {
                done = true;
            }
        }
    }

    public static void availableZones()	{

        System.out.println("Available zones....");
        try {
            DescribeAvailabilityZonesResult availabilityZonesResult = ec2.describeAvailabilityZones();
            Iterator <AvailabilityZone> iterator = availabilityZonesResult.getAvailabilityZones().iterator();

            AvailabilityZone zone;
            while(iterator.hasNext()) {
                zone = iterator.next();
                System.out.printf("[id] %s,  [region] %15s, [zone] %15s\n", zone.getZoneId(), zone.getRegionName(), zone.getZoneName());
            }
            System.out.println("You have access to " + availabilityZonesResult.getAvailabilityZones().size() +
                    " Availability Zones.");

        } catch (AmazonServiceException ase) {
            System.out.println("Caught Exception: " + ase.getMessage());
            System.out.println("Reponse Status Code: " + ase.getStatusCode());
            System.out.println("Error Code: " + ase.getErrorCode());
            System.out.println("Request ID: " + ase.getRequestId());
        }

    }

    public static void startInstance(String instance_id)
    {

        System.out.printf("Starting .... %s\n", instance_id);
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        DryRunSupportedRequest<StartInstancesRequest> dry_request =
                () -> {
                    StartInstancesRequest request = new StartInstancesRequest()
                            .withInstanceIds(instance_id);

                    return request.getDryRunRequest();
                };

        StartInstancesRequest request = new StartInstancesRequest()
                .withInstanceIds(instance_id);

        ec2.startInstances(request);

        System.out.printf("Successfully started instance %s", instance_id);
    }


    public static void availableRegions() {

        System.out.println("Available regions ....");

        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        DescribeRegionsResult regions_response = ec2.describeRegions();

        for(Region region : regions_response.getRegions()) {
            System.out.printf(
                    "[region] %15s, " +
                            "[endpoint] %s\n",
                    region.getRegionName(),
                    region.getEndpoint());
        }
    }

    public static void stopInstance(String instance_id) {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        DryRunSupportedRequest<StopInstancesRequest> dry_request =
                () -> {
                    StopInstancesRequest request = new StopInstancesRequest()
                            .withInstanceIds(instance_id);

                    return request.getDryRunRequest();
                };

        try {
            StopInstancesRequest request = new StopInstancesRequest()
                    .withInstanceIds(instance_id);

            ec2.stopInstances(request);
            System.out.printf("Successfully stop instance %s\n", instance_id);

        } catch(Exception e)
        {
            System.out.println("Exception: "+e.toString());
        }

    }

    public static void createInstance(String ami_id, String num) {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        int numInstances = Integer.parseInt(num);
        RunInstancesRequest run_request = new RunInstancesRequest()
                .withImageId(ami_id)
                .withInstanceType(InstanceType.T2Micro)
                .withMaxCount(numInstances)
                .withMinCount(numInstances)
                .withSecurityGroupIds("sg-0e61735b2344da4d9");

        RunInstancesResult run_response = ec2.runInstances(run_request);

        for (Instance instance : run_response.getReservation().getInstances()) {
            System.out.printf(
                    "Successfully started EC2 instance %s based on AMI %s\n",
                    instance.getInstanceId(), ami_id);
        }
    }

    public static void rebootInstance(String instance_id) {

        System.out.printf("Rebooting .... %s\n", instance_id);

        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        try {
            RebootInstancesRequest request = new RebootInstancesRequest()
                    .withInstanceIds(instance_id);

            RebootInstancesResult response = ec2.rebootInstances(request);

            System.out.printf(
                    "Successfully rebooted instance %s", instance_id);

        } catch(Exception e)
        {
            System.out.println("Exception: "+e.toString());
        }


    }

    public static void listImages() {
        System.out.println("Listing images....");

        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        DescribeImagesRequest request = new DescribeImagesRequest();
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

        request.getFilters().add(new Filter().withName("name").withValues("Shinhyo_slave_cloud"));
        request.setRequestCredentialsProvider(credentialsProvider);

        DescribeImagesResult results = ec2.describeImages(request);

        for(Image images :results.getImages()){
            System.out.printf("[ImageID] %s, [Name] %s, [Owner] %s\n",
                    images.getImageId(), images.getName(), images.getOwnerId());
        }

    }
    public static void terminateInstances(String instance_id)
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        DryRunSupportedRequest<TerminateInstancesRequest> dry_request =
                () -> {
                    TerminateInstancesRequest request = new TerminateInstancesRequest()
                            .withInstanceIds(instance_id);

                    return request.getDryRunRequest();
                };

        try {
            TerminateInstancesRequest request = new TerminateInstancesRequest()
                    .withInstanceIds(instance_id);

            ec2.terminateInstances(request);
            System.out.printf("Successfully terminate instance %s\n", instance_id);

        } catch(Exception e)
        {
            System.out.println("Exception: "+e.toString());
        }
    }

    public static void monitorInstances(String instance_id)
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        DryRunSupportedRequest<MonitorInstancesRequest> dry_request =
                () -> {
                    MonitorInstancesRequest request = new MonitorInstancesRequest()
                            .withInstanceIds(instance_id);

                    return request.getDryRunRequest();
                };

        try {
            MonitorInstancesRequest request = new MonitorInstancesRequest()
                    .withInstanceIds(instance_id);

            ec2.monitorInstances(request);
            System.out.printf("Successfully monitoring instance %s\n", instance_id);

        } catch(Exception e)
        {
            System.out.println("Exception: "+e.toString());
        }
    }

    public static void decribeInstances()
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        boolean done = false;

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        while(!done) {
            DescribeInstancesResult response = ec2.describeInstances(request);

            for(Reservation reservation : response.getReservations()) {
                for(Instance instance : reservation.getInstances()) {
                    System.out.printf(
                            "\nFound instance with id : %s\n" +
                                    "AMI: %s\n" +
                                    "type: %s\n" +
                                    "state: %s\n" +
                                    "monitoring state:%s\n",
                            instance.getInstanceId(),
                            instance.getImageId(),
                            instance.getInstanceType(),
                            instance.getState().getName(),
                            instance.getMonitoring().getState());
                }
            }

            request.setNextToken(response.getNextToken());

            if(response.getNextToken() == null) {
                done = true;
            }
        }
    }

    public static void stopMonitoring()
    {
        Scanner id_string = new Scanner(System.in);
        String instance_id = "";
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        boolean done = false;

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        while(!done) {
            DescribeInstancesResult response = ec2.describeInstances(request);

            for(Reservation reservation : response.getReservations()) {
                for(Instance instance : reservation.getInstances()) {

                    if(instance.getMonitoring().getState().equals("enabled")==true)
                    {
                        System.out.println("\n-------------Current Monitoring Instances------------");
                        System.out.printf(
                                "#Instance id : %s\n" ,
                                instance.getInstanceId());
                    }
                }
            }

            request.setNextToken(response.getNextToken());

            if(response.getNextToken() == null) {
                done = true;
            }
        }
        System.out.print("\nEnter instance id for stop Monitoring: ");
        if(id_string.hasNext())
            instance_id = id_string.nextLine();
        if(!instance_id.isBlank())
        {
            String finalInstance_id = instance_id;
            DryRunSupportedRequest<UnmonitorInstancesRequest> dry_request =
                    () -> {
                        UnmonitorInstancesRequest unmonitor_request = new UnmonitorInstancesRequest()
                                .withInstanceIds(finalInstance_id);

                        return unmonitor_request.getDryRunRequest();
                    };

            try {
                UnmonitorInstancesRequest unmonitor_request= new UnmonitorInstancesRequest()
                        .withInstanceIds(instance_id);

                ec2.unmonitorInstances(unmonitor_request);
                System.out.printf("Successfully stop monitoring instance: %s\n", instance_id);

            } catch(Exception e)
            {
                System.out.println("Exception: "+e.toString());
            }
        }

    }

    public static void FindRunningInstances(AmazonEC2 ec2) {
        try {
            // Create the Filter to use to find running instances
            Filter filter = new Filter("instance-state-name");
            filter.withValues("running");

            // Create a DescribeInstancesRequest
            DescribeInstancesRequest request = new DescribeInstancesRequest();
            request.withFilters(filter);

            // Find the running instances
            DescribeInstancesResult response = ec2.describeInstances(request);

            boolean foundInstances = false;
            int count = 1; // 인덱스를 세기 위한 변수
            System.out.println("\n-----------Running instance Id-----------");
            for (Reservation reservation : response.getReservations()) {
                for (Instance instance : reservation.getInstances()) {
                    System.out.printf("#%d %s\n", count, instance.getInstanceId());
                    count++; // 다음 인덱스로 증가
                    foundInstances = true;
                }
            }
            if(!foundInstances)
            {
                System.out.println("There is no running instances");
            }
            if(foundInstances)
            {
                System.out.print("End");
            }

        } catch (SdkClientException e) {
            e.getStackTrace();
        }
    }
    public static void condor_status()
    {
        String command = "condor_status";
        ConnectToEc2(command);
    }
    public static void condor_q()
    {
        String command = "condor_q";
        ConnectToEc2(command);
    }
    public static void condor_submit(String jds_file_path)
    {
        String command="condor_submit -allow-crlf-script"+" "+jds_file_path;
        ConnectToEc2(command);
    }

    public static void showthebill(String Start_date , String End_date) {
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        AWSCostExplorer costExplorer = AWSCostExplorerClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion("us-east-2")
                .build();

        GetCostAndUsageRequest request = new GetCostAndUsageRequest()
                .withTimePeriod(new DateInterval().withStart(Start_date).withEnd(End_date))
                .withGranularity("MONTHLY")
                .withMetrics("BlendedCost");

        GetCostAndUsageResult result = costExplorer.getCostAndUsage(request);
        System.out.println("\n--------------월별 청구서--------------");
        for (ResultByTime resultByTime : result.getResultsByTime()) {
            System.out.println("\n기간: " + resultByTime.getTimePeriod());
            System.out.println("총 비용: " + resultByTime.getTotal().get("BlendedCost"));
        }
    }

    public static void copyFileToEc2()
    {
        String user = "ec2-user";
        String host = "13.58.106.251";
        int port = 22;
        String privateKeyPath = "C:\\Users\\tlsgy\\OneDrive\\바탕 화면\\학기 공부\\project\\cloud\\Cloud_2016039082.pem";
        String localFilePath = "C:\\Users\\tlsgy\\OneDrive\\바탕 화면\\학기 공부\\project\\cloud\\test.txt"; // 로컬 파일 경로
        String remoteFilePath = "/home/ec2-user/test/"; // EC2 내 파일 저장 경로

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            // SSH 세션 열기
            jsch.addIdentity(privateKeyPath);
            session = jsch.getSession(user, host, 22);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            // SFTP 채널 열기
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // 로컬 파일을 EC2로 전송
            File localFile = new File(localFilePath);
            String remoteFileName = localFile.getName();
            channelSftp.put(new FileInputStream(localFile), remoteFilePath + "/" + remoteFileName);

            System.out.println("파일 업로드 완료");
        } catch (JSchException | SftpException | java.io.FileNotFoundException e) {
            System.err.println("파일 업로드 중 오류 발생: " + e.getMessage());
        } finally {
            // 연결 종료
            if (channelSftp != null) {
                channelSftp.exit();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    public static void ConnectToEc2(String command)
    {
        String host = "13.58.106.251";
        String user = "ec2-user";
        String privateKey = "C:\\Users\\tlsgy\\OneDrive\\바탕 화면\\학기 공부\\project\\cloud\\Cloud_2016039082.pem";
        int port = 22; // SSH 포트 번호

        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKey);

            Session session = jsch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();

            // 결과 출력
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 연결 종료
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void ready_for_condor_submit(String File_Path1 , String File_Path2)
    {
        String user = "ec2-user";
        String host = "13.58.106.251";
        int port = 22;
        String privateKeyPath = "C:\\Users\\tlsgy\\OneDrive\\바탕 화면\\학기 공부\\project\\cloud\\Cloud_2016039082.pem";
        String localFilePath1 = File_Path1; // 로컬 파일 경로 ( shell script로 바뀔 )
        String localFilePath2 = File_Path2; // 로컬 파일 경로 ( jds파일로 바뀔 )
        String remoteFilePath = "/home/ec2-user/"; // EC2 내 파일 저장 경로

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            // SSH 세션 열기
            jsch.addIdentity(privateKeyPath);
            session = jsch.getSession(user, host, 22);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            // SFTP 채널 열기
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // 로컬 파일을 EC2로 전송
            File localFile = new File(localFilePath1);
            String remoteFileName1 = localFile.getName();
            int lastIndex = remoteFileName1.lastIndexOf('.');
            String fileNameWithoutExtension1 = remoteFileName1.substring(0, lastIndex);
            channelSftp.put(new FileInputStream(localFile), remoteFilePath + "/" + remoteFileName1);

            System.out.println("파일 업로드 완료");

            // EC2 내 파일을 쉘 스크립트로 변환
            convertToShellScript(session, remoteFilePath, remoteFileName1,fileNameWithoutExtension1);

        } catch (JSchException | SftpException | IOException e) {
            System.err.println("파일 업로드 중 오류 발생: " + e.getMessage());
        }
        try {

            // SFTP 채널 열기
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // 로컬 파일을 EC2로 전송
            File localFile2 = new File(localFilePath2);
            String remoteFileName2 = localFile2.getName();
            int lastIndex = remoteFileName2.lastIndexOf('.');
            String fileNameWithoutExtension2 = remoteFileName2.substring(0, lastIndex);
            channelSftp.put(new FileInputStream(localFile2), remoteFilePath + "/" + remoteFileName2);

            System.out.println("파일 업로드 완료");

            // EC2 내 파일을 .jds 파일로 변환
            convertToJDS(session, remoteFilePath, remoteFileName2,fileNameWithoutExtension2);

        } catch (JSchException | SftpException | IOException e) {
            System.err.println("파일 업로드 중 오류 발생: " + e.getMessage());
        } finally {
            // 연결 종료
            if (channelSftp != null) {
                channelSftp.exit();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }


    // EC2 내 파일을 쉘 스크립트로 변환하는 메서드
    public static void convertToShellScript(Session session, String remoteFilePath, String remoteFileName1,String fileNameWithoutExtension1) {
        try {
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            String command = "cat " + remoteFilePath + "/" + remoteFileName1 + " > " + remoteFilePath + "/" + fileNameWithoutExtension1 + ".sh; " +
                    "dos2unix " + remoteFilePath + "/" + fileNameWithoutExtension1 + ".sh; " +
                    "chmod 777 " + remoteFilePath + "/" + fileNameWithoutExtension1 + ".sh";
            channelExec.setCommand(command);
            channelExec.connect();
            channelExec.disconnect();
            System.out.println("파일을 쉘 스크립트로 변환 완료\n");
        } catch (JSchException e) {
            System.err.println("쉘 스크립트 변환 중 오류 발생: " + e.getMessage());
        }
    }



    // EC2 내 파일을 .jds 파일로 변환하는 메서드
    public static void convertToJDS(Session session, String remoteFilePath, String remoteFileName,String fileNameWithoutExtension2) {
        try {
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            String command = "mv " + remoteFilePath + "/" + remoteFileName + " " + remoteFilePath + "/"+fileNameWithoutExtension2+".jds";
            channelExec.setCommand(command);
            channelExec.connect();
            channelExec.disconnect();
            System.out.println("파일을 .jds로 변환 완료\n");
        } catch (JSchException e) {
            System.err.println(".jds 파일로 변환 중 오류 발생: " + e.getMessage());
        }
    }
}

