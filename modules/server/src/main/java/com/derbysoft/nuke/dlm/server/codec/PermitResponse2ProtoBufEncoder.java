package com.derbysoft.nuke.dlm.server.codec;

import com.derbysoft.nuke.dlm.model.*;
import com.derbysoft.nuke.dlm.server.utils.ProtoBufUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import static com.derbysoft.nuke.dlm.model.Protobuf.Response.ResponseType.*;
import static com.derbysoft.nuke.dlm.model.Protobuf.Response.ResponseType.ACQUIRE_RESPONSE;

/**
 * Created by passyt on 16-9-4.
 */
public class PermitResponse2ProtoBufEncoder extends MessageToMessageEncoder<IPermitResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, IPermitResponse response, List<Object> out) throws Exception {
        if (response instanceof RegisterResponse) {
            RegisterResponse actualResponse = (RegisterResponse) response;
            out.add(Protobuf.Response.newBuilder()
                    .setType(REGISTER_RESPONSE)
                    .setRegisterResponse(
                            Protobuf.RegisterResponse.newBuilder()
                                    .setPermitId(ProtoBufUtils.safeValueOf(actualResponse.getPermitId()))
                                    .setErrorMessage(ProtoBufUtils.safeValueOf(actualResponse.getErrorMessage()))
                                    .setSuccessful(actualResponse.isSuccessful())
                    )
                    .build()
            );
        } else if (response instanceof UnRegisterResponse) {
            UnRegisterResponse actualResponse = (UnRegisterResponse) response;
            out.add(Protobuf.Response.newBuilder()
                    .setType(UNREGISTER_RESPONSE)
                    .setUnRegisterResponse(
                            Protobuf.UnRegisterResponse.newBuilder()
                                    .setPermitId(ProtoBufUtils.safeValueOf(actualResponse.getPermitId()))
                                    .setErrorMessage(ProtoBufUtils.safeValueOf(actualResponse.getErrorMessage()))
                                    .setSuccessful(actualResponse.isSuccessful())
                    )
                    .build()
            );
        } else if (response instanceof ExistingResponse) {
            ExistingResponse actualResponse = (ExistingResponse) response;
            out.add(Protobuf.Response.newBuilder()
                    .setType(EXISTING_RESPONSE)
                    .setExistingResponse(
                            Protobuf.ExistingResponse.newBuilder()
                                    .setPermitId(ProtoBufUtils.safeValueOf(actualResponse.getPermitId()))
                                    .setErrorMessage(ProtoBufUtils.safeValueOf(actualResponse.getErrorMessage()))
                                    .setExisting(actualResponse.isExisting())
                    )
                    .build()
            );
        } else if (response instanceof AcquireResponse) {
            AcquireResponse actualResponse = (AcquireResponse) response;
            out.add(Protobuf.Response.newBuilder()
                    .setType(ACQUIRE_RESPONSE)
                    .setAcquireResponse(
                            Protobuf.AcquireResponse.newBuilder()
                                    .setPermitId(ProtoBufUtils.safeValueOf(actualResponse.getPermitId()))
                                    .setErrorMessage(ProtoBufUtils.safeValueOf(actualResponse.getErrorMessage()))

                    )
                    .build()
            );
        } else if (response instanceof TryAcquireResponse) {
            TryAcquireResponse actualResponse = (TryAcquireResponse) response;
            out.add(Protobuf.Response.newBuilder()
                    .setType(ACQUIRE_RESPONSE)
                    .setTryAcquireResponse(
                            Protobuf.TryAcquireResponse.newBuilder()
                                    .setPermitId(ProtoBufUtils.safeValueOf(actualResponse.getPermitId()))
                                    .setErrorMessage(ProtoBufUtils.safeValueOf(actualResponse.getErrorMessage()))
                                    .setSuccessful(actualResponse.isSuccessful())
                    )
                    .build()
            );
        } else if (response instanceof ReleaseResponse) {
            ReleaseResponse actualResponse = (ReleaseResponse) response;
            out.add(Protobuf.Response.newBuilder()
                    .setType(RELEASE_RESPONSE)
                    .setReleaseResponse(
                            Protobuf.ReleaseResponse.newBuilder()
                                    .setPermitId(ProtoBufUtils.safeValueOf(actualResponse.getPermitId()))
                                    .setErrorMessage(ProtoBufUtils.safeValueOf(actualResponse.getErrorMessage()))
                    )
                    .build()
            );
        } else {
            throw new IllegalArgumentException("Unknown message type by " + response.getClass());
        }
    }

}
