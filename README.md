# Extension For Java SDK

This repostory provide a Java SDK based on [emqx-extension-hook](https://github.com/emqx/emqx-extension-hook).

## Requirements

- JDK 1.8+
- Depend on `erlport.jar` (The communication module used in emqx-extension-hook)

## Get Started

1. First of all, create your Java project.
2. Download the [io.emqx.extension.jar](https://github.com/emqx/emqx-extension-java-sdk/releases) and [erlport.jar](https://github.com/emqx/emqx-extension-java-sdk/blob/master/deps/erlport-v1.1.1.jar)
3. Add the sdk: `io.emqx.extension.jar` and `erlport.jar` to your project dependency.
4. Copy the `examples/SampleHandler.java` into your project.
5. Successfully compile all the source codes.

Note: NOT read/write `System.out.*` and `System.in` Stream. They are used to communicate with EMQ X.

## Deploy

After compiled all source codes, you should deploy the `sdk` and your class files into EMQ X.

1. Copy the `io.emqx.extension.jar` to `emqx/data/extension` directory.
2. Copy your class files, e.g: `SampleHandler.class` to `emqx/data/extension` directory.
3. Modify the `emqx/etc/plugins/emqx_extension_hook.conf` file. e.g:

```protperties
exhook.drivers = java
## Search path for scripts or library
exhook.drivers.java.path = data/extension/
exhook.drivers.java.init_module = SampleHandler
```
4. Execute `bin/emqx console` to start EMQ X and load the `emqx_extension_hook` plugin.
5. Try to establish a MQTT connection and observe the console output.

## Examples

See: examples/SampleHandler.java

## License

Apache License v2

## Autor

- [Chong Yuan](https://github.com/chongyuanyin)
