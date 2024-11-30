package org.qlik.memoryLeak;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakDemo {

    interface EventListener {
        void onEvent();
    }

    static class EventSource {
        private final List<EventListener> listeners = new ArrayList<>();

        public void registerListener(EventListener listener) {
            listeners.add(listener);
        }

        public void triggerEvent() {
            for (EventListener listener : listeners) {
                listener.onEvent();
            }
        }
    }

    //do not run code will cause memory leaks
    public static void main(String[] args) throws InterruptedException {
        EventSource source = new EventSource();

        for (int i = 0; i < 10_000; i++) {
            final int number = i;
            source.registerListener(() -> System.out.println("Event triggered for listener " + number));
        }

        System.out.println("Listeners registered. Triggering event...");
        source.triggerEvent(); // Memory leak: listeners are never removed
        Thread.sleep(1000000);

    }
}
