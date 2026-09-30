package it.unicam.cs.mpgc.rpg126231.model.item;

import it.unicam.cs.mpgc.rpg126231.model.hero.Hero;
import it.unicam.cs.mpgc.rpg126231.model.hero.Mage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemTest {

    @Test
    void potionHealsTheTarget() {
        Hero hero = new Hero("Merlino", new Mage());
        hero.takeDamage(40);
        new Potion("small_potion", "Pozione piccola", 30).consume(hero);
        assertEquals(hero.maxHp() - 10, hero.currentHp());
    }

    @Test
    void potionWithNonPositiveHealIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Potion("bad", "Pozione", 0));
    }

    @Test
    void weaponWithNegativeBonusIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Weapon("bad", "Arma", -1));
    }
}
