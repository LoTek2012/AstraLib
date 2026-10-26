# AstraLib

A library for Minecraft space mods used in AstraCraft.

## Gravity API

`GravityManager` exposes the gravity value as a multiplier of Earth's gravity (`1.0`).
Entities inherit their dimension's default unless they have an individual override.
The Overworld, Nether, and End default to `1.0`; other dimensions also default to
`1.0` until configured.

```java
GravityManager.setDimensionGravity(customDimension, 0.38);
GravityManager.setGravity(player, 0.8);
double gravity = GravityManager.getGravity(player);
GravityManager.resetGravity(player); // Inherit the current dimension's default again
```

The gravity value is persisted on the entity. The physics handler applies the
multiplier before entity ticks on both the logical server and client; the
server synchronizes player gravity to the client for responsive local movement.

## `/gravity`

Operators can use `/gravity <targets> set <value>`, `/gravity <targets> get`, and
`/gravity <targets> reset`. Targets use vanilla entity selectors. `reset` removes
the individual override and restores the gravity inherited from the entity's
current dimension.
