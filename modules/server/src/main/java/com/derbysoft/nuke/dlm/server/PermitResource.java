package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by passyt on 16-9-2.
 */
public class PermitResource implements IPermitResource {

    private Logger log = LoggerFactory.getLogger(PermitResource.class);
    private ConcurrentMap<String, IPermit> permits = new ConcurrentHashMap<>();

    @Override
    public boolean register(String permitId, String permitClassName, Object... arguments) {
        log.debug("Register permit {} with arguments {} by id {}", permitClassName, arguments, permitId);

        if (permits.putIfAbsent(permitId, buildPermit(permitClassName, arguments)) != null) {
            log.warn("An existing permit {} with id {}, not allow to register", permits.get(permitId), permitId);
            return false;
        }

        return true;
    }

    @Override
    public IPermit getPermit(String permitId) {
        return permits.get(permitId);
    }

    protected IPermit buildPermit(String permitClassName, Object[] arguments) {
        try {
            Class<?> clazz = Class.forName(permitClassName);
            Constructor<IPermit>[] constructors = (Constructor<IPermit>[]) clazz.getConstructors();
            for (Constructor<IPermit> constructor : constructors) {
                if (constructor.getParameterCount() != arguments.length) {
                    continue;
                }

                try {
                    return constructor.newInstance(toArgs(arguments, constructor.getParameterTypes()));
                } catch (Exception e) {
                }
            }
            throw new IllegalArgumentException("No permit by class " + permitClassName + " with arguments " + Arrays.toString(arguments));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected Object[] toArgs(Object[] arguments, Class<?>[] parameterTypes) {
        Object[] args = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            args[i] = toArg(arguments[i], parameterTypes[i]);
        }
        return args;
    }

    protected Object toArg(Object argument, Class<?> parameterType) {
        //TODO convert to arg with a specific type
        return argument;
    }

}
