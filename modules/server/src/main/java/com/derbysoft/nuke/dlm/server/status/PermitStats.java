package com.derbysoft.nuke.dlm.server.status;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by passyt on 16-9-19.
 */
public class PermitStats {

    private Peak peak = new Peak();
    private AtomicLong actives = new AtomicLong(0);

    public PermitStats increment() {
        long total = actives.incrementAndGet();
        if (total >= peak.getCount().get()) {
            peak.getCount().set(total);
            peak.timestamp.set(ZonedDateTime.now());
        }
        return this;
    }

    public PermitStats decrement() {
        actives.decrementAndGet();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermitStats)) return false;
        PermitStats that = (PermitStats) o;
        return Objects.equal(peak, that.peak) &&
                Objects.equal(actives, that.actives);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(peak, actives);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("peak", peak)
                .add("actives", actives)
                .toString();
    }

    private static class Peak {

        private final AtomicLong count;
        private final AtomicReference<ZonedDateTime> timestamp;

        public Peak() {
            count = new AtomicLong(0);
            timestamp = new AtomicReference(ZonedDateTime.now());
        }

        public AtomicLong getCount() {
            return count;
        }

        public AtomicReference<ZonedDateTime> getTimestamp() {
            return timestamp;
        }

        public Peak increment() {
            count.incrementAndGet();
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Peak)) return false;
            Peak peak = (Peak) o;
            return Objects.equal(count, peak.count) &&
                    Objects.equal(timestamp, peak.timestamp);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(count, timestamp);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("count", count)
                    .add("timestamp", timestamp)
                    .toString();
        }
    }

}
