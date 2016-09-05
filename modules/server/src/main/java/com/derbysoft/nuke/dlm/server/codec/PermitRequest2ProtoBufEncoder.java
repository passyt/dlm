package com.derbysoft.nuke.dlm.server.codec;

import com.derbysoft.nuke.dlm.model.*;
import com.derbysoft.nuke.dlm.server.utils.ProtoBufUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.derbysoft.nuke.dlm.model.Protobuf.Request.RequestType.*;
import static com.derbysoft.nuke.dlm.model.Protobuf.Request.RequestType.TRY_ACQUIRE_REQUEST;

/**
 * Created by passyt on 16-9-4.
 */
public class PermitRequest2ProtoBufEncoder extends MessageToMessageEncoder<IPermitRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IPermitRequest request, List<Object> out) throws Exception {
        if (request instanceof RegisterRequest) {
            RegisterRequest actualRequest = (RegisterRequest) request;
            out.add(Protobuf.Request.newBuilder()
                    .setType(REGISTER_REQUEST)
                    .setRegisterRequest(
                            Protobuf.RegisterRequest.newBuilder()
                                    .setPermitId(actualRequest.getPermitId())
                                    .setPermitResourceName(ProtoBufUtils.safeValueOf(actualRequest.getPermitResourceName()))
                                    .setPermitSpec(actualRequest.getPermitSpec().getSpecification())
                    )
                    .build());
        } else if (request instanceof UnRegisterRequest) {
            out.add(Protobuf.Request.newBuilder()
                    .setType(UNREGISTER_REQUEST)
                    .setUnRegisterRequest(
                            Protobuf.UnRegisterRequest.newBuilder()
                                    .setPermitId(((UnRegisterRequest) request).getPermitId())
                    )
                    .build());
        } else if (request instanceof ExistingRequest) {
            out.add(Protobuf.Request.newBuilder()
                    .setType(EXISTING_REQUEST)
                    .setExistingRequest(
                            Protobuf.ExistingRequest.newBuilder()
                                    .setPermitId(((ExistingRequest) request).getPermitId())
                    )
                    .build());
        } else if (request instanceof AcquireRequest) {
            out.add(Protobuf.Request.newBuilder()
                    .setType(ACQUIRE_REQUEST)
                    .setAcquireRequest(
                            Protobuf.AcquireRequest.newBuilder()
                                    .setPermitId(((AcquireRequest) request).getPermitId())
                    )
                    .build());
        } else if (request instanceof TryAcquireRequest) {
            TryAcquireRequest tryAcquireRequest = (TryAcquireRequest) request;
            out.add(Protobuf.Request.newBuilder()
                    .setType(TRY_ACQUIRE_REQUEST)
                    .setTryAcquireRequest(
                            Protobuf.TryAcquireRequest.newBuilder()
                                    .setPermitId(tryAcquireRequest.getPermitId())
                                    .setTimeout(ProtoBufUtils.safeValueOf(tryAcquireRequest.getTimeout()))
                                    .setTimeUnit(
                                            Protobuf.TryAcquireRequest.TIME_UNIT.valueOf(
                                                    ProtoBufUtils.safeValueOf(tryAcquireRequest.getTimeUnit(), TimeUnit.DAYS).name()
                                            )
                                    )
                    )
                    .build());
        } else if (request instanceof ReleaseRequest) {
            ReleaseRequest releaseRequest = (ReleaseRequest) request;
            out.add(Protobuf.Request.newBuilder()
                    .setType(RELEASE_REQUEST)
                    .setReleaseRequest(
                            Protobuf.ReleaseRequest.newBuilder()
                                    .setPermitId(releaseRequest.getPermitId())
                    )
                    .build());
        } else {
            throw new IllegalArgumentException("Unknown message type by " + request.getClass());
        }
    }

}
