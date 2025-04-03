package com.benjamin.stack;

import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.*;
import software.amazon.awscdk.services.rds.*;
import software.amazon.awscdk.services.route53.CfnHealthCheck;

public class LocalStack extends Stack{

    private final Vpc vpc;

    public LocalStack(final App scope, final String id, final StackProps props) {
        super(scope, id, props);
        this.vpc = createVpc();

        DatabaseInstance authServiceDb = createDatabase("AuthServiceDb", "identity-db");

        DatabaseInstance patientServiceDb = createDatabase("PatientServiceDb", "patient-service-db");

        CfnHealthCheck authDbHealthCheck = createDbHealthCheck(authServiceDb, "AuthServiceDBHealthCheck");
        CfnHealthCheck patientServiceDBHealthCheck = createDbHealthCheck(patientServiceDb, "PatientServiceDBHealthCheck");
    }

    private CfnHealthCheck createDbHealthCheck(DatabaseInstance db, String id){
        return CfnHealthCheck.Builder.create(this, id)
                .healthCheckConfig(CfnHealthCheck.HealthCheckConfigProperty
                        .builder()
                        .type("TCP")
                        .port(Token.asNumber(db.getDbInstanceEndpointPort()))
                        .ipAddress(db.getDbInstanceEndpointAddress())
                        .requestInterval(30)
                        .failureThreshold(3)
                        .build())
                .build();
    }

    private Vpc createVpc(){
        return Vpc.Builder
                .create(this, "PatientManagementVPC")
                .vpcName("PatientManagementVPC")
                .maxAzs(2)
                .build();
    }

    //construct will create an RDS database
    private DatabaseInstance createDatabase(String id, String dbName) {
        return DatabaseInstance.Builder
                .create(this, id)
                .engine(DatabaseInstanceEngine
                        .postgres(PostgresInstanceEngineProps
                                .builder()
                                .version(PostgresEngineVersion.VER_17_2)
                                .build()))
                .vpc(vpc)
                /* when deploy in ws, can use different setting for database depend on customer*/
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                .allocatedStorage(20)
                .credentials(Credentials.fromGeneratedSecret("benjamin"))
                .databaseName(dbName)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
    }


    /* entry point to create cloud formation template for infrastructure*/
    public static void main(final String[] args) {
        App app = new App(AppProps.builder().outdir("./cdk.out").build());

        /* define some additional properties you want to apply to ur stack */
        StackProps props = StackProps.builder()
                .synthesizer(new BootstraplessSynthesizer()) // Synthesizer is an ABS term used to convert code into a cloud formation template
                .build();

        new LocalStack(app, "localstack", props);
        app.synth();
        /* app.synth tell cdk that we want to take our stack + any props add to ./cdk.out folder */
        System.out.println("App synthesizing in progress . . .");
    }
}
