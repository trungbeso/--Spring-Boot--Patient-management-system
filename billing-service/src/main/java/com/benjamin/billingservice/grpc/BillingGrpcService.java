package com.benjamin.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest, StreamObserver<billing.BillingResponse> responseObserver) {
        log.info("Create billing account request received {} ", billingRequest.toString());
        //TODO: business logic

        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("113")
                .setStatus("ACTIVE")
                .build();

        responseObserver.onNext(response); //send response from grpc service (billing service) back to client (patient service)
        /* Can pass any response as u want before onCompleted */
        responseObserver.onCompleted(); // end the cycle of this response
    }

}
