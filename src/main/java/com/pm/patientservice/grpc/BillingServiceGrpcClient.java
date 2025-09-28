package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.stubfactory.BlockingStubFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub stub;

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort,
            BlockingStubFactory blockingStubFactory){
        log.info("Initializing BillingServiceGrpcClient service at {}:{}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        stub = BillingServiceGrpc.newBlockingStub(channel);
    }
    public BillingResponse createBillingAccount(String patientId,String name,String email){
        BillingRequest request = BillingRequest.newBuilder().setName(name).setEmail(email).build();

        BillingResponse response = stub.createBillingAccount(request);
        log.info("createBillingAccount response {}", response);
        return response;
    }
}
