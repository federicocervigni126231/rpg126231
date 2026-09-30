package it.unicam.cs.mpgc.rpg126231.service.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EventBusTest {

    private static final GameEvent EVENT = new GameEvent.ExperienceGained(10);

    private EventBus bus;

    @BeforeEach
    void setUp() {
        bus = new EventBus();
    }

    @Test
    void publishReachesEverySubscriberInOrder() {
        List<String> received = new ArrayList<>();
        bus.subscribe(event -> received.add("primo"));
        bus.subscribe(event -> received.add("secondo"));

        bus.publish(EVENT);

        assertEquals(List.of("primo", "secondo"), received);
    }

    @Test
    void unsubscribedListenerNoLongerReceivesEvents() {
        List<GameEvent> received = new ArrayList<>();
        GameEventListener listener = received::add;
        bus.subscribe(listener);
        bus.unsubscribe(listener);

        bus.publish(EVENT);

        assertTrue(received.isEmpty());
    }

    @Test
    void listenerCanUnsubscribeWhileHandlingAnEvent() {
        List<GameEvent> received = new ArrayList<>();
        bus.subscribe(new GameEventListener() {
            @Override
            public void onEvent(GameEvent event) {
                bus.unsubscribe(this);
            }
        });
        bus.subscribe(received::add);

        bus.publish(EVENT);

        assertEquals(List.of(EVENT), received);
    }
}
