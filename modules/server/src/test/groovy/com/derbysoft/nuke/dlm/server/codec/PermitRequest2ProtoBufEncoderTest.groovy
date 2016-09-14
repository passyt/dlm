package com.derbysoft.nuke.dlm.server.codec

import com.derbysoft.nuke.dlm.model.AcquireRequest
import com.derbysoft.nuke.dlm.model.ExistingRequest
import com.derbysoft.nuke.dlm.model.Protobuf
import com.derbysoft.nuke.dlm.model.RegisterRequest
import com.derbysoft.nuke.dlm.model.ReleaseRequest
import com.derbysoft.nuke.dlm.model.TryAcquireRequest
import com.derbysoft.nuke.dlm.model.UnRegisterRequest
import com.derbysoft.nuke.dlm.server.utils.ProtoBufUtils
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.FullHttpRequest
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static com.derbysoft.nuke.dlm.model.Protobuf.Request.RequestType.ACQUIRE_REQUEST
import static com.derbysoft.nuke.dlm.model.Protobuf.Request.RequestType.EXISTING_REQUEST
import static com.derbysoft.nuke.dlm.model.Protobuf.Request.RequestType.REGISTER_REQUEST
import static com.derbysoft.nuke.dlm.model.Protobuf.Request.RequestType.RELEASE_REQUEST
import static com.derbysoft.nuke.dlm.model.Protobuf.Request.RequestType.TRY_ACQUIRE_REQUEST
import static com.derbysoft.nuke.dlm.model.Protobuf.Request.RequestType.UNREGISTER_REQUEST

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
                        .setPermitId(resourceId)
                        .setPermitResourceName("permitResourceName")
                        .setPermitSpec("permitSpec")
        ).build()
        new UnRegisterRequest(resourceId)                                   | Protobuf.Request
                .newBuilder()
                .setType(UNREGISTER_REQUEST)
                .setUnRegisterRequest(
                Protobuf.UnRegisterRequest
                        .newBuilder()
                        .setPermitId(resourceId)
        ).build()
        new ExistingRequest(resourceId)                                     | Protobuf.Request
                .newBuilder()
                .setType(EXISTING_REQUEST)
                .setExistingRequest(
                Protobuf.ExistingRequest
                        .newBuilder()
                        .setPermitId(resourceId)
        ).build()
        new AcquireRequest(resourceId)                                      | Protobuf.Request
                .newBuilder()
                .setType(ACQUIRE_REQUEST)
                .setAcquireRequest(
                Protobuf.AcquireRequest
                        .newBuilder()
                        .setPermitId(resourceId)
        ).build()
        new TryAcquireRequest(resourceId, 1L, TimeUnit.DAYS)                | Protobuf.Request
                .newBuilder()
                .setType(TRY_ACQUIRE_REQUEST)
                .setTryAcquireRequest(
                Protobuf.TryAcquireRequest
                        .newBuilder()
                        .setPermitId(resourceId)
                        .setTimeout(1L)
                        .setTimeUnit(Protobuf.TryAcquireRequest.TIME_UNIT.DAYS)
        ).build()
        new ReleaseRequest(resourceId)                                      | Protobuf.Request
                .newBuilder()
                .setType(RELEASE_REQUEST)
                .setReleaseRequest(
                Protobuf.ReleaseRequest
                        .newBuilder()
                        .setPermitId(resourceId)
        ).build()
    }

}
