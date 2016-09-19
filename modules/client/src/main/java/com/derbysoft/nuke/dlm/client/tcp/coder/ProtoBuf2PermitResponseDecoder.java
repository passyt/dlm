package com.derbysoft.nuke.dlm.client.tcp.coder;

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
                out.add(new RegisterResponse(response.getRegisterResponse().getResourceId(), response.getRegisterResponse().getErrorMessage(), response.getRegisterResponse().getSuccessful(), new Header(response.getRegisterResponse().getHeader().getTransactionId())));
                break;
            case UNREGISTER_RESPONSE:
                out.add(new UnRegisterResponse(response.getUnRegisterResponse().getResourceId(), response.getUnRegisterResponse().getErrorMessage(), response.getUnRegisterResponse().getSuccessful(), new Header(response.getUnRegisterResponse().getHeader().getTransactionId())));
                break;
            case EXISTING_RESPONSE:
                out.add(new ExistingResponse(response.getExistingResponse().getResourceId(), response.getExistingResponse().getErrorMessage(), response.getExistingResponse().getExisting(), new Header(response.getExistingResponse().getHeader().getTransactionId())));
                break;
            case ACQUIRE_RESPONSE:
                out.add(new AcquireResponse(response.getAcquireResponse().getResourceId(), response.getAcquireResponse().getErrorMessage(), new Header(response.getAcquireResponse().getHeader().getTransactionId())));
                break;
            case TRY_ACQUIRE_RESPONSE:
                out.add(new TryAcquireResponse(response.getTryAcquireResponse().getResourceId(), response.getTryAcquireResponse().getErrorMessage(), response.getTryAcquireResponse().getSuccessful(), new Header(response.getTryAcquireResponse().getHeader().getTransactionId())));
                break;
            case RELEASE_RESPONSE:
                out.add(new ReleaseResponse(response.getReleaseResponse().getResourceId(), response.getReleaseResponse().getErrorMessage(), new Header(response.getReleaseResponse().getHeader().getTransactionId())));
                break;
            default:
                throw new IllegalArgumentException("Unknown message type by " + response.getType());
        }
    }

}
