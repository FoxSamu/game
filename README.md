# Samū/Game
A small library for managing a game with a game loop, modules and services.

# Usage

## NSIDs
NSID stands for **N**ame**S**paced **ID**entifier, which is a name or path to a file prefixed with a namespace. NSIDs
are used to identify most things in a game, such as a module, service or resource. The class `samu.game.NSID` represents
an NSID.

## Modules
A module is a component of a game. The order in which modules load
