package com.derbysoft.nuke.dlm.server.codec;

import com.derbysoft.nuke.dlm.model.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created by passyt on 16-9-4.
 */
public class ProtoBuf2PermitResponseDecoder extends MessageToMessageDecoder<Protobuf.Response> {

    @Override
    protected void decode(ChannelHandlerContext ctx, Protobuf.Response response, List<Object> out) throws Exception {
        switch (response.getType()) {
            case REGISTER_RESPONSE:
                out.add(new RegisterResponse(response.getRegisterResponse().getPermitId(), response.getRegisterResponse().getErrorMessage(), response.getRegisterResponse().getSuccessful()));
                break;
            case UNREGISTER_RESPONSE:
                out.add(new UnRegisterResponse(response.getUnRegisterResponse().getPermitId(), response.getUnRegisterResponse().getErrorMessage(), response.getUnRegisterResponse().getSuccessful()));
                break;
            case EXISTING_RESPONSE:
                out.add(new ExistingResponse(response.getExistingResponse().getPermitId(), response.getExistingResponse().getErrorMessage(), response.getExistingResponse().getExisting()));
                break;
            case ACQUIRE_RESPONSE:
                out.add(new AcquireResponse(response.getAcquireResponse().getPermitId(), response.getAcquireResponse().getErrorMessage()));
                break;
            case TRY_ACQUIRE_RESPONSE:
                out.add(new TryAcquireResponse(response.getTryAcquireResponse().getPermitId(), response.getTryAcquireResponse().getErrorMessage(), response.getTryAcquireResponse().getSuccessful()));
                break;
            default:
                throw new IllegalArgumentException("Unknown message type by " + response.getType());
        }
    }

}
