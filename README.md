# Samū/Game
A small library for managing a game with a game loop, modules and services.

# Usage
To get a game up and running, three steps are needed:
1. Extend the class `net.shadew.game.Game`
2. Create an instance of your game class
3. Call `run()` on your game instance

To stop the game, call `stop()`.

## Lifecycles
Game components and the game itself use lifecycles, which consist of three phases: "init", "update" and "cleanup".
- In the "init" phase, components should initialize themselves.
- In the "update" phase, components receive frequent updates from the game loop.
- In the "cleanup" phase, components should clean up the mess they've made (if they made that).

## NSIDs
NSID stands for **N**ame**S**paced **ID**entifier, which is a name or path to a file prefixed with a namespace. NSIDs
are used to identify most things in a game, such as a module, service or resource. The class `net.shadew.game.NSID` represents
an NSID.

Usually, a game associates one default namespace with itself. External components and resources then should preferably
use a different namespace to identify as foreign. Before initializing anything, call
`net.shadew.game.DefaultNamespace.set(...)` to configure a default namespace for namespace parsing. If none is set, it will
try to read the JVM property `net.shadew.defns`, which can be particularly disabled by setting the default namespace to `null`
explicitly (in this case there is no default namespace and namespaces must always be explicitly specified).

## Modules
A module is a component of a game. The order in which modules load is based on dependencies: modules can indicate which
modules they need to have or which modules they optionally need, and whether they should load before or after the
module.

To add a module:
1. Extend the class `net.shadew.game.Module`
2. Optionally, call some `dependsOn(...)` and `finalizedBy(...)` methods in your module's constructor
3. In your game class, call `addModule` and pass an instance of your module class
4. Optionally, assign the module to a field in your class

## Services
A service is a temporary component of a game. It can be started at any time during the runtime of the game, and stopped
at any later moment. All services are automatically stopped when the game stops.

To add a service:
1. Extend the class `net.shadew.game.Service`
2. When you want to start the service, call `start()` on your service or `startService(service)` on your game context
3. When the service is done, call `stop()` on your service or `stopService(service)` on your game context

## Signals
Signals are little events that can be emitted and will be broadcasted to all modules and services, and the game itself.
Anything that implements `net.shadew.game.Signalable` can essentially receive signals, but the game will only broadcast to
services, modules and the game itself. It is your task to forward signals to other components.

To emit a signal:
1. Instantiate a `net.shadew.game.Signal` (you may optionally extend this class)
2. Call `emit(signal)` on your game context

## Game contexts
A `net.shadew.game.GameContext` is quick access to a certain game instance. It is implemented by the game itself, but also by
modules and services which delegate all calls directly to their associated game.

## Exception handling
Exceptions are a common occurence in games. In each lifecycle phase, exceptions can occur which are caught by the game
and sent to their respective sources. Two types of exceptions can be identified: fatal and non-fatal exceptions.
- If the exception is a `java.lang.Error`, it is considered fatal
- If the exception is a `net.shadew.game.GameException` with the `fatal` flag set, it is considered fatal

It is up to the implementation what to do with these exceptions, but the default action is to stop the game in case of
fatal exceptions only.

### Error reports
Error reports are a handy tool to collect information about occurred exceptions. To use error reports properly, some
setup is needed:

- Override `handleReport` in your game class to handle
- At useful points:
  1. Wrap code in a `try`-`catch` block, and catch `java.lang.Throwable`
  2. Obtain a `net.shadew.game.ErrorReport` by calling `ErrorReport.of(throwable)`
  3. Add a context using `addContext(context)`
  4. Throw the error report, it is a `Throwable` itself

  Example:
  ```java
  public void checkpoint(String someData) {
      try {
          doSomethingThatHasExceptions(someData);
      } catch(Throwable thr) {
          throw ErrorReport.of(thr).addContext(
              ErrorContext.context("at a checkpoint")
                          .prop("some_data", someData)
          );
      }
  }
  ```

When printing the error report, it attempts to match parts of the stack trace with context checkpoints.

## Debugging
The `net.shadew.game.GameDebugListener` has various methods that are called at many different points in the game's runtime
process. By default, the game calls them on a no-op implementation. The listener can be set to a custom implementation,
for example for logging. It is possible to set watchpoints on the methods of the interface to pause execution in the
debugger at those points - no custom implementation needs to be set for this.

# Download
The artifact is available on my Maven:
```groovy
repositories {
    maven { url "https://maven.shadew.net/" }
}

dependencies {
    implementation "net.shadew:game:1.0"
}
```
