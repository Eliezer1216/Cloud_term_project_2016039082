package aws;

/*
 * Cloud Computing
 *
 * Dynamic Resource Management Tool
 * using AWS Java SDK Library
 * Cloud_Computing_project_2016039082_
 */
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


// Cloud_Computing_2016039082
public class awsTest {

    static AmazonEC2 ec2;

    private static void init() throws Exception {
        //AWS 자격증명
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
        //AWS 자격 증명을 바탕으로 AmazonEC2Client 생성
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
            System.out.println("           ( 2016039082_ hyomin , Shin )                    ");
            System.out.println("------------------------------------------------------------");
            System.out.println("  1. list instance                2. available zones        ");
            System.out.println("  3. start instance               4. available regions      ");
            System.out.println("  5. stop instance                6. create instance        ");
            System.out.println("  7. reboot instance              8. list images            ");
            System.out.println("  9. condor_status               10. terminate instance     ");
            System.out.println(" 11. describe instance           12. start monitoring       ");
            System.out.println(" 13. stop monitoring             14. Find Running Instance  ");
            System.out.println(" 15. show the bill               16. copy file to Master EC2");
            System.out.println(" 17. ready for condor_submit     18. condor_submit          ");
            System.out.println(" 19. condor_q                    20. create_image           ");
            System.out.println(" 21. all_start_instances         22. all_stop_instances     ");
            System.out.println("                                 23. quit                   ");
            System.out.println("------------------------------------------------------------");

            System.out.print("Enter an integer: ");

            if(menu.hasNextInt()){
                number = menu.nextInt();
            }else {
                System.out.println("concentration!");
                break;
            }


            String instance_id = ""; // 인스턴스 Id
            String jds_file_path=""; // jds 파일 경로
            String file_path=""; // 로컬 파일 경로
            switch(number) {

                // 1. list instance
                case 1:
                    listInstances();
                    break;

                // 2. available zones
                case 2:
                    availableZones();
                    break;

                // 3. start instance
                case 3:
                    System.out.print("Enter instance id: ");
                    if(id_string.hasNext())
                        instance_id = id_string.nextLine();

                    if(!instance_id.isBlank())
                        startInstance(instance_id);
                    break;

                // 4. available regions
                case 4:
                    availableRegions();
                    break;

                // 5. stop instance
                case 5:
                    FindRunningInstances(ec2);
                    System.out.print("\n\nEnter instance id: ");
                    if(id_string.hasNext())
                        instance_id = id_string.nextLine();

                    if(!instance_id.isBlank())
                        stopInstance(instance_id);
                    break;

                // 6. create instance
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

                // 7. reboot instance
                case 7:
                    System.out.print("Enter instance id: ");
                    if(id_string.hasNext())
                        instance_id = id_string.nextLine();

                    if(!instance_id.isBlank())
                        rebootInstance(instance_id);
                    break;

                // 8. list images
                case 8:
                    listImages();
                    break;

                // 9. condor_status
                case 9:
                    condor_status();
                    break;

                // 10. terminate instance
                case 10:
                    listInstances();
                    System.out.print("Enter instance id: ");
                    if(id_string.hasNext())
                        instance_id = id_string.nextLine();

                    if(!instance_id.isBlank())
                        terminateInstances(instance_id);
                    break;

                // 11. describe instance
                case 11:
                    decribeInstances();
                    break;

                // 12. start monitoring
                case 12:
                    listInstances();
                    System.out.print("\nEnter instance id: ");
                    if(id_string.hasNext())
                        instance_id = id_string.nextLine();

                    if(!instance_id.isBlank())
                        monitorInstances(instance_id);
                    break;

                // 13. stop monitoring
                case 13:
                    stopMonitoring();
                    break;

                // 14. Find Running instance
                case 14:
                    FindRunningInstances(ec2);
                    break;

                // 15. show the bill ( 월별로 청구서 보여주기 )
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

                // 16. copy file to Master EC2 ( 마스터 ec2에 파일 보내기 )
                case 16:
                    System.out.print("\nEnter local file path: ");
                    if(id_string.hasNext())
                        file_path = id_string.nextLine();

                    if(!file_path.isBlank())
                        copyFileToEc2(file_path);

                    break;

                // 17. ready for condor_submit ( txt 파일 1 -> shell script 파일 , txt 파일 2 -> jds 파일로 변경 )
                case 17:
                    System.out.println("Please enter the txt file path\n(This file will change to shell script)");
                    System.out.print("File Path: ");
                    String File_Path1 = "";

                    if(id_string.hasNext())
                        File_Path1 = id_string.nextLine();

                    if(!File_Path1.isBlank())
                    {
                        System.out.println("\nPlease enter the txt file path\n(This file will change to .jds File)");
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

                // 18. condor_submit
                case 18:
                    System.out.print("\nEnter jds File Path: ");
                    if(id_string.hasNext())
                        jds_file_path = id_string.nextLine();

                    if(!jds_file_path.isBlank())
                    condor_submit(jds_file_path);
                    break;

                // 19. condor_q() 명령어에 대한 결과 출력
                case 19:
                    condor_q();
                    break;

                // 20. create_image()
                case 20:
                    System.out.print("Enter instance id: ");
                    String instanceId = "";

                    if(id_string.hasNext())
                        instanceId = id_string.nextLine();

                    if(!instanceId.isBlank())
                    {
                        System.out.print("Enter image description: ");
                        String image_description="";
                        if(id_string.hasNext())
                        {
                           image_description=id_string.nextLine();
                        }
                        if(!image_description.isBlank())
                        {
                            String image_name="";
                            System.out.print("Enter image name: ");
                            if(id_string.hasNext())
                            {
                                image_name=id_string.nextLine();
                            }
                            if(!image_name.isBlank())
                            {
                                createImage(instanceId,image_description,image_name );
                            }
                        }
                    }
                    break;

                // 21. all_start_instances
                case 21:
                    all_instances_start();
                    break;

                // 22. all_stop_instances
                case 22:
                    all_instances_stop();
                    break;

                // 23. quit
                case 23:
                    System.out.println("bye!");
                    menu.close();
                    id_string.close();
                    return;
                
                // 숫자가 아닌 이상한 거 입력하면 출력
                default: System.out.println("concentration!");
            }

        }

    }

    // 1. list instance 구현
    public static void listInstances() {

        System.out.println("Listing instances....");

        boolean done = false;
        DescribeInstancesRequest request = new DescribeInstancesRequest();

        // 모든 인스턴스를 나열하기 위해 반복문 사용
        while(!done) {
            // EC2 서비스에 대한 DescribeInstancesRequest를 보냄
            DescribeInstancesResult response = ec2.describeInstances(request);

            // DescribeInstancesResult로부터 받은 응답에서 인스턴스 정보를 반복하여 가져옴
            for(Reservation reservation : response.getReservations()) {
                for(Instance instance : reservation.getInstances()) {
                    // 각 인스턴스의 ID, AMI, 유형, 상태, 모니터링 상태 출력
                    System.out.printf(
                            "[id] %s, " +
                                    "[AMI] %s, " +
                                    "[type] %s, " +
                                    "[state] %10s, " +
                                    "[monitoring state] %s%n",
                            instance.getInstanceId(),
                            instance.getImageId(),
                            instance.getInstanceType(),
                            instance.getState().getName(),
                            instance.getMonitoring().getState());
                }

            }

            // 페이징 처리를 위해 다음 페이지 토큰 설정
            request.setNextToken(response.getNextToken());

            // 다음 토큰이 없으면 반복문 종료
            if(response.getNextToken() == null) {
                done = true;
            }
        }
    }

    // 2. available zones
    public static void availableZones()	{

        System.out.println("Available zones....");

        try {
            // EC2 서비스를 사용하여 사용 가능한 가용 영역을 나열하는 DescribeAvailabilityZonesResult를 가져옴
            DescribeAvailabilityZonesResult availabilityZonesResult = ec2.describeAvailabilityZones();

            // Iterator를 사용하여 사용 가능한 각 가용 영역의 정보를 반복해서 가져옴
            Iterator<AvailabilityZone> iterator = availabilityZonesResult.getAvailabilityZones().iterator();

            AvailabilityZone zone;
            while(iterator.hasNext()) {
                zone = iterator.next();
                // 각 가용 영역의 ID, 리전 이름, 영역 이름을 출력
                System.out.printf("[id] %s,  [region] %15s, [zone] %15s\n", zone.getZoneId(), zone.getRegionName(), zone.getZoneName());
            }
            // 사용 가능한 가용 영역의 총 개수 출력
            System.out.println("You have access to " + availabilityZonesResult.getAvailabilityZones().size() +
                    " Availability Zones.");

        } catch (AmazonServiceException ase) {
            // AmazonServiceException이 발생한 경우 예외 처리
            System.out.println("Caught Exception: " + ase.getMessage());
            System.out.println("Reponse Status Code: " + ase.getStatusCode());
            System.out.println("Error Code: " + ase.getErrorCode());
            System.out.println("Request ID: " + ase.getRequestId());
        }

    }

    // 3. start instance
    public static void startInstance(String instance_id)
    {

        // Amazon EC2 클라이언트 생성
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // 인스턴스 시작 요청에 대한 Dry Run을 지원하는지 확인
        DryRunSupportedRequest<StartInstancesRequest> dry_request =
                () -> {
                    // 인스턴스 시작 요청 객체 생성
                    StartInstancesRequest request = new StartInstancesRequest()
                            .withInstanceIds(instance_id);

                    // Dry Run 요청 반환
                    return request.getDryRunRequest();
                };

        // 실제 인스턴스 시작 요청
        StartInstancesRequest request = new StartInstancesRequest()
                .withInstanceIds(instance_id);

        // EC2 인스턴스 시작
        ec2.startInstances(request);

        System.out.printf("Successfully started instance %s", instance_id);
    }

    // 4. available regions
    public static void availableRegions() {

        System.out.println("Available regions ....");

        // Amazon EC2 클라이언트 생성
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // 사용 가능한 모든 리전 정보를 가져옴
        DescribeRegionsResult regions_response = ec2.describeRegions();

        // 각 리전의 정보를 반복하여 가져와 출력
        for(Region region : regions_response.getRegions()) {
            // 각 리전의 이름과 엔드포인트 정보 출력
            System.out.printf(
                    "[region] %15s, " +
                            "[endpoint] %s\n",
                    region.getRegionName(),
                    region.getEndpoint());
        }
    }

    // 5. stop instance
    public static void stopInstance(String instance_id) {
        // Amazon EC2 클라이언트 생성
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // 인스턴스 중지 Dry Run을 지원하는지 확인
        DryRunSupportedRequest<StopInstancesRequest> dry_request =
                () -> {
                    // 중지 요청 객체 생성
                    StopInstancesRequest request = new StopInstancesRequest()
                            .withInstanceIds(instance_id);

                    // Dry Run 요청 반환
                    return request.getDryRunRequest();
                };

        try {
            // 실제 인스턴스 중지 요청
            StopInstancesRequest request = new StopInstancesRequest()
                    .withInstanceIds(instance_id);

            // EC2 인스턴스 중지
            ec2.stopInstances(request);

            // 인스턴스가 성공적으로 중지되었음을 출력
            System.out.printf("Successfully stop instance %s\n", instance_id);

        } catch(Exception e) {
            // 예외가 발생한 경우 예외 처리
            System.out.println("Exception: " + e.toString());
        }


    }

    // 6. create Instance
    public static void createInstance(String ami_id, String num) {
        // Amazon EC2 클라이언트 생성
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // 문자열을 정수로 변환하여 numInstances에 저장
        int numInstances = Integer.parseInt(num);

        // RunInstancesRequest를 생성하여 EC2 인스턴스 시작 요청 설정
        RunInstancesRequest run_request = new RunInstancesRequest()
                .withImageId(ami_id) // AMI ID 설정
                .withInstanceType(InstanceType.T2Micro) // 인스턴스 유형 설정
                .withMaxCount(numInstances) // 시작할 인스턴스의 최대 수량 설정
                .withMinCount(numInstances) // 시작할 인스턴스의 최소 수량 설정
                .withSecurityGroupIds("sg-0e61735b2344da4d9"); // 보안 그룹 설정

        // EC2 인스턴스 시작 요청 후 결과 받기
        RunInstancesResult run_response = ec2.runInstances(run_request);

        // 시작된 각 인스턴스에 대한 정보를 반복하여 가져와 출력
        for (Instance instance : run_response.getReservation().getInstances()) {
            // 각 인스턴스의 시작 성공 메시지 출력
            System.out.printf(
                    "Successfully started EC2 instance %s based on AMI %s\n",
                    instance.getInstanceId(), ami_id);
        }

    }

    // 7. reboot instance
    public static void rebootInstance(String instance_id) {

        // 재부팅 메시지 출력
        System.out.printf("Rebooting .... %s\n", instance_id);

        // Amazon EC2 클라이언트 생성
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        try {
            // 인스턴스 재부팅을 위한 RebootInstancesRequest 생성
            RebootInstancesRequest request = new RebootInstancesRequest()
                    .withInstanceIds(instance_id);

            // EC2 인스턴스 재부팅 요청
            RebootInstancesResult response = ec2.rebootInstances(request);

            // 성공적으로 재부팅된 인스턴스 메시지 출력
            System.out.printf(
                    "Successfully rebooted instance %s", instance_id);

        } catch(Exception e) {
            // 예외가 발생한 경우 예외 처리
            System.out.println("Exception: " + e.toString());
        }
    }

    // 8. list images
    public static void listImages() {
        // 이미지 목록을 나열하는 메시지 출력
        System.out.println("Listing images....");

        // Amazon EC2 클라이언트 생성
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // DescribeImagesRequest 객체 생성
        DescribeImagesRequest request = new DescribeImagesRequest();

        // 프로필 자격 증명 공급자(ProfileCredentialsProvider)를 생성하여 자격 증명 설정
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

        // 요청에 필터 추가: 이름이 "Shinhyo_slave_cloud"인 이미지 검색
        request.getFilters().add(new Filter().withName("name").withValues("Shinhyo_slave_cloud"));

        // 요청에 자격 증명 설정
        request.setRequestCredentialsProvider(credentialsProvider);

        // 이미지 조회 요청 및 결과 받기
        DescribeImagesResult results = ec2.describeImages(request);

        // 조회된 각 이미지에 대한 정보를 반복하여 출력
        for (Image images : results.getImages()) {
            // 이미지 ID, 이름, 소유자 출력
            System.out.printf("[ImageID] %s, [Name] %s, [Owner] %s\n",
                    images.getImageId(), images.getName(), images.getOwnerId());
        }
    }

    // 9. condor_status
    public static void condor_status()
    {
        String command = "condor_status";
        ConnectToEc2(command);
    }

    // 10. terminate instance
    public static void terminateInstances(String instance_id)
    {
        // Amazon EC2 클라이언트 생성
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // 인스턴스 종료 Dry Run을 지원하는지 확인
        DryRunSupportedRequest<TerminateInstancesRequest> dry_request =
                () -> {
                    // 인스턴스 종료 요청 객체 생성
                    TerminateInstancesRequest request = new TerminateInstancesRequest()
                            .withInstanceIds(instance_id);

                    // Dry Run 요청 반환
                    return request.getDryRunRequest();
                };

        try {
            // 실제 인스턴스 종료 요청
            TerminateInstancesRequest request = new TerminateInstancesRequest()
                    .withInstanceIds(instance_id);

            // EC2 인스턴스 종료
            ec2.terminateInstances(request);

            // 성공적으로 종료된 인스턴스 메시지 출력
            System.out.printf("Successfully terminate instance %s\n", instance_id);

        } catch(Exception e) {
            // 예외가 발생한 경우 예외 처리
            System.out.println("Exception: " + e.toString());
        }

    }

    // 11. describe instance
    public static void decribeInstances()
    {
        // Amazon EC2 클라이언트 생성
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // done 변수를 사용하여 페이지네이션을 처리하기 위한 반복문
        boolean done = false;

        // DescribeInstancesRequest 객체 생성
        DescribeInstancesRequest request = new DescribeInstancesRequest();

        // 모든 페이지에 대해 실행되도록 반복문 설정
        while (!done) {
            // EC2 인스턴스 정보를 가져오기 위한 DescribeInstances 요청
            DescribeInstancesResult response = ec2.describeInstances(request);

            // 가져온 각 Reservation(예약)의 인스턴스 정보 출력
            for (Reservation reservation : response.getReservations()) {
                for (Instance instance : reservation.getInstances()) {
                    // 각 인스턴스의 상태 및 모니터링 정보 출력
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

            // 페이지네이션을 위한 토큰 설정
            request.setNextToken(response.getNextToken());

            // 마지막 페이지인 경우 반복문 종료
            if (response.getNextToken() == null) {
                done = true;
            }
        }

    }

    // 12. start monitoring
    public static void monitorInstances(String instance_id)
    {
        // Amazon EC2 클라이언트 생성
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // 인스턴스 모니터링 Dry Run을 지원하는지 확인
        DryRunSupportedRequest<MonitorInstancesRequest> dry_request =
                () -> {
                    // 인스턴스 모니터링 요청 객체 생성
                    MonitorInstancesRequest request = new MonitorInstancesRequest()
                            .withInstanceIds(instance_id);

                    // Dry Run 요청 반환
                    return request.getDryRunRequest();
                };

        try {
            // 실제 인스턴스 모니터링 요청
            MonitorInstancesRequest request = new MonitorInstancesRequest()
                    .withInstanceIds(instance_id);

            // EC2 인스턴스 모니터링 시작
            ec2.monitorInstances(request);

            // 성공적으로 모니터링된 인스턴스 메시지 출력
            System.out.printf("Successfully monitoring instance %s\n", instance_id);

        } catch(Exception e) {
            // 예외가 발생한 경우 예외 처리
            System.out.println("Exception: " + e.toString());
        }

    }


    // 13. stop monitoring
    public static void stopMonitoring()
    {
        // 사용자로부터 인스턴스 ID를 입력받기 위한 Scanner 객체 생성
        Scanner id_string = new Scanner(System.in);
        String instance_id = "";

        // Amazon EC2 클라이언트 생성
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // 모든 페이지에 대해 실행되도록 반복문 설정
        boolean done = false;
        DescribeInstancesRequest request = new DescribeInstancesRequest();

        // 현재 모니터링 중인 인스턴스를 조회하고 표시하는 반복문
        while (!done) {
            DescribeInstancesResult response = ec2.describeInstances(request);

            for (Reservation reservation : response.getReservations()) {
                for (Instance instance : reservation.getInstances()) {
                    // 모니터링이 활성화된 인스턴스 목록 출력
                    if (instance.getMonitoring().getState().equals("enabled")) {
                        System.out.println("\n-------------Current Monitoring Instances------------");
                        System.out.printf(
                                "#Instance id : %s\n",
                                instance.getInstanceId());
                    }
                }
            }

            request.setNextToken(response.getNextToken());

            if (response.getNextToken() == null) {
                done = true;
            }
        }

        // 사용자로부터 중지할 인스턴스 ID를 입력 받음
        System.out.print("\nEnter instance id for stop Monitoring: ");
        if (id_string.hasNext())
            instance_id = id_string.nextLine();

        // 입력된 인스턴스 ID가 비어있지 않은 경우 모니터링 중지를 시도함
        if (!instance_id.isBlank()) {
            // 입력된 인스턴스 ID로 UnmonitorInstancesRequest 생성
            String finalInstance_id = instance_id;
            DryRunSupportedRequest<UnmonitorInstancesRequest> dry_request =
                    () -> {
                        UnmonitorInstancesRequest unmonitor_request = new UnmonitorInstancesRequest()
                                .withInstanceIds(finalInstance_id);

                        return unmonitor_request.getDryRunRequest();
                    };

            try {
                // 실제 인스턴스의 모니터링 중지 요청 생성
                UnmonitorInstancesRequest unmonitor_request = new UnmonitorInstancesRequest()
                        .withInstanceIds(instance_id);

                // EC2 인스턴스의 모니터링 중지
                ec2.unmonitorInstances(unmonitor_request);
                System.out.printf("Successfully stop monitoring instance: %s\n", instance_id);

            } catch (Exception e) {
                // 예외가 발생한 경우 예외 처리
                System.out.println("Exception: " + e.toString());
            }
        }


    }

    // 14. Find Running Instance
    public static void FindRunningInstances(AmazonEC2 ec2) {
        try {
            // 실행 중인 인스턴스를 찾기 위한 필터 생성
            Filter filter = new Filter("instance-state-name");
            filter.withValues("running");

            // DescribeInstancesRequest 객체 생성
            DescribeInstancesRequest request = new DescribeInstancesRequest();
            request.withFilters(filter);

            // 실행 중인 인스턴스 조회
            DescribeInstancesResult response = ec2.describeInstances(request);

            boolean foundInstances = false; // 실행 중인 인스턴스가 있는지 확인하는 플래그
            int count = 1; // 인덱스를 위한 변수
            System.out.println("\n-----------Running instance Id-----------");

            // 조회된 실행 중인 인스턴스의 ID 출력
            for (Reservation reservation : response.getReservations()) {
                for (Instance instance : reservation.getInstances()) {
                    System.out.printf("#%d %s\n", count, instance.getInstanceId());
                    count++; // 인덱스 증가
                    foundInstances = true; // 실행 중인 인스턴스 발견
                }
            }

            // 실행 중인 인스턴스가 없는 경우 메시지 출력
            if (!foundInstances) {
                System.out.println("There are no running instances");
            }

            // 실행 중인 인스턴스가 있는 경우 "End" 출력
            if (foundInstances) {
                System.out.print("End");
            }
        } catch (SdkClientException e) {
            // SDK 클라이언트 예외 처리
            e.getStackTrace();
        }

    }

    // 15. show the bill
    public static void showthebill(String Start_date , String End_date) {
        // AWS 프로파일 정보를 제공하는 Credentials Provider 생성
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials(); // AWS 자격 증명을 가져옴
        } catch (Exception e) {
            // 자격 증명을 로드할 수 없는 경우 AmazonClientException을 던짐
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        // AWSCostExplorerClient 생성
        AWSCostExplorer costExplorer = AWSCostExplorerClientBuilder.standard()
                .withCredentials(credentialsProvider) // Credentials Provider 설정
                .withRegion("us-east-2") // 리전 설정
                .build();

        // GetCostAndUsageRequest 생성
        GetCostAndUsageRequest request = new GetCostAndUsageRequest()
                .withTimePeriod(new DateInterval().withStart(Start_date).withEnd(End_date)) // 기간 설정
                .withGranularity("MONTHLY") // 사용량의 간격 설정
                .withMetrics("BlendedCost"); // 비용 메트릭 지정

        // GetCostAndUsageRequest를 사용하여 비용 및 사용량 정보 가져오기
        GetCostAndUsageResult result = costExplorer.getCostAndUsage(request);

        // 월별 청구서 출력
        System.out.println("\n--------------월별 청구서--------------");
        for (ResultByTime resultByTime : result.getResultsByTime()) {
            System.out.println("\n기간: " + resultByTime.getTimePeriod()); // 기간 출력
            System.out.println("총 비용: " + resultByTime.getTotal().get("BlendedCost")); // 총 비용 출력
        }

    }

    // 16. copy file to Master EC2
    public static void copyFileToEc2(String localFilePath)
    {
        // 연결에 필요한 변수들을 설정합니다.
        String user = "ec2-user"; // EC2 인스턴스의 사용자 이름
        String host = "13.58.106.251"; // EC2 인스턴스의 IP 주소 또는 호스트 이름
        int port = 22; // SSH 포트 (기본값)
        String privateKeyPath = "C:\\Users\\tlsgy\\OneDrive\\바탕 화면\\학기 공부\\project\\cloud\\Cloud_2016039082.pem"; // 개인 키파일 경로
        String remoteFilePath = "/home/ec2-user/"; // EC2 내 파일 저장 경로

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            // SSH 세션 열기
            jsch.addIdentity(privateKeyPath); // 개인 키파일 추가
            session = jsch.getSession(user, host, port); // 세션 생성
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no"); // 호스트 키 체크 설정
            session.setConfig(config); // 세션 설정
            session.connect(); // 세션 연결

            // SFTP 채널 열기
            channelSftp = (ChannelSftp) session.openChannel("sftp"); // SFTP 채널 열기
            channelSftp.connect(); // 채널 연결

            // 로컬 파일을 EC2로 전송
            File localFile = new File(localFilePath); // 로컬 파일 객체 생성
            String remoteFileName = localFile.getName(); // 전송할 파일의 이름
            channelSftp.put(new FileInputStream(localFile), remoteFilePath + "/" + remoteFileName); // 파일 전송

            System.out.println("파일 업로드 완료"); // 파일 업로드 완료 메시지 출력
        } catch (JSchException | SftpException | java.io.FileNotFoundException e) {
            System.err.println("파일 업로드 중 오류 발생: " + e.getMessage()); // 오류 발생 시 오류 메시지 출력
        } finally {
            // 연결 종료
            if (channelSftp != null) {
                channelSftp.exit(); // 채널 종료
            }
            if (session != null) {
                session.disconnect(); // 세션 종료
            }
        }

    }

    //  SSH로 Ec2에 접근하는 함수
    public static void ConnectToEc2(String command)
    {
        String host = "13.58.106.251"; // 목적지 호스트의 IP 주소 또는 도메인 이름
        String user = "ec2-user"; // 원격 호스트의 사용자 이름
        String privateKey = "C:\\Users\\tlsgy\\OneDrive\\바탕 화면\\학기 공부\\project\\cloud\\Cloud_2016039082.pem"; // 개인 키파일 경로
        int port = 22; // SSH 포트 번호 (기본값)

        try {
            JSch jsch = new JSch(); // JSch 객체 생성
            jsch.addIdentity(privateKey); // 개인 키파일 추가

            Session session = jsch.getSession(user, host, port); // SSH 세션 생성
            session.setConfig("StrictHostKeyChecking", "no"); // 호스트 키 체크 설정
            session.connect(); // 세션 연결

            Channel channel = session.openChannel("exec"); // 명령 실행용 채널 열기
            ((ChannelExec) channel).setCommand(command); // 실행할 명령 설정

            channel.setInputStream(null); // 입력 스트림 설정
            ((ChannelExec) channel).setErrStream(System.err); // 에러 스트림 설정

            InputStream in = channel.getInputStream(); // 결과 입력 스트림 가져오기
            channel.connect(); // 채널 연결

            // 결과 출력
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 결과를 한 줄씩 출력
            }

            // 연결 종료
            channel.disconnect(); // 채널 종료
            session.disconnect(); // 세션 종료
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
        }

    }

    // 17. ready for condor_submit
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

    // 18. condor_submit
    public static void condor_submit(String jds_file_path)
    {
        String command="condor_submit -allow-crlf-script"+" "+jds_file_path;
        ConnectToEc2(command);
    }

    // 19. condor_q
    public static void condor_q()
    {
        String command = "condor_q";
        ConnectToEc2(command);
    }

    // 20. create Image
    public static void createImage(String instanceId , String imageDescription , String imageName)
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        CreateImageRequest createImageRequest = new CreateImageRequest()
                .withInstanceId(instanceId)
                .withName(imageName)
                .withDescription(imageDescription)
                .withNoReboot(true); // 인스턴스를 재부팅하지 않고 이미지 생성

        CreateImageResult createImageResult = ec2.createImage(createImageRequest);
        String imageId = createImageResult.getImageId();

        System.out.println("Created image ID: " + imageId);
    }

    // 21. all_start_instances
    public static void all_instances_start()
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // 중지된 인스턴스를 조회
        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        DescribeInstancesResult describeInstancesResult = ec2.describeInstances(describeInstancesRequest);

        for (Reservation reservation : describeInstancesResult.getReservations()) {
            for (Instance instance : reservation.getInstances()) {
                if (InstanceStateName.Stopped.toString().equals(instance.getState().getName())) {
                    // 중지된 인스턴스의 경우 시작
                    StartInstancesRequest startInstancesRequest = new StartInstancesRequest().withInstanceIds(instance.getInstanceId());
                    ec2.startInstances(startInstancesRequest);
                    System.out.println("Starting instance: " + instance.getInstanceId());
                } else {
                    System.out.println("Instance " + instance.getInstanceId() + " is already running.");
                }
            }
        }
    }

    // 21. all_stop_instances
    public static void all_instances_stop()
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        // 실행 중인 모든 인스턴스 조회
        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        DescribeInstancesResult describeInstancesResult = ec2.describeInstances(describeInstancesRequest);

        for (Reservation reservation : describeInstancesResult.getReservations()) {
            for (Instance instance : reservation.getInstances()) {
                if (InstanceStateName.Running.toString().equals(instance.getState().getName())) {
                    // 실행 중인 인스턴스의 경우 중지
                    StopInstancesRequest stopInstancesRequest = new StopInstancesRequest().withInstanceIds(instance.getInstanceId());
                    ec2.stopInstances(stopInstancesRequest);
                    System.out.println("Stopping instance: " + instance.getInstanceId());
                } else {
                    System.out.println("Instance " + instance.getInstanceId() + " is already stopped or stopping.");
                }
            }
        }
    }
}

