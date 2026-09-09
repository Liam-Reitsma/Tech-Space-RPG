package game.combat;

import java.util.Optional;

public record ActionDecider(String message, Optional<CombatErrors> endReason) {
}
