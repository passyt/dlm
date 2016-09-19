package com.derbysoft.nuke.dlm.client.tcp.coder

import com.derbysoft.nuke.dlm.model.*
import io.netty.channel.ChannelHandlerContext
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static com.derbysoft.nuke.dlm.model.Protobuf.Request.RequestType.*

/**
 * Created by passyt on 16-9-14.
 */
class PermitRequest2ProtoBufEncoderTest extends Specification {

    def PermitRequest2ProtoBufEncoder encoder;
    def ChannelHandlerContext ctx;
    @Shared
    def resourceId = UUID.randomUUID().toString();

    def setup() {
        encoder = new PermitRequest2ProtoBufEncoder();
        ctx = Mock();
    }

    void encode() {
        def out = [];
        encoder.encode(ctx, permitRequest, out);

        expect:
        out.size() == 1
        out.get(0).class == protoRequest.class
        out.get(0) == protoRequest

        where:
        permitRequest                                                       | protoRequest
        new RegisterRequest(resourceId, "permitResourceName", "permitSpec") | Protobuf.Request
                .newBuilder()
                .setType(REGISTER_REQUEST)
                .setRegisterRequest(
                Protobuf.RegisterRequest
                        .newBuilder()
                        .setHeader(Protobuf.Header.newBuilder().setTransactionId(permitRequest.getHeader().getTransactionId()))
                        .setResourceId(resourceId)
                        .setPermitName("permitResourceName")
                        .setPermitSpec("permitSpec")
        ).build()
        new UnRegisterRequest(resourceId)                                   | Protobuf.Request
                .newBuilder()
                .setType(UNREGISTER_REQUEST)
                .setUnRegisterRequest(
                Protobuf.UnRegisterRequest
                        .newBuilder()
                        .setHeader(Protobuf.Header.newBuilder().setTransactionId(permitRequest.getHeader().getTransactionId()))
                        .setResourceId(resourceId)
        ).build()
        new ExistingRequest(resourceId)                                     | Protobuf.Request
                .newBuilder()
                .setType(EXISTING_REQUEST)
                .setExistingRequest(
                Protobuf.ExistingRequest
                        .newBuilder()
                        .setHeader(Protobuf.Header.newBuilder().setTransactionId(permitRequest.getHeader().getTransactionId()))
                        .setResourceId(resourceId)
        ).build()
        new AcquireRequest(resourceId)                                      | Protobuf.Request
                .newBuilder()
                .setType(ACQUIRE_REQUEST)
                .setAcquireRequest(
                Protobuf.AcquireRequest
                        .newBuilder()
                        .setHeader(Protobuf.Header.newBuilder().setTransactionId(permitRequest.getHeader().getTransactionId()))
                        .setResourceId(resourceId)
        ).build()
        new TryAcquireRequest(resourceId, 1L, TimeUnit.DAYS)                | Protobuf.Request
                .newBuilder()
                .setType(TRY_ACQUIRE_REQUEST)
                .setTryAcquireRequest(
                Protobuf.TryAcquireRequest
                        .newBuilder()
                        .setHeader(Protobuf.Header.newBuilder().setTransactionId(permitRequest.getHeader().getTransactionId()))
                        .setResourceId(resourceId)
                        .setTimeout(1L)
                        .setTimeUnit(Protobuf.TryAcquireRequest.TIME_UNIT.DAYS)
        ).build()
        new ReleaseRequest(resourceId)                                      | Protobuf.Request
                .newBuilder()
                .setType(RELEASE_REQUEST)
                .setReleaseRequest(
                Protobuf.ReleaseRequest
                        .newBuilder()
                        .setHeader(Protobuf.Header.newBuilder().setTransactionId(permitRequest.getHeader().getTransactionId()))
                        .setResourceId(resourceId)
        ).build()
    }

}
