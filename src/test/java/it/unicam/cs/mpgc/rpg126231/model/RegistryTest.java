package it.unicam.cs.mpgc.rpg126231.model;

import it.unicam.cs.mpgc.rpg126231.model.hero.HeroClass;
import it.unicam.cs.mpgc.rpg126231.model.hero.Mage;
import it.unicam.cs.mpgc.rpg126231.model.hero.Warrior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegistryTest {

    private final HeroClass warrior = new Warrior();
    private final HeroClass mage = new Mage();
    private Registry<HeroClass> registry;

    @BeforeEach
    void setUp() {
        registry = new Registry<>();
        registry.register(warrior);
        registry.register(mage);
    }

    @Test
    void getReturnsTheElementWithThatId() {
        assertSame(mage, registry.get("mage"));
    }

    @Test
    void allKeepsRegistrationOrder() {
        assertEquals(List.of(warrior, mage), List.copyOf(registry.all()));
    }

    @Test
    void duplicateIdIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> registry.register(new Warrior()));
    }

    @Test
    void unknownIdIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> registry.get("paladin"));
    }
}
