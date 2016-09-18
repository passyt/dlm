package com.derbysoft.nuke.dlm.server.codec;

import com.derbysoft.nuke.dlm.model.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by passyt on 16-9-4.
 */
public class ProtoBuf2PermitRequestDecoder extends MessageToMessageDecoder<Protobuf.Request> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Protobuf.Request request, List<Object> out) throws Exception {
        switch (request.getType()) {
            case REGISTER_REQUEST:
                out.add(new RegisterRequest(request.getRegisterRequest().getResourceId(), request.getRegisterRequest().getPermitName(), request.getRegisterRequest().getPermitSpec()));
                break;
            case UNREGISTER_REQUEST:
                out.add(new UnRegisterRequest(request.getUnRegisterRequest().getResourceId()));
                break;
            case EXISTING_REQUEST:
                out.add(new ExistingRequest(request.getExistingRequest().getResourceId()));
                break;
            case ACQUIRE_REQUEST:
                out.add(new AcquireRequest(request.getAcquireRequest().getResourceId()));
                break;
            case TRY_ACQUIRE_REQUEST:
                if (request.getTryAcquireRequest().getTimeout() == 0) {
                    out.add(new TryAcquireRequest(request.getTryAcquireRequest().getResourceId()));
                } else {
                    out.add(new TryAcquireRequest(request.getTryAcquireRequest().getResourceId(), request.getTryAcquireRequest().getTimeout(), TimeUnit.valueOf(request.getTryAcquireRequest().getTimeUnit().name())));
                }
                break;
            case RELEASE_REQUEST:
                out.add(new ReleaseRequest(request.getReleaseRequest().getResourceId()));
                break;
            default:
                throw new IllegalArgumentException("Unknown message type by " + request.getType());
        }
    }
}
