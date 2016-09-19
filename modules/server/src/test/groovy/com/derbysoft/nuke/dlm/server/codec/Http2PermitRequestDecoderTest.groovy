package com.derbysoft.nuke.dlm.server.codec

import com.derbysoft.nuke.dlm.model.*
import com.derbysoft.nuke.dlm.server.PermitManager
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.DecoderResult
import io.netty.handler.codec.http.*
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.TimeUnit

/**
 * Created by passyt on 16-9-14.
 */
class Http2PermitRequestDecoderTest extends Specification {

    def Http2PermitRequestDecoder decoder;
    def FullHttpRequest request;
    def ChannelHandlerContext ctx;
    def PermitManager manager;
    @Shared
    def resourceId = UUID.randomUUID().toString();
    @Shared
    def transactionId = UUID.randomUUID().toString();

    def setup() {
        manager = Mock();
        decoder = new Http2PermitRequestDecoder(manager);
        request = Mock();
        ctx = Mock();
    }

    void "decode permit request"() {
        given:
        def out = [];
        def decoderResult = Mock(DecoderResult);
        def httpHeaders = Mock(HttpHeaders)

        when:
        decoder.decode(ctx, request, out);

        then:
        1 * request.decoderResult() >> decoderResult
        1 * decoderResult.isSuccess() >> true
        1 * request.headers() >> httpHeaders
        1 * httpHeaders.getAndConvert("transactionId", _) >> transactionId
        1 * request.uri() >> uri

        expect:
        out.size() == 1
        out.get(0).class == permitRequest.class
        out.get(0) == permitRequest

        where:
        uri                                                                  | permitRequest
        "/register/${resourceId}/permitname/TestPermit/spec/TestSpec"        | new RegisterRequest(resourceId, "TestPermit", "TestSpec", new Header(transactionId))
        "/unregister/${resourceId}"                                          | new UnRegisterRequest(resourceId, new Header(transactionId))
        "/existing/${resourceId}"                                            | new ExistingRequest(resourceId, new Header(transactionId))
        "/permit/${resourceId}/action/acquire"                               | new AcquireRequest(resourceId, new Header(transactionId))
        "/permit/${resourceId}/action/tryacquire"                            | new TryAcquireRequest(resourceId, new Header(transactionId))
        "/permit/${resourceId}/action/tryacquire/timeout/1/timeunit/seconds" | new TryAcquireRequest(resourceId, 1L, TimeUnit.SECONDS, new Header(transactionId))
        "/permit/${resourceId}/action/release"                               | new ReleaseRequest(resourceId, new Header(transactionId))
    }

    void help() {
        given:
        def out = [];
        def decoderResult = Mock(DecoderResult);
        def actualResponse = null;
        def channelFuture = Mock(ChannelFuture);
        def httpHeaders = new DefaultHttpHeaders();

        when:
        decoder.decode(ctx, request, out);

        then:
        1 * request.decoderResult() >> decoderResult
        1 * decoderResult.isSuccess() >> true
        1 * request.headers() >> httpHeaders
        1 * request.uri() >> "/help"
        1 * manager.permits() >> [:]
        1 * ctx.writeAndFlush(_) >> {
            actualResponse = it[0];
            channelFuture
        }
        1 * channelFuture.addListener(ChannelFutureListener.CLOSE)

        expect:
        def content = '''Usage:
<ul>
 <li>/register/${resourceId}/permitname/${permitname}/spec/${spec}</li>
 <li>/unregister/${resourceId}</li>
 <li>/existing/${resourceId}</li>
 <li>/permit/${resourceId}/action/acquire</li>
 <li>/permit/${resourceId}/action/tryacquire</li>
 <li>/permit/${resourceId}/action/tryacquire/timeout/10/timeunit/seconds</li>
 <li>/permit/${resourceId}/action/release</li>
 </ul>
Status:
<table border="1" bordercolor="#ccc" cellpadding="5" cellspacing="0" style="border-collapse:collapse;width:100%;">
</table>
'''

        def DefaultFullHttpResponse expectedResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(content.getBytes("UTF-8")));
        expectedResponse.headers().add("Content-Type", "text/html; charset=utf-8");
        expectedResponse.headers().add("Server", "Netty-5.0");

        expectedResponse == actualResponse
    }

}
