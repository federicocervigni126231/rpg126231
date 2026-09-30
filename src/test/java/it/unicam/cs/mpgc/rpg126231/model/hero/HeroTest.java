package it.unicam.cs.mpgc.rpg126231.model.hero;

import it.unicam.cs.mpgc.rpg126231.model.combat.Stats;
import it.unicam.cs.mpgc.rpg126231.model.item.Inventory;
import it.unicam.cs.mpgc.rpg126231.model.item.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeroTest {

    private static final Weapon SWORD = new Weapon("sword", "Spada", 5);
    private static final Weapon DAGGER = new Weapon("dagger", "Pugnale", 3);

    private final HeroClass warrior = new Warrior();
    private Hero hero;

    @BeforeEach
    void setUp() {
        hero = new Hero("Aldo", warrior);
    }

    @Test
    void newHeroStartsAtLevelOneWithBaseStatsAndFullHp() {
        assertEquals(1, hero.level());
        assertEquals(0, hero.experience());
        assertEquals(warrior.baseStats(), hero.stats());
        assertEquals(hero.maxHp(), hero.currentHp());
        assertTrue(hero.inventory().items().isEmpty());
        assertTrue(hero.equippedWeapon().isEmpty());
    }

    @Test
    void experienceBelowThresholdDoesNotLevelUp() {
        assertEquals(0, hero.gainExperience(99));
        assertEquals(1, hero.level());
        assertEquals(99, hero.experience());
    }

    @Test
    void levelUpIncreasesStatsAndKeepsLeftoverExperience() {
        assertEquals(1, hero.gainExperience(130));
        assertEquals(2, hero.level());
        assertEquals(30, hero.experience());
        assertEquals(warrior.baseStats().plus(warrior.growthPerLevel()), hero.stats());
    }

    @Test
    void largeExperienceGainCanGiveSeveralLevels() {
        // Livello 1 -> 2 costa 100, livello 2 -> 3 costa 200.
        assertEquals(2, hero.gainExperience(310));
        assertEquals(3, hero.level());
        assertEquals(10, hero.experience());
    }

    @Test
    void levelUpRestoresFullHp() {
        hero.takeDamage(50);
        hero.gainExperience(100);
        assertEquals(hero.maxHp(), hero.currentHp());
    }

    @Test
    void negativeExperienceIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> hero.gainExperience(-1));
    }

    @Test
    void equippedWeaponAddsAttackBonusAndLeavesInventory() {
        hero.inventory().add(SWORD);
        hero.equip(SWORD);
        assertEquals(warrior.baseStats().attack() + 5, hero.attackPower());
        assertEquals(Optional.of(SWORD), hero.equippedWeapon());
        assertTrue(hero.inventory().items().isEmpty());
    }

    @Test
    void equippingAnotherWeaponReturnsThePreviousOneToInventory() {
        hero.inventory().add(SWORD);
        hero.inventory().add(DAGGER);
        hero.equip(SWORD);
        hero.equip(DAGGER);
        assertEquals(Optional.of(DAGGER), hero.equippedWeapon());
        assertEquals(List.of(SWORD), hero.inventory().items());
    }

    @Test
    void equippingAWeaponNotInInventoryIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> hero.equip(SWORD));
    }

    @Test
    void restoredHeroKeepsItsState() {
        Inventory inventory = new Inventory();
        Hero restored = new Hero("Aldo", warrior, 3, 40, 70, inventory, SWORD);
        assertEquals(3, restored.level());
        assertEquals(40, restored.experience());
        assertEquals(70, restored.currentHp());
        assertEquals(Optional.of(SWORD), restored.equippedWeapon());
    }

    @Test
    void invalidRestoredStateIsRejected() {
        Inventory inventory = new Inventory();
        assertThrows(IllegalArgumentException.class,
                () -> new Hero("Aldo", warrior, 0, 0, 10, inventory, null));
        assertThrows(IllegalArgumentException.class,
                () -> new Hero("Aldo", warrior, 1, 100, 10, inventory, null));
        assertThrows(IllegalArgumentException.class,
                () -> new Hero("Aldo", warrior, 1, 0, 1000, inventory, null));
    }

    @Test
    void everyHeroClassHasAnAbilityAndValidBaseStats() {
        for (HeroClass heroClass : List.of(new Warrior(), new Mage(), new Rogue())) {
            Stats base = heroClass.baseStats();
            assertTrue(base.maxHp() > 0, heroClass.id());
            assertTrue(heroClass.ability().cooldownTurns() > 0, heroClass.id());
        }
    }
}
